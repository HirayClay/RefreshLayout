package com.refresh

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

/**
 * Created by CJJ on 2017/9/6.

 * @author CJJ
 */

class RefreshFooter : View, PtrHandler {
    override fun getView(): View {
        return this
    }

    override fun onIdle(refreshView: RefreshLayout) {
        this.state = RefreshLayout.IDLE
        invalidate()
    }

    override fun onPrepare(refreshView: RefreshLayout) {
        this.state = RefreshLayout.PTR_TENSE
        invalidate()
    }

    override fun onLoading(refreshView: RefreshLayout) {
        this.state = RefreshLayout.PTR_LOADING
        invalidate()
    }

    override fun onPositionChange(ratio: Float) {
        /*no op*/
    }


    var loadingIcon: Bitmap
    var state: Int = RefreshLayout.PTR_IDLE
    var RELEASE_LOADING_TEXT: String
    var IDLE_TEXT: String
    var textPaint: TextPaint
    var loadMoreTextWidth: Float = 0f
    var idleTextWidth: Float = 0f
    var space: Int = 0
    var angle: Float = 0f
    val DEGREE = 10f

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        loadingIcon = BitmapFactory.decodeResource(context.resources, R.mipmap.item)
        space = context.resources.getDimensionPixelSize(R.dimen.refreshview_space)
        RELEASE_LOADING_TEXT = context.resources.getString(R.string.release_load_more)
        IDLE_TEXT = resources.getString(R.string.refresh_footer_idle_text)
        textPaint = TextPaint()
        textPaint.color = Color.BLACK
        textPaint.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 13f, resources.displayMetrics)
        textPaint.textAlign = Paint.Align.LEFT
        loadMoreTextWidth = textPaint.measureText(RELEASE_LOADING_TEXT)
        idleTextWidth = textPaint.measureText(IDLE_TEXT)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(space * 3 + loadingIcon.height, MeasureSpec.EXACTLY))
    }

    override fun onDraw(canvas: Canvas) {
        if (state == RefreshLayout.PTR_LOADING) {
            angle += DEGREE
            canvas.save()
            canvas.rotate(angle, measuredWidth / 2f, measuredHeight / 2f)
            canvas.drawBitmap(loadingIcon, measuredWidth / 2f - loadingIcon.width / 2, measuredHeight / 2f - loadingIcon.height / 2, null)
            canvas.restore()
            postInvalidateOnAnimation()
        } else if (state == RefreshLayout.PTR_TENSE) {
            val x = measuredWidth / 2 - loadMoreTextWidth / 2
            var y = measuredHeight / 2 - textPaint.fontMetrics.ascent - textPaint.textSize / 2f
            canvas.drawText(RELEASE_LOADING_TEXT, x, y, textPaint)
        } else {
            val x = measuredWidth / 2 - idleTextWidth / 2
            var y = measuredHeight / 2 - textPaint.fontMetrics.ascent - textPaint.textSize / 2f
            canvas.drawText(IDLE_TEXT, x, y, textPaint)
        }
    }

    fun onStateChange(state: Int) {

    }
}
