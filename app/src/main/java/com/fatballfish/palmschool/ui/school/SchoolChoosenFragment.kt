package com.fatballfish.palmschool.ui.school

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fatballfish.palmschool.R
import com.fatballfish.palmschool.logic.dao.ActivityDao
import com.fatballfish.palmschool.logic.model.school.School
import com.fatballfish.palmschool.ui.mine.RealAuthUpdateViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_school_choosen.*
import kotlinx.android.synthetic.main.fragment_school_choosen.toolbar_userInfoEdit

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "key"
private const val ARG_PARAM2 = "value"

/**
 * A simple [Fragment] subclass.
 * Use the [SchoolChoosenFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class SchoolChoosenFragment : Fragment() {
    private var key: String? = null
    private var value: String? = null
    val schoolListViewModel by lazy { ViewModelProvider(this)[SchoolListViewModel::class.java] }
    val realAuthUpdateViewModel by lazy { ViewModelProvider(this)[RealAuthUpdateViewModel::class.java] }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            key = it.getString(ARG_PARAM1)
            value = it.getString(ARG_PARAM2)
        }
        activity?.setResult(ActivityDao.RESULT_CANCEL)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_school_choosen, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.setResult(Activity.RESULT_CANCELED)
        // 设置toolbar和菜单
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(toolbar_userInfoEdit)
        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setTitle("选择学校")
        }
        // 列表配置
        recyclerView_schoolList.layoutManager = LinearLayoutManager(context)
        val adapter = SchoolAdapter(schoolListViewModel.schoolList, this)
        recyclerView_schoolList.adapter = adapter
        // ViewModel
        schoolListViewModel.schoolListLiveData.observe(viewLifecycleOwner, Observer { result ->
            val data = result.getOrNull()
            if (data != null) {
                schoolListViewModel.schoolList.clear()
                val schoolList = data.list
                schoolListViewModel.schoolList.addAll(schoolList)
                adapter.notifyDataSetChanged()
            } else {
                result.exceptionOrNull()?.printStackTrace()
                Snackbar.make(
                    recyclerView_schoolList,
                    "获取学校列表失败|${result.exceptionOrNull()?.message}",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })
        realAuthUpdateViewModel.realAuthUpdateLiveData.observe(
            viewLifecycleOwner,
            Observer { result ->
                val data = result.getOrNull()
                if (data != null) {
                    activity?.setResult(ActivityDao.RESULT_OK)
                    Toast.makeText(activity, "更新成功！", Toast.LENGTH_SHORT).show()
                    activity?.finish()
                } else {
                    result.exceptionOrNull()?.printStackTrace()
                    Snackbar.make(
                        recyclerView_schoolList,
                        "更新用户信息失败|${result.exceptionOrNull()?.message}",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            })
        // 文本框内容改变事件监听
        edit_searchSchool.addTextChangedListener { text ->
            val content = text.toString()
            if (content.isNotEmpty()) {
                val map = mapOf("key" to content)
                schoolListViewModel.getSchoolList(map)
            } else {
                schoolListViewModel.getSchoolList(mapOf("key" to ""))
            }
        }
        initUI()
    }

    private fun initUI() {
        // 开局获取所有学校列表
        schoolListViewModel.getSchoolList(mapOf("key" to ""))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                activity?.finish()
            }
        }
        return true
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param key Parameter 1.
         * @param value Parameter 2.
         * @return A new instance of fragment SchoolChoosenFragment.
         */
        @JvmStatic
        fun newInstance(key: String, value: String) =
            SchoolChoosenFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, key)
                    putString(ARG_PARAM2, value)
                }
            }
    }
}