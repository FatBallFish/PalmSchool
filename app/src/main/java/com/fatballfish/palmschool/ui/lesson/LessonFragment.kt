package com.fatballfish.palmschool.ui.lesson

import android.content.*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.fatballfish.palmschool.MainActivity
import com.fatballfish.palmschool.PalmSchoolApplication
import com.fatballfish.palmschool.R
import com.fatballfish.palmschool.logic.dao.ActivityDao
import com.fatballfish.palmschool.ui.login.LoginActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_lessons.*

class LessonFragment : Fragment() {
    lateinit var receiver: TemplateRefreshReceiver
    val lessonTemplateViewModel by lazy { ViewModelProvider(this)[LessonTemplateViewModel::class.java] }
    val templateViewModel by lazy { ViewModelProvider(this)[TemplateListViewModel::class.java] }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lessons, container, true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        templateViewModel.templateListLiveData.observe(viewLifecycleOwner, Observer { result ->
            val data = result.getOrNull()
            if (data != null) {
                templateViewModel.templateList.clear()
                templateViewModel.templateList.addAll(data.list)
                val token = templateViewModel.getToken()
                var tid = templateViewModel.getTemplateID(token)
                if (templateViewModel.templateList.size == 0) {
                    Log.d("Template", "模板列表元素数为0")
                    Toast.makeText(context, "未找到课程表,请先添加", Toast.LENGTH_SHORT).show()
                    layout_swipeRefresh.isRefreshing = false
                    return@Observer
                } else if (tid == -1 && templateViewModel.templateList.size > 0) {
                    Log.d("Template", "检测到无初始tid，且返回数据存在，默认使用第一个返回列表")
                    tid = templateViewModel.templateList[0].tid
                    Log.d("Template", "默认第一个列表tid:$tid")
                    val save_result = templateViewModel.saveTemplateID(token, tid)
                    Log.d("Temlate", "tid保存结果：$save_result")
                    lessonTemplateViewModel.current_tid = tid
                } else if (tid != -1) {
                    lessonTemplateViewModel.current_tid = tid
                }
                refreshLessonTemplate()
            } else {
                result.exceptionOrNull()?.printStackTrace()
                Snackbar.make(
                    layout_lessonWeek,
                    "模版列表获取失败|${result.exceptionOrNull()?.message}",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })
        lessonTemplateViewModel.personLessonListLiveData.observe(
            viewLifecycleOwner,
            Observer { result ->
                val data = result.getOrNull()
                if (data != null) {
                    val save_ret = lessonTemplateViewModel.saveTemplateID(
                        lessonTemplateViewModel.getToken(),
                        lessonTemplateViewModel.current_tid
                    )
                    Log.d("Template", "leesonTemplateViewModel 保存tid结果:$save_ret")
                    lessonTemplateViewModel.lessonTableList.clear()
                    lessonTemplateViewModel.lessonTableList.addAll(data.list)
                    // 递归填充课程表
                    layout_lessonWeek.removeAllViews()
                    for (lessonTable in lessonTemplateViewModel.lessonTableList) {
                        val lessonInfo =
                            "${lessonTable.name}\n${lessonTable.timeText}\n${lessonTable.teacher}\n${lessonTable.location}"
                        val lessonWeek = lessonTable.weekday
                        val start = lessonTable.lessons.min() ?: 0
                        val end = lessonTable.lessons.max() ?: 0
                        // 定义视图
                        val view =
                            LessonCardView(requireContext(), lessonInfo, lessonWeek, start, end)
                        val LayoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        layout_lessonWeek.addView(view, LayoutParams)
                    }
                } else {
                    result.exceptionOrNull()?.printStackTrace()
                    Snackbar.make(
                        layout_lessonWeek,
                        "课程获取失败|${result.exceptionOrNull()?.message}",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                layout_swipeRefresh.isRefreshing = false
            })
        if (activity is MainActivity) {
            // 定义广播
            val intentFilter = IntentFilter()
            intentFilter.addAction(ActivityDao.ACTION_REFRESH_TEMPLATE)
            receiver = TemplateRefreshReceiver()
            activity?.registerReceiver(receiver, intentFilter)
            Toast.makeText(context, "由主窗口载入", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 刷新容器
        layout_swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
        layout_swipeRefresh.setOnRefreshListener {
            getTemplateList()
        }
        initUI()
    }

    private fun initUI() {
        if (lessonTemplateViewModel.isTokenSaved()) {
            getTemplateList()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Toast.makeText(context, "Fragment 结果返回", Toast.LENGTH_SHORT).show()
        when (requestCode) {
            ActivityDao.REQUEST_LOGIN -> {
                if (resultCode == ActivityDao.RESULT_OK) {
                    val token = data?.getStringExtra("token") ?: ""
                    val loginType = data?.getStringExtra("login_type")
                    Toast.makeText(context, "$token|$loginType", Toast.LENGTH_SHORT).show()
                    lessonTemplateViewModel.saveToken(token)
//                    if (lessonTemplateViewModel.isTemplateIDSaved(token)) {
//                        val template_id = lessonTemplateViewModel.getTemplateID(token)
//                    }
                    getTemplateList()
                } else if (resultCode == ActivityDao.RESULT_CANCEL) {
                    Toast.makeText(context, "取消登录", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun refreshLessonTemplate() {
        layout_swipeRefresh.isRefreshing = true
        if (lessonTemplateViewModel.isTokenSaved()) {
            Log.d("LessonFragment", "刷新课程：token存在")
            val token = lessonTemplateViewModel.getToken()
            val template_id = lessonTemplateViewModel.getTemplateID(token)
            Log.d("LessonFragment", "刷新课程：tid：$template_id")
            lessonTemplateViewModel.getLessonTemplateList(token, template_id)
        } else {
            Log.d("LessonFragment", "刷新课程：token不存在")
            layout_swipeRefresh.isRefreshing = false
            Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, LoginActivity::class.java)
            startActivityForResult(intent, ActivityDao.REQUEST_LOGIN)
        }
    }

    private fun getTemplateList() {
        if (templateViewModel.isTokenSaved()) {
            Log.d("LessonFragment", "获取模板列表：token存在")
            val token = lessonTemplateViewModel.getToken()
            templateViewModel.getTemplateList(token, mapOf("mode" to "all"))
        } else {
            Log.d("LessonFragment", "获取模板列表：token不存在")
            layout_swipeRefresh.isRefreshing = false
            Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, LoginActivity::class.java)
            startActivityForResult(intent, ActivityDao.REQUEST_LOGIN)
        }
    }

    inner class TemplateRefreshReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Toast.makeText(activity, "收到通知", Toast.LENGTH_SHORT).show()
            val tid = intent?.getIntExtra("tid", -1) ?: -1
            Log.d("Template", "broadcast tid:$tid")
            if (tid != -1) {
                templateViewModel.saveTemplateID(templateViewModel.getToken(), tid)
            }
            getTemplateList()
        }
    }
}