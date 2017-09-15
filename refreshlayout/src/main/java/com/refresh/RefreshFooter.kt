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

class RefreshFooter : View {

    var loadingIcon: Bitmap
    var state: Int = RefreshLayout.PTR_IDLE
    var RELEASE_LOADING_TEXT: String
    var textPaint: TextPaint
    var textWidth: Float = 0f
    var space: Int = 0
    var angle: Float = 0f
    val DEGREE = 10f

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        loadingIcon = BitmapFactory.decodeResource(context.resources, R.mipmap.item)
        space = context.resources.getDimensionPixelSize(R.dimen.refreshview_space)
        RELEASE_LOADING_TEXT = context.resources.getString(R.string.release_load_more)
        textPaint = TextPaint()
        textPaint.color = Color.BLACK
        textPaint.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 13f, resources.displayMetrics)
        textPaint.textAlign = Paint.Align.LEFT
        textWidth = textPaint.measureText(RELEASE_LOADING_TEXT)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(space * 2 + loadingIcon.height, MeasureSpec.EXACTLY))
    }

    override fun onDraw(canvas: Canvas) {
        if (state == RefreshLayout.PTR_LOADING) {
            angle += DEGREE
            canvas.save()
            canvas.rotate(angle, measuredWidth / 2f, measuredHeight / 2f)
            canvas.drawBitmap(loadingIcon, measuredWidth / 2f - loadingIcon.width / 2, measuredHeight / 2f - loadingIcon.height / 2, null)
            canvas.restore()
            postInvalidateOnAnimation()
        } else if (state == RefreshLayout.PTR_TENSE){
            val x = measuredWidth / 2 - textWidth / 2
            var y = measuredHeight / 2 - textPaint.fontMetrics.ascent - textPaint.textSize / 2f
            canvas.drawText(RELEASE_LOADING_TEXT, x, y, textPaint)
        }
    }

    fun onStateChange(state: Int) {
        this.state = state
        invalidate()
    }
}
