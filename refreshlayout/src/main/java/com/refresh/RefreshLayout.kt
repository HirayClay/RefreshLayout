package com.refresh

import android.content.Context
import android.graphics.Canvas
import android.support.v4.view.NestedScrollingParent
import android.support.v4.view.NestedScrollingParentHelper
import android.support.v4.view.ViewCompat
import android.support.v4.widget.ScrollerCompat
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.Interpolator

/**
 * Created by CJJ on 2017/9/6.
 * 刷新控件一般有两种一种加一个头部，如果还需要加载更多那就加上尾巴,让头部或者尾巴的层级低一些，在内容图层的下方，当然也可以
 * 一开始在onLayout方法处让头部layout在边界之外（或者改变LayoutParam的margin让不可见），下拉或者上拉的时候改变其位置；不过前者实现起来更方便一些，逻辑会简单一些
 * （只需要给头部刷新控件传入适当的参数即可，不需要控制位置;打开边界布局，我看了ele 安卓客户端首页刷新的实现貌似就是这样做的）,后者有一点麻烦.
 * 用实现NestedScrollingParent接口的方式自定义一个ViewGroup的方式来做是最合适的，比起往Recycler.Adapter里面
 * 加入Item要好的多，因为加入Item的方式某种程度上来说让你的Adapter不得不多一层封装，RecyclerView需要加入刷新的逻辑，让Adapter
 * 和RecyclerView变得复杂了，RecyclerView本身也变味了;而用嵌套滚动机制实现一个刷新控件十分自然，因为对View滑动几乎拥有绝对的控制权
 * ,而且流程是非常清晰的；除了用来做刷新，当需要配合滚动做其他效果，也十分的容易（比如UC浏览器首页那个效果）。
 * 这个项目本身最核心的就是RefreshLayout，另外提供了接口可以自定义自己的Header或者Footer，默认的Header不是太漂亮，github上有很多好看的头部控件，
 * 不过我觉得太花哨除了练习一下自定义效果并没有什么用，基本上公司不会允许用进自家的项目，一般都是每个公司自己设计的刷新头部，所以，还是自己定义吧，demo里面有比较完整简单的例子
 *
 * @author CJJ
 */

class RefreshLayout : ViewGroup, NestedScrollingParent {
    val DEBUG = false


    companion object {
        val IDLE = 0
        val DRAGGING = 1
        //settling to certain position
        val SETTLING = 2
        val REFRESHING_OR_LOADING = 3
        //force settle to top
        val FORCE = 4
        val SETTLING_DURATION = 400 //ms

        //tags for ptr
        val PTR_IDLE = 0
        //indicate that ptr is in tense ,once release will be in loading//松手就会刷新的状态
        val PTR_TENSE = 1
        //ptr is in loading
        val PTR_LOADING = 2

        //ptr cannot load more or disable the refresh
        val PTR_DISABLE = 3

        val MAX_VEL = 28000f
        val BASE_PULL_DOWN_HEIGHT = 1000
    }

    var headerState = PTR_IDLE
    var footerState = PTR_IDLE


    private val durationInterpolator = Interpolator { t -> 1 - t * t * t * t * t }
    val TAG = "RefreshLayout"
    var mHeader: View
    var mFooter: View
    var mHeaderHandler: PtrHandler
    var mFooterHandler: PtrHandler
    var mScroller: ScrollerCompat
    private var resourceId: Int = -1
    lateinit var mTarget: View
    var listener: RefreshListener? = null
    var touchSlop: Int
    var state: Int = IDLE

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private var mSettleDuration: Int

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.RefreshLayout)
        mSettleDuration = ta.getInt(R.styleable.RefreshLayout_refresh_settle_duration, 240)
        mHeaderHandler = RefreshHeader(context)
        mHeader = mHeaderHandler.getView()
        mFooterHandler = RefreshFooter(context)
        mFooter = mFooterHandler.getView()

        resourceId = ta.getResourceId(R.styleable.RefreshLayout_targetId, -1)
        mScroller = ScrollerCompat.create(context, AccelerateInterpolator())
        addView(mHeader)
        addView(mFooter)
        touchSlop = ViewConfiguration.get(context).scaledTouchSlop
        ta.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (resourceId != -1)
            mTarget = findViewById(resourceId)
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)

    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return super.onInterceptTouchEvent(ev)
    }

    override fun onStopNestedScroll(child: View?) {
        settle()
    }


    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        return ViewCompat.SCROLL_AXIS_VERTICAL and nestedScrollAxes != 0
    }


    override fun onNestedScrollAccepted(child: View, target: View, axes: Int) {
        //a new nested scroll start ,reset the state to "IDLE"
        if (state == FORCE)
            state = IDLE
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        consumed[0] = 0
        consumed[1] = preScroll(dy)

    }

    override fun onNestedScroll(target: View?, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
        mTarget.offsetTopAndBottom(-dyUnconsumed)
    }


    override fun onNestedPreFling(target: View?, velocityX: Float, velocityY: Float): Boolean {
        return preFling(velocityY)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        measureChildren(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        //header
        run {
            val top = -mHeader.measuredHeight
            val left = 0
            val right = mHeader.measuredWidth
            val bottom = 0
            mHeader.layout(left, top, right, bottom)
        }

        //target
        run {
            val left = 0
            val top = 0
            val right = measuredWidth
            val bottom = mTarget.measuredHeight
            mTarget.layout(left, top, right, bottom)
        }

        //footer
        run {
            val left = 0
            val top = measuredHeight
            val right = mFooter.measuredWidth
            val bottom = top + mFooter.measuredHeight
            mFooter.layout(left, top, right, bottom)
        }
    }


    @Suppress("VARIABLE_WITH_REDUNDANT_INITIALIZER")
    private fun preScroll(dy: Int): Int {
        if (DEBUG)
            Log.i(TAG, "before::::::::::::::::::$dy ${stateString(state)}")
        if (state == SETTLING || state == FORCE)
            return dy
        state = DRAGGING
        var consumedY = 0

        //the header current top pos
        val curTop = mTarget.top
        //we don't handle it if contentView is at top,let the nested scrolling view handle it first
        //if the nested scrolling view don't need it ,we still have chance to take "dyUnconsumed" in {#link onNestedScroll}

        //先不处理，交给内部View处理，如果内部View不处理自然还会返回给自己处理
        //而在onNestedScroll里面直接偏移掉剩余的部分
        //如果是上拉到顶了，那么onNestedScroll执行之后导致curTop<0进入上拉加载更多处理的逻辑；
        //如果是下来到顶了，那么onNestedScroll执行之后导致curTop>0进入下拉加载更多处理的逻辑.
        if (curTop == 0) {
            consumedY = 0
        } else if (curTop > 0) {

            //note that,nested scrolling view don't need to handle the dy while pulling down with curTop >0
            //otherwise ,we only take the  part that contentView can be at top


            //处理边界越界和消耗计算的问题


            //这里需要注意的是，curTop>0时，如果我们是下拉，内部滚动View是不需要处理的，全部自己处理；
            //如果上拉，则只消耗刚好让contentView到达顶部的距离即可,处理滑动也是如此

            if (-dy > 0) {
                consumedY = dy
            } else {
                if (curTop - dy <= 0)
                    consumedY = curTop
                else consumedY = dy
            }


            var scrollY = computeRealScrollY(-dy, curTop)

            //如果滑动距离过大,只允许滑动到curTop=0，剩余的偏移量应该交给内部View
            if (scrollY + curTop < 0) {
                scrollY = -curTop
                consumedY = curTop
            }
            val headerTop = headerPos(scrollY + mTarget.top)
            mHeader.layout(0, headerTop, mHeader.measuredWidth, headerTop + mHeader.measuredHeight)
            mTarget.offsetTopAndBottom(scrollY)

        } else {

            //take all while pulling up
            if (-dy < 0)
                consumedY = dy
            else {
                if (curTop - dy >= 0)
                    consumedY = curTop
                else
                    consumedY = dy
            }

            var scrollY = computeRealScrollY(-dy, curTop)
            if (scrollY + curTop >= 0) {
                scrollY = -curTop
                consumedY = curTop
            }


            val footerTop = footerPos(scrollY + mTarget.top)
            if (footerTop != mFooter.top)
                mFooter.layout(0, footerTop, mFooter.measuredWidth, footerTop + mFooter.measuredHeight)
            mTarget.offsetTopAndBottom(scrollY)
        }
        if (DEBUG)
            Log.i(TAG, "after::::::::::::::::::$dy ${stateString(state)}")

        if (curTop != 0)
            refreshState()
        return consumedY
    }


    /**
     * ....how do i use the velocity to compute the appropriate settling duration???
     * in fact i omit the fling's influence on destTop, destTop depends on the curTop
     */
    private fun preFling(yVel: Float): Boolean {
        val currTop = mTarget.top

        //let nested scrolling view handle it,none of your business
        //直接交给内部View处理，自己不要管闲事
        if (currTop == 0)
            return false


        if (currTop <= -mFooter.measuredHeight) {
            val duration = (SETTLING_DURATION * durationInterpolator.getInterpolation(yVel / MAX_VEL)).toInt() / 3
            mScroller.startScroll(0, currTop, 0, -mFooter.measuredHeight - currTop, duration)
            state = SETTLING
            footerState = PTR_LOADING
            mFooterHandler.onLoading(this)
            if (this.listener != null)
                this.listener!!.onLoadMore()
            ViewCompat.postInvalidateOnAnimation(this)
            return true
        } else if (currTop >= mHeader.measuredHeight) {
            mScroller.startScroll(0, currTop, 0, mHeader.measuredHeight - currTop, 100)
            state = SETTLING
            headerState = PTR_LOADING
            mHeaderHandler.onLoading(this)
            if (this.listener != null)
                this.listener!!.onRefresh()
            ViewCompat.postInvalidateOnAnimation(this)
            return true
        } else {
            mScroller.startScroll(0, currTop, 0, -currTop, 100)
            state = SETTLING
            ViewCompat.postInvalidateOnAnimation(this)
        }

        //让内部View处理,自己不要搞事情
        return false
    }


    //我们这里只是改变刷新控件的状态，并且给footer ，header打下标记，这些标记对应的行为(也就是说这些标记是为了settle准备的)
    // 在onStopNestedScroll里面去触发
    private fun refreshState() {
        // if header is not in refreshing
        if (headerState != PTR_LOADING)
            if (mHeader.top <= 0) {
                headerState = PTR_IDLE
                mHeaderHandler.onIdle(this)
            } else {
                headerState = PTR_TENSE
                mHeaderHandler.onPrepare(this)
            }
        mHeaderHandler.onPositionChange(zeroConstrain(mHeader.bottom.toFloat() / mHeader.height))

        //same as header
        if (footerState != PTR_LOADING)
            if (mFooter.bottom <= measuredHeight) {
                footerState = PTR_TENSE
                mFooterHandler.onPrepare(this)
            } else {
                if (footerState != PTR_IDLE)
                    mFooterHandler.onIdle(this)
                footerState = PTR_IDLE
            }
    }


    /**
     * settle有以下几种情况
     * 1. 不满足刷新，让header恢复到idle位置，target重置
     * 2. 满足刷新，让header 刷新，target回复到header.height的位置
     */

    private fun settle() {
        val curTop = mTarget.top
        val destTop: Int

        var duration = mSettleDuration


        //already in "settling" state or start a new nest scroll ,cancel this settle
        if (curTop == 0 || state == SETTLING || state == FORCE)
            return

        if (headerState == PTR_TENSE) {
            destTop = mHeader.measuredHeight
            headerState = PTR_LOADING
            mHeaderHandler.onLoading(this)
            duration = linearDuration(Math.abs(destTop - curTop))
            if (this.listener != null)
                this.listener!!.onRefresh()
        } else if (footerState == PTR_TENSE) {
            destTop = -mFooter.measuredHeight
            duration = linearDuration(Math.abs(destTop - curTop))
            footerState = PTR_LOADING
            mFooterHandler.onLoading(this)
            if (this.listener != null)
                this.listener!!.onLoadMore()
        } else {
            headerState = PTR_IDLE
            footerState = PTR_IDLE
            mHeaderHandler.onIdle(this)
            destTop = 0
        }

        state = SETTLING
        val deltaY = destTop - curTop
        mScroller.startScroll(0, curTop, 0, deltaY, duration)
        ViewCompat.postInvalidateOnAnimation(this)
    }

    override fun computeScroll() {

        if (mScroller.computeScrollOffset()) {
            val curY = mScroller.currY
            val scrollY = curY - mTarget.top
            val headerTop = headerPos(mTarget.top + scrollY)
            val footerTop = footerPos(mTarget.top + scrollY)
            mHeader.offsetTopAndBottom(headerTop - mHeader.top)
            mFooter.offsetTopAndBottom(footerTop - mFooter.top)
            mTarget.offsetTopAndBottom(scrollY)
            ViewCompat.postInvalidateOnAnimation(this)
        } else {
            if (state == SETTLING) {
                if (mScroller.finalY == mHeader.height || mScroller.finalY == -mFooter.height)
                    state = REFRESHING_OR_LOADING
                else if (mScroller.finalY == 0) {
                    state = IDLE
                }
            }
            //settle完成之后
            if (state == FORCE) {
                headerState = PTR_IDLE
                footerState = PTR_IDLE
                mHeaderHandler.onIdle(this)
                mFooterHandler.onIdle(this)
            }
        }

    }

    /**
     * scroll with resistance ,use piece-wise function
     * 模拟阻尼效果,直接乘以一个常数就好了
     */
    private fun computeRealScrollY(dy: Int, distance: Int): Int {

        if (distance in -2 * mFooter.height..2 * mHeader.height)
            return dy / 2
        else return if (dy > 0) 1 else -1
    }

    private fun headerPos(distance: Int): Int {
        val H = mHeader.measuredHeight
        val r: Int
        if (distance in 0..H)
            r = distance - H
        else if (distance > H) {
            r = ((distance - H) / 2f).toInt()
        } else {
            r = -H
        }

        return r
    }

    private fun footerPos(distance: Int): Int {
        val H = mFooter.measuredHeight
        val r: Int
        if (distance in -H..0) {
            r = distance + measuredHeight
        } else if (distance < -H) {
            r = ((distance + H) / 2f).toInt() + measuredHeight - H
        } else {
            r = measuredHeight
        }
        return r
    }

    /**
     * linear duration is bad for effect
     */
    private fun linearDuration(distance: Int): Int {
        val r = ((distance + 0f) / BASE_PULL_DOWN_HEIGHT * SETTLING_DURATION).toInt()
        return if (r > 500) 500 else r
    }

    fun setRefreshListener(listener: RefreshListener) {
        this.listener = listener
    }


    fun setHeader(header: PtrHandler) {
        removeView(mHeader)
        this.mHeader = header.getView()
        this.mHeaderHandler = header
        addView(mHeader)
        requestLayout()
    }

    fun onRefreshComplete() {
        //settle()
        mHeaderHandler.onRefreshComplete(this)
        //delay  invoke
        forceSettleAtTop()
    }

    fun onLoadMoreComplete() {
        mFooterHandler.onLoadMoreComplete(this)
        forceSettleAtTop()
    }

    private fun forceSettleAtTop() {
        state = FORCE
        val curTop = mTarget.top
        val deltaY = -curTop
        mScroller.startScroll(0, curTop, 0, deltaY, mSettleDuration)
        ViewCompat.postInvalidateOnAnimation(this)
    }

    private fun zeroConstrain(value: Float): Float {
        if (value <= 0f)
            return 0f
        else return value
    }


    private fun stateString(state: Int) = when (state) {
        IDLE -> "IDLE"
        DRAGGING -> "DRAGGING"
        REFRESHING_OR_LOADING -> "REFRESHING_OR_LOADING"
        SETTLING -> "SETTLING"
        FORCE -> "FORCE"
        else -> ""
    }
}
