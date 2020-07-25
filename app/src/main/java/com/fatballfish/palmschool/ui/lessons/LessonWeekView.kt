package com.fatballfish.palmschool.ui.lessons

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.ViewGroup
import com.fatballfish.palmschool.R

class LessonWeekView(context: Context, attrs: AttributeSet) : ViewGroup(context, attrs) {
    private val widthLine = 7
    private val heightLine = 12
    private var sectionWidth: Float = 0.0f
    private var sectionHeight: Float = 0.0f
    private var dividerWidth = 5 // 子控件之间的间隔


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        sectionWidth = (width / widthLine).toFloat()
        sectionHeight = sectionWidth
        val height = (sectionHeight * heightLine).toInt()
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val count = childCount
        for (i in 0 until count) { // 获得子控件个数
            val lessonCardView = getChildAt(i) as LessonCardView
            val lessonWeek = lessonCardView.lessonWeek
            val startClass = lessonCardView.startClass
            val stopClass = lessonCardView.stopClass
            //计算左边的坐标
            val left = (sectionWidth * (lessonWeek - 1) + dividerWidth).toInt()
            //计算右边坐标
            val right = (left + sectionWidth - 2 * dividerWidth).toInt()
            //计算顶部坐标
            val top = (sectionHeight * (startClass - 1) + dividerWidth).toInt()
            //计算底部坐标
            val bottom =
                (top + (stopClass - startClass + 1) * sectionHeight - 2 * dividerWidth).toInt()
            lessonCardView.layout(left, top, right, bottom)
        }
    }

}