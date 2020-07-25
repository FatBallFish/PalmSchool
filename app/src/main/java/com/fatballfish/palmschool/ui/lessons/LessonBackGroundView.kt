package com.fatballfish.palmschool.ui.lessons

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.fatballfish.palmschool.R

class LessonBackGroundView(context: Context,attrs:AttributeSet) : View(context,attrs) {
    private val widthLine = 7
    private val heightLine = 12
    private var sectionWidth: Float = 0.0f
    private var sectionHeight: Float = 0.0f


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        sectionWidth = (width / widthLine).toFloat()
        sectionHeight = sectionWidth
        val height = (sectionHeight * heightLine).toInt()
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // 画横线
        val mpaint = Paint()
        mpaint.color = Color.GRAY
        mpaint.alpha = 255
        mpaint.style = Paint.Style.FILL
        mpaint.strokeWidth = 5f
        for (i in 0..heightLine) {
            canvas?.drawLine(0f, i * sectionHeight, width.toFloat(), i * sectionHeight, mpaint)
        }
        // 画竖线
        for (i in 0..widthLine) {
            canvas?.drawLine(i * sectionWidth, 0f, i * sectionWidth, height.toFloat(), mpaint)
        }
    }
}