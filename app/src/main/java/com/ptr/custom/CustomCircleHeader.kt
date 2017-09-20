package com.ptr.custom

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.ptr.R
import com.refresh.PtrHandler
import com.refresh.RefreshLayout

/**
 * Created by CJJ on 2017/9/18.
 *@author CJJ
 */
class CustomCircleHeader : View, PtrHandler {

    override fun getView(): View {
        return this
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)


    var state: Int = 0
    private var circleIcon: Bitmap
    private var ratio = 0f
    private var degree: Float = 0f

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        //这个环形箭头图片有一些问题。环形箭头是CircleImageView的一个内部类，是可以代码画出来的，可以去SwipeRefreshLayout代码里面找一下
        circleIcon = BitmapFactory.decodeResource(context.resources, R.mipmap.loading_circle)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(150 + circleIcon.height, MeasureSpec.EXACTLY))
    }

    override fun onIdle(refreshView: RefreshLayout, ptr: View) {
        state = 0
        invalidate()
    }

    override fun onPrepare(refreshView: RefreshLayout, ptr: View) {
        onIdle(refreshView, ptr)
    }

    override fun onLoading(refreshView: RefreshLayout, ptr: View) {
        state = 1
        invalidate()
    }

    override fun onOffsetChange(f: Float) {
        ratio = f
        invalidate()
    }


    override fun onDraw(canvas: Canvas) {
        if (state == 1) {
            canvas.save()
            degree += 7f
            canvas.rotate(degree, width / 2f, height / 2f)
            val x = measuredWidth / 2f - circleIcon.width / 2f
            val y = measuredHeight / 2f - circleIcon.height / 2f
            canvas.drawBitmap(circleIcon, x, y, null)
            canvas.restore()
            invalidate()
        } else {
            canvas.save()
            val degree = 360 * ratio
            //图片圆环并不是居中的，加上7像素修正一些
            canvas.rotate(degree, width / 2f, height / 2f + 7)
            val x = measuredWidth / 2f - circleIcon.width / 2f
            val y = measuredHeight / 2f - circleIcon.height / 2f
            canvas.drawBitmap(circleIcon, x, y, null)
            canvas.restore()
        }
    }
}