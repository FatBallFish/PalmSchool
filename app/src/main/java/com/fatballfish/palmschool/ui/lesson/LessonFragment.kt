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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_lessons.*

@AndroidEntryPoint
class LessonFragment : Fragment() {
    val lessonTemplateViewModel by lazy { ViewModelProvider(this)[LessonTemplateViewModel::class.java] }
    val templateViewModel by lazy { ViewModelProvider(this)[TemplateListViewModel::class.java] }
    val currentTemplateViewModel by lazy { ViewModelProvider(this)[CurrentTemplateViewModel::class.java] }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lessons, container, true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // viewModel
        templateViewModel.templateListLiveData.observe(viewLifecycleOwner, Observer { result ->
            val data = result.getOrNull()
            if (data != null) {
                templateViewModel.templateList.clear()
                templateViewModel.templateList.addAll(data.list)
                val token = templateViewModel.getToken()
                var tid = templateViewModel.getTemplateID(token)
                if (templateViewModel.templateList.size == 0) {
                    Log.d("Template", "模板列表元素数为0")
                    Toast.makeText(context, "暂无可选择的课表模版", Toast.LENGTH_SHORT).show()
                    layout_swipeRefresh.isRefreshing = false
                    return@Observer
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
        currentTemplateViewModel.getCurrentTemplateLiveData.observe(
            viewLifecycleOwner,
            Observer { result ->
                val data = result.getOrNull()
                if (data != null) {
                    val tid = data.tid
                    if (tid != -1) {
                        Log.d("LessonFragment", "存在默认课表tid:$tid")
                        val save_result =
                            templateViewModel.saveTemplateID(templateViewModel.getToken(), tid)
                        Log.d("LessonFragment", "tid保存结果：$save_result")
                        refreshLessonTemplate()
                    } else {
                        Toast.makeText(context, "请选择一个课表模版", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(
                        context,
                        "用户当前课表模版信息获取失败|${result.exceptionOrNull()?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                layout_swipeRefresh.isRefreshing = false
            })
        currentTemplateViewModel.updateCurrentTemplateLiveData.observe(
            viewLifecycleOwner,
            Observer { result ->
                val data = result.getOrNull()
                if (data != null) {
                    templateViewModel.saveTemplateID(templateViewModel.getToken(), data.tid)
                    refreshLessonTemplate()
                    Toast.makeText(context, "模版切换成功", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        context,
                        "模版切换失败|${result.exceptionOrNull()?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                layout_swipeRefresh.isRefreshing = false
            })
        if (activity is MainActivity) {
            // 定义广播
            val intentFilter = IntentFilter()
            intentFilter.addAction(ActivityDao.ACTION_REFRESH_TEMPLATE)
            (activity as MainActivity).receiver = TemplateRefreshReceiver()
            activity?.registerReceiver((activity as MainActivity).receiver, intentFilter)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 刷新容器
        layout_swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
        layout_swipeRefresh.setOnRefreshListener {
            getCurrentTemplate()
//            getTemplateList()
        }
        initUI()
    }

    private fun initUI() {
        if (currentTemplateViewModel.isTokenSaved()) {
            getCurrentTemplate()
//            getTemplateList()
        }
    }

    private fun insertTokens(token: String, username: String) {
        val db = PalmSchoolApplication.db
        val cursor = db.rawQuery("SELECT * FROM tokens WHERE token = ?", arrayOf(token))
        Log.d("SQL", "insertTokens:count:${cursor.count}")
        if (cursor.count == 0) {
            val tokens = ContentValues().apply {
                put("token", token)
                put("username", username)
            }
            val ret_id = db.insert(PalmSchoolApplication.TABLE_TOKEN, null, tokens)
//            Toast.makeText(this, "insert ret_id:$ret_id", Toast.LENGTH_SHORT).show()
            Log.d("SQL", "insert ret_id:$ret_id")
        }
        cursor.close()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            ActivityDao.REQUEST_LOGIN -> {
                if (resultCode == ActivityDao.RESULT_OK) {
                    val username = data?.getStringExtra("phone") ?: ""
                    val token = data?.getStringExtra("token") ?: ""
                    val loginType = data?.getStringExtra("login_type")
                    insertTokens(token, username)
                    lessonTemplateViewModel.saveToken(token)
                    getCurrentTemplate()
//                    getTemplateList()
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

    private fun getCurrentTemplate() {
        layout_swipeRefresh.isRefreshing = true
        if (currentTemplateViewModel.isTokenSaved()) {
            Log.d("LessonFragment", "获取用户当前课表模版id：token存在")
            currentTemplateViewModel.getCurrentTemplateId()
        } else {
            Log.d("LessonFragment", "获取用户当前课表模版id：token不存在")
            layout_swipeRefresh.isRefreshing = false
            Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, LoginActivity::class.java)
            startActivityForResult(intent, ActivityDao.REQUEST_LOGIN)
        }
    }

    private fun clearCls() {
        lessonTemplateViewModel.lessonTableList.clear()
        layout_lessonWeek.removeAllViews()
    }

    inner class TemplateRefreshReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("BroadcastReceiver", "收到通知")
            val from = intent?.getStringExtra("from")
            val tid = intent?.getIntExtra("tid", -1) ?: -1
            Log.d("Template", "broadcast tid:$tid")
            when (from) {
                "login" -> {
                    clearCls()
                    getCurrentTemplate()
                }
                "template" -> {
                    if (tid != -1) {
                        currentTemplateViewModel.updateCurrentTemplateId(tid)
                    }
                }
                "logout" -> {
                    clearCls()
                }
            }

        }
    }
}