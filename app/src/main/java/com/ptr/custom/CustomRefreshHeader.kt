package com.ptr.custom

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.ptr.R
import com.refresh.PtrHandler
import com.refresh.RefreshLayout

/**
 * Created by CJJ on 2017/9/18.
 *@author CJJ
 */
class CustomRefreshHeader : View, PtrHandler {
    override fun getView(): View {
        return this
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private var textPaint: TextPaint = TextPaint()

    private var idleTextWidth: Float
    private var tenseTextWidth: Float


    private val IDLE_TEXT = "Pull To Refresh"
    private val TENSE_TEXT = "Release To Refresh"
    private var loadingIcon: Bitmap
    private var state: Int = 0
    private var degree = 0f

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        loadingIcon = BitmapFactory.decodeResource(context.resources, R.mipmap.custom_header_loading)
        textPaint.color = Color.BLACK
        textPaint.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 13f, resources.displayMetrics)
        textPaint.textAlign = Paint.Align.LEFT
        idleTextWidth = textPaint.measureText(IDLE_TEXT)
        tenseTextWidth = textPaint.measureText(TENSE_TEXT)
    }


    override fun onDraw(canvas: Canvas) {
        if (state == 0) {
            val x = measuredWidth / 2 - idleTextWidth / 2
            val y = measuredHeight / 2 - textPaint.fontMetrics.ascent - textPaint.textSize / 2f
            canvas.drawText(IDLE_TEXT, x, y, textPaint)
        } else if (state == 1) {
            val x = measuredWidth / 2 - tenseTextWidth / 2
            val y = measuredHeight / 2 - textPaint.fontMetrics.ascent - textPaint.textSize / 2f
            canvas.drawText(TENSE_TEXT, x, y, textPaint)
        } else {
            canvas.save()
            degree += 7f
            canvas.rotate(degree, width / 2f, height / 2f)
            val x = measuredWidth / 2f - loadingIcon.width / 2f
            val y = measuredHeight / 2f - loadingIcon.height / 2f
            canvas.drawBitmap(loadingIcon, x, y, null)
            canvas.restore()
            invalidate()
        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(150 + loadingIcon.height, MeasureSpec.EXACTLY))
    }

    override fun onIdle(refreshView: RefreshLayout) {
        state = 0
        invalidate()
    }

    override fun onPrepare(refreshView: RefreshLayout) {
        state = 1
        invalidate()
    }

    override fun onLoading(refreshView: RefreshLayout) {
        state = 2
        invalidate()
    }

    override fun onPositionChange(ratio: Float) {

    }
}