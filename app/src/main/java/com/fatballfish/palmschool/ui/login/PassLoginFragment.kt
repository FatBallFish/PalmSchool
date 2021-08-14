package com.fatballfish.palmschool.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.fatballfish.palmschool.R
import com.fatballfish.palmschool.logic.dao.ActivityDao
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_pass_login.*
import kotlinx.android.synthetic.main.fragment_pass_login.edit_username

private const val ARG_PHONE = "phone"

/**
 * A simple [Fragment] subclass.
 * Use the [PassLoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class PassLoginFragment : Fragment() {
    private var phone: String? = null
    val viewModel by lazy { ViewModelProvider(this)[PassLoginViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            phone = it.getString(ARG_PHONE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pass_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.setResult(ActivityDao.RESULT_CANCEL)
        viewModel.passLoginLiveData.observe(viewLifecycleOwner, Observer { result ->
            val data = result.getOrNull()
            if (data != null) {
                val phone = edit_username.text.toString()
                val token = data.token
                val loginType = data.loginType
                viewModel.saveToken(token)
                val intent = Intent()
                intent
                    .putExtra("phone", phone)
                    .putExtra("token", token)
                    .putExtra("login_type", loginType)
                activity?.setResult(ActivityDao.RESULT_OK, intent)
                activity?.finish()
//                Snackbar.make(btn_passLogin, "$loginType|$token", Snackbar.LENGTH_SHORT).show()
            } else {
                result.exceptionOrNull()?.printStackTrace()
                Snackbar.make(
                    btn_passLogin,
                    "登录失败|${result.exceptionOrNull()?.message}",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })
        // 登录按钮事件监听
        btn_passLogin.setOnClickListener {
            val username = edit_username.text.toString()
            val password = edit_password.text.toString()
            if (username == "" || password == "") {
                Snackbar.make(
                    btn_passLogin,
                    "用户名密码不可为空",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                viewModel.passLogin(username, password)
            }
        }
        // 切换短信登录事件监听
        text_loginBySms.setOnClickListener {
            val controller = Navigation.findNavController(it)
            val bundle = Bundle()
            phone = edit_username.text.toString()
            bundle.putString("phone", phone)
            controller.navigate(R.id.action_passLoginFragment_to_smsLoginFragment2, bundle)

        }
        initUI()
    }

    private fun initUI() {
        if (phone != null) {
            edit_username.text = SpannableStringBuilder(phone)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param phone Parameter 1.
         * @return A new instance of fragment PassLoginFragment.
         */
        @JvmStatic
        fun newInstance(phone: String) =
            PassLoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PHONE, phone)
                }
            }
    }
}