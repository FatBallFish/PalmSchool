package com.fatballfish.palmschool.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.fatballfish.palmschool.R
import com.fatballfish.palmschool.logic.dao.ActivityDao

import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_sms_login.*
import kotlinx.android.synthetic.main.fragment_sms_login.edit_username

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PHONE = "phone"

/**
 * A simple [Fragment] subclass.
 * Use the [SmsLoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SmsLoginFragment : Fragment() {
    private var phone: String? = null
    private var rand: String? = null
    private var smsCode: String? = null
    val smsLoginViewModel by lazy { ViewModelProvider(this)[SmsLoginViewModel::class.java] }
    val smsCaptchaCreateViewModel by lazy { ViewModelProvider(this)[SmsCaptchaCreateViewModel::class.java] }
    val smsCaptchaValidateViewModel by lazy { ViewModelProvider(this)[SmsCaptchaValidateViewModel::class.java] }
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
        return inflater.inflate(R.layout.fragment_sms_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.setResult(ActivityDao.RESULT_CANCEL)
        // 登录事件viewModel
        smsLoginViewModel.smsLoginLiveData.observe(viewLifecycleOwner, Observer { result ->
            val data = result.getOrNull()
            if (data != null) {
                val token = data.token
                val loginType = data.loginType
                smsLoginViewModel.saveToken(token)
                val intent = Intent()
                val phone = edit_username.text.toString()
                intent
                    .putExtra("phone", phone)
                    .putExtra("token", token)
                    .putExtra("login_type", loginType)
                activity?.setResult(ActivityDao.RESULT_OK, intent)
                activity?.finish()
//                Snackbar.make(btn_smsLogin, "$loginType|$token", Snackbar.LENGTH_SHORT).show()
            } else {
                result.exceptionOrNull()?.printStackTrace()
                Snackbar.make(
                    btn_smsLogin,
                    "登录失败|${result.exceptionOrNull()?.message}",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })
        // 获取验证码事件viewModel
        smsCaptchaCreateViewModel.smsCaptchaCreateLiveData.observe(
            viewLifecycleOwner,
            Observer { result ->
                val data = result.getOrNull()
                if (data != null) {
                    rand = data.rand
                    Snackbar.make(btn_smsLogin, "验证码发送成功，3min有效", Snackbar.LENGTH_SHORT).show()
                    btn_getSmsCode.isEnabled = false
                    btn_getSmsCode.isClickable = false
                } else {
                    result.exceptionOrNull()?.printStackTrace()
                    Snackbar.make(
                        btn_getSmsCode,
                        "验证码获取失败|${result.exceptionOrNull()?.message}",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            })
        // 验证验证码ViewModel
        smsCaptchaValidateViewModel.smsCaptchaValidateLiveData.observe(
            viewLifecycleOwner,
            Observer { result ->
                val data = result.getOrNull()
                if (data != null) {
                    if (phone == null || phone == "") {
                        Snackbar.make(btn_smsLogin, "请输入手机号", Snackbar.LENGTH_SHORT).show()
                    } else {
                        smsLoginViewModel.smsLogin(phone!!, smsCaptchaValidateViewModel.hash)
                    }
                } else {
                    result.exceptionOrNull()?.printStackTrace()
                    Snackbar.make(btn_smsLogin, "验证码有误", Snackbar.LENGTH_SHORT).show()
                }
            })
        // 登录按钮事件监听
        btn_smsLogin.setOnClickListener {
            phone = edit_username.text.toString()
            // 先进行验证码判断，成功后再执行登录操作
            smsCode = edit_smsCode.text.toString()
            if (phone == "" || phone == null || smsCode == "" || smsCode == null) {
                Snackbar.make(
                    btn_smsLogin,
                    "手机号或验证码不可为空",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                if (rand == null) {
                    Snackbar.make(btn_smsLogin, "请先获取验证码", Snackbar.LENGTH_SHORT).show()
                } else {
                    smsCaptchaValidateViewModel.smsCaptchaValidate(smsCode!!, rand!!)
                }
            }
        }
        // 获取验证码按钮事件监听
        btn_getSmsCode.setOnClickListener {
            phone = edit_username.text.toString()
            if (phone == null) {
                Snackbar.make(btn_smsLogin, "请先输入手机号码", Snackbar.LENGTH_SHORT).show()
            } else {
                smsCaptchaCreateViewModel.smsCaptchaCreate(phone!!)
            }
        }
        // 登录方式切换
        text_loginByPass.setOnClickListener {
            val controller = Navigation.findNavController(it)
            val bundle = Bundle()
            phone = edit_username.text.toString()
            bundle.putString("phone", phone)
            controller.navigate(R.id.action_smsLoginFragment_to_passLoginFragment2, bundle)

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
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SmsLoginFragment.
         */
        @JvmStatic
        fun newInstance(phone: String) =
            SmsLoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PHONE, phone)
                }
            }
    }
}