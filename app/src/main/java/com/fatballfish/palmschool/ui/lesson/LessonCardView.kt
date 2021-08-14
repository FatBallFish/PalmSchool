package com.fatballfish.palmschool.ui.lesson

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.fatballfish.palmschool.R
import com.google.android.material.card.MaterialCardView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.lesson_info.view.*

class LessonCardView : MaterialCardView {
    var startClass = 0
    var stopClass = 0
    var lessonWeek = 0
    var lessonInfo = ""
    private lateinit var view: View
    private lateinit var relativeLayout: RelativeLayout
    private lateinit var textView: TextView

    constructor(context: Context) : this(context, null) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
        init(context, attrs)
    }

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        init(context, attributeSet)
    }

    constructor(
        context: Context,
        lessonInfo: String,
        lessonWeek: Int,
        startClass: Int,
        stopClass: Int
    ) : this(context, null, 0) {
        this.lessonInfo = lessonInfo
        this.lessonWeek = lessonWeek
        this.startClass = startClass
        this.stopClass = stopClass
        init(context)
    }

    fun init(context: Context) {

        view = LayoutInflater.from(context).inflate(R.layout.lesson_info, this, true)
        relativeLayout = layout_lessonInfo
        textView = text_lessonInfo
        setCardBackgroundColor(
            Color.argb(
                255,
                (0..255).random(),
                (0..255).random(),
                (0..255).random()
            )
        )
        setOnClickListener {
            Toast.makeText(context, lessonInfo, Toast.LENGTH_SHORT).show()
            Log.d("Lesson", "layout：${relativeLayout.width}:${relativeLayout.height}")
            Log.d("Lesson", "TextView：${textView.width}:${textView.height}")
        }
    }

    fun init(context: Context, attrs: AttributeSet?) {
        view = LayoutInflater.from(context).inflate(R.layout.lesson_info, this, true)
        // 获取子控件
        relativeLayout = layout_lessonInfo
        textView = text_lessonInfo

        setCardBackgroundColor(
            Color.argb(
                255,
                (0..255).random(),
                (0..255).random(),
                (0..255).random()
            )
        )
        // ===== 属性读取 ======
        val mattrs = context.obtainStyledAttributes(attrs, R.styleable.LessonCardView)
        startClass = mattrs.getInt(R.styleable.LessonCardView_startClass, 0)
        stopClass = mattrs.getInt(R.styleable.LessonCardView_stopClass, 0)
        lessonWeek = mattrs.getInt(R.styleable.LessonCardView_lessonWeek, 0)
        lessonInfo = mattrs.getString(R.styleable.LessonCardView_lessonInfo) ?: ""
        mattrs.recycle()
        setOnClickListener {
            Toast.makeText(context, lessonInfo, Toast.LENGTH_SHORT).show()
            Log.d("Lesson", "layout：${relativeLayout.width}:${relativeLayout.height}")
            Log.d("Lesson", "TextView：${textView.width}:${textView.height}")
        }
    }

    fun initSub() {
        relativeLayout.layout(0, 0, width, height)
        textView.layout(0, 0, width, height)
        textView.text = lessonInfo
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        initSub()
    }
}