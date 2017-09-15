package com.refresh

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

/**
 * Created by CJJ on 2017/9/6.
 * 头部刷新
 * @author CJJ
 */

class RefreshHeader : View {

    var space: Int = 0
    var arrowIcon: Bitmap
    var loadingIcon: Bitmap
    var textPaint: TextPaint
    val DEGREE = 10f
    val RELEASE_LOADING: String
    var angle = 0f
    val THRESHOLD = 0.8f

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private var textWidth: Float

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        space = context.resources.getDimensionPixelSize(R.dimen.refreshview_space)
        arrowIcon = BitmapFactory.decodeResource(context.resources, R.mipmap.arrow)
        loadingIcon = BitmapFactory.decodeResource(context.resources, R.mipmap.loading)
        RELEASE_LOADING = resources.getString(R.string.release_refresh)
        textPaint = TextPaint()
        textPaint.color = Color.BLACK
        textPaint.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 13f, resources.displayMetrics)
        textPaint.textAlign = Paint.Align.LEFT
        textWidth = textPaint.measureText(RELEASE_LOADING)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val h = Math.max(loadingIcon.height, arrowIcon.height) + space * 2 + textPaint.textSize + 10
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(h.toInt(), MeasureSpec.EXACTLY))
    }

    override fun onDraw(canvas: Canvas) {
        if (state == RefreshLayout.IDLE) {
            //just draw the arrow bitmap
            val left = measuredWidth / 2f - arrowIcon.width / 2
            val top = measuredHeight / 2f - 5 - arrowIcon.height / 2
            canvas.drawBitmap(arrowIcon, left, top, null)

        } else if (state == RefreshLayout.PTR_TENSE) {
            // when tense,draw arrow bitmap and the text
            val left = measuredWidth / 2f - arrowIcon.width / 2
            val top = measuredHeight / 2f - 10 - arrowIcon.height / 2
            canvas.drawBitmap(arrowIcon, left, top, null)
            var x = measuredWidth / 2 - textWidth / 2
            canvas.drawText(RELEASE_LOADING, x, space + arrowIcon.height + 10 + -textPaint.fontMetrics.top, textPaint)

        } else {//loading
            angle += DEGREE
            canvas.save()
            val left = measuredWidth / 2f - loadingIcon.width / 2f
            val top = measuredHeight / 2f - loadingIcon.height / 2f
            canvas.rotate(angle, (measuredWidth / 2).toFloat(), (measuredHeight / 2).toFloat())
            canvas.drawBitmap(loadingIcon, left, top, null)
            canvas.restore()
            invalidate()
        }
    }

    private var state: Int = RefreshLayout.IDLE


    //主要是告诉是头部目前处在怎样的状态
    fun onStateChange(state: Int) {
        this.state = state
        invalidate()
    }


}
