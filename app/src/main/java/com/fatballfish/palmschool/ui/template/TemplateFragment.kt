package com.fatballfish.palmschool.ui.template

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fatballfish.palmschool.R
import com.fatballfish.palmschool.logic.dao.ActivityDao
import com.fatballfish.palmschool.ui.lesson.CurrentTemplateViewModel
import com.fatballfish.palmschool.ui.lesson.TemplateListViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_template.*

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "token"

/**
 * A simple [Fragment] subclass.
 * Use the [TemplateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TemplateFragment : Fragment() {
    private val templateViewModel by lazy { ViewModelProvider(this)[TemplateListViewModel::class.java] }
    val currentTemplateViewModel by lazy { ViewModelProvider(this)[CurrentTemplateViewModel::class.java] }
    private lateinit var adapter: TemplateAdapter
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            token = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_template, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (activity is TemplateActivity && templateViewModel.isTokenSaved()) {
        }
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        adapter = TemplateAdapter(this, templateViewModel.templateList)
        recyclerView.adapter = adapter
        if (templateViewModel.isTokenSaved())
            token = templateViewModel.getToken()
        else
            token = ""
        edit_searchTemplate.addTextChangedListener { text: Editable? ->
            val content = text.toString()
            if (content.isNotEmpty()) {
                val map = mapOf("key" to content, "mode" to "all")
                templateViewModel.getTemplateList(token!!, map)
            } else {
                templateViewModel.getTemplateList(token!!, mapOf("key" to "", "mode" to "all"))
            }
        }
        templateViewModel.templateListLiveData.observe(viewLifecycleOwner, Observer { result ->
            val data = result.getOrNull()
            if (data != null) {
                templateViewModel.templateList.clear()
                templateViewModel.templateList.addAll(data.list)
                adapter.notifyDataSetChanged()
                recyclerView.visibility = View.VISIBLE
            } else {
                recyclerView.visibility = View.GONE
                result.exceptionOrNull()?.printStackTrace()
                Snackbar.make(
                    requireView(),
                    "模版列表获取失败|${result.exceptionOrNull()?.message}",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })
        currentTemplateViewModel.updateCurrentTemplateLiveData.observe(
            viewLifecycleOwner,
            Observer { result ->
                val data = result.getOrNull()
                if (data != null) {
                    println("设置返回时tid:${data.tid}")
                    templateViewModel.saveTemplateID(templateViewModel.getToken(), data.tid)
                    val intent = Intent()
                        .putExtra("tid", data.tid)
                    activity?.setResult(ActivityDao.RESULT_OK, intent)
                    activity?.finish()
                } else {
                    Toast.makeText(
                        context,
                        "模版切换失败|${result.exceptionOrNull()?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        initUI()
    }

    private fun initUI() {
        templateViewModel.getTemplateList(token!!, mapOf("key" to "", "mode" to "all"))
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TemplateFragment.
         */
        @JvmStatic
        fun newInstance(token: String) =
            TemplateFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, token)
                }
            }
    }
}