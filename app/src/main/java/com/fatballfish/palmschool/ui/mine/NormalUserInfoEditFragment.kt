package com.fatballfish.palmschool.ui.mine

import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.text.SpannableStringBuilder
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fatballfish.palmschool.R
import com.fatballfish.palmschool.logic.dao.ActivityDao
import com.fatballfish.palmschool.logic.model.realAuth.RealAuthUpdateRequest
import com.fatballfish.palmschool.logic.model.user.UserInfoUpdateRequest
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_normal_user_info_edit.*
import java.util.regex.Pattern

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "key"
private const val ARG_PARAM2 = "value"

/**
 * A simple [Fragment] subclass.
 * Use the [NormalUserInfoEditFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class NormalUserInfoEditFragment : Fragment() {
    private var key: String? = null
    private var value: String? = null
    private val keyMap = mapOf(
        "nickname" to "昵称",
        "email" to "邮箱",
        "dept" to "学院",
        "major" to "专业",
        "cls" to "班级",
        "" to "未知字段",
        "null" to "未知字段"
    )
    private val lengthMap = mapOf(
        "nickname" to 20,
        "email" to 100,
        "dept" to 100,
        "major" to 50,
        "cls" to 20
    )
    private val inputTypeMap = mapOf(
        "nickname" to InputType.TYPE_CLASS_TEXT,
        "email" to InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS,
        "dept" to InputType.TYPE_CLASS_TEXT,
        "major" to InputType.TYPE_CLASS_TEXT,
        "cls" to InputType.TYPE_CLASS_TEXT
    )
    val userInfoUpdateViewModel by lazy { ViewModelProvider(this)[UserInfoUpdateViewModel::class.java] }
    val realAuthUpdateViewModel by lazy { ViewModelProvider(this)[RealAuthUpdateViewModel::class.java] }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            key = it.getString(ARG_PARAM1)
            value = it.getString(ARG_PARAM2)
        }
        activity?.setResult(ActivityDao.RESULT_CANCEL)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.normal_user_info_edit_menu, menu)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_normal_user_info_edit, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // 设置toolbar和菜单
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(toolbar_userInfoEdit)
        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setTitle("修改${keyMap[key]}")
        }
        // 设置界面
        text_new_field.text = "新${keyMap[key]}"
        edit_infoEdit.apply {
            text = SpannableStringBuilder(value)
            filters = arrayOf(InputFilter.LengthFilter(lengthMap[key] ?: 100))
            inputType = inputTypeMap[key] ?: InputType.TYPE_CLASS_TEXT
        }
        // 用户信息更新ViewModel
        userInfoUpdateViewModel.userInfoUpdateLiveData.observe(
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
                        edit_infoEdit,
                        "更新用户信息失败|${result.exceptionOrNull()?.message}",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            })
        // 实名信息更新ViewModel
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
                        edit_infoEdit,
                        "更新用户信息失败|${result.exceptionOrNull()?.message}",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                activity?.finish()
            }
            R.id.menu_save -> {
                when (key) {
                    "nickname" -> {
                        val userInfoUpdateRequest =
                            UserInfoUpdateRequest(nickname = edit_infoEdit.text.toString())
                        userInfoUpdateViewModel.updateUserInfo(userInfoUpdateRequest)
                    }
                    "email" -> {
                        val email = edit_infoEdit.text.toString()
                        val regex =
                            Pattern.compile("[\\w!#\$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#\$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?")
                        val matcher = regex.matcher(email)
                        if (matcher.matches()) {
                            val userInfoUpdateRequest =
                                UserInfoUpdateRequest(email = edit_infoEdit.text.toString())
                            userInfoUpdateViewModel.updateUserInfo(userInfoUpdateRequest)
                        } else {
                            Toast.makeText(context, "请输入正确的邮箱地址", Toast.LENGTH_SHORT).show()
                        }
                    }
                    "dept" -> {
                        val realAuthUpdateRequest =
                            RealAuthUpdateRequest(dept = edit_infoEdit.text.toString())
                        realAuthUpdateViewModel.updateRealAuth(realAuthUpdateRequest)
                    }
                    "major" -> {
                        val realAuthUpdateRequest =
                            RealAuthUpdateRequest(major = edit_infoEdit.text.toString())
                        realAuthUpdateViewModel.updateRealAuth(realAuthUpdateRequest)
                    }
                    "cls" -> {
                        val realAuthUpdateRequest =
                            RealAuthUpdateRequest(cls = edit_infoEdit.text.toString())
                        realAuthUpdateViewModel.updateRealAuth(realAuthUpdateRequest)
                    }
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
         * @return A new instance of fragment NormalUserInfoEditFragment.
         */
        @JvmStatic
        fun newInstance(key: String, value: String) =
            NormalUserInfoEditFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, key)
                    putString(ARG_PARAM2, value)
                }
            }
    }
}