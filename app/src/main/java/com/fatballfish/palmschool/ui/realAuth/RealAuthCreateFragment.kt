package com.fatballfish.palmschool.ui.realAuth

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fatballfish.palmschool.R
import com.fatballfish.palmschool.logic.dao.ActivityDao
import com.fatballfish.palmschool.logic.model.realAuth.RealAuthCreateRequest
import com.fatballfish.palmschool.logic.model.user.UserInfoUpdateRequest
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_normal_user_info_edit.*
import kotlinx.android.synthetic.main.fragment_real_auth_create.*
import kotlinx.android.synthetic.main.fragment_real_auth_create.toolbar_userInfoEdit
import kotlinx.android.synthetic.main.fragment_school_choosen.*

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "key"
private const val ARG_PARAM2 = "value"

/**
 * A simple [Fragment] subclass.
 * Use the [RealAuthCreateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RealAuthCreateFragment : Fragment() {
    private var key: String? = null
    private var value: String? = null
    val realAuthCreateViewModel by lazy { ViewModelProvider(this)[RealAuthCreateViewModel::class.java] }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            key = it.getString(ARG_PARAM1)
            value = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_real_auth_create, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // 设置toolbar和菜单
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(toolbar_userInfoEdit)
        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setTitle("实名认证")
        }
        realAuthCreateViewModel.realAuthCreateRequest.observe(
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
                        edit_id, "创建实名认证失败|${result.exceptionOrNull()?.message}",
                        Snackbar.LENGTH_SHORT
                    ).show()

                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.normal_user_info_edit_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                activity?.finish()
            }
            R.id.menu_save -> {
                if (edit_id.text.toString().isNullOrEmpty() || edit_name.text.toString()
                        .isNullOrEmpty()
                ) {
                    Toast.makeText(context, "学号与姓名不可为空", Toast.LENGTH_SHORT).show()
                } else {
                    val realAuthCreateRequest =
                        RealAuthCreateRequest(edit_id.text.toString(), edit_name.text.toString())
                    realAuthCreateViewModel.createRealAuth(realAuthCreateRequest)
                }
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
         * @return A new instance of fragment RealAuthCreateFragment.
         */
        @JvmStatic
        fun newInstance(key: String, value: String) =
            RealAuthCreateFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, key)
                    putString(ARG_PARAM2, value)
                }
            }
    }
}