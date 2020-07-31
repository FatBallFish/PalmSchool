package com.fatballfish.palmschool.ui.mine

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.fatballfish.palmschool.R
import com.fatballfish.palmschool.logic.dao.ActivityDao
import com.fatballfish.palmschool.logic.model.mine.UserInfoItem
import com.fatballfish.palmschool.logic.model.mine.getInfoItem
import com.fatballfish.palmschool.logic.util.TimeStampUtil
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_user_info.*
import kotlinx.android.synthetic.main.bottom_userinfo_setting.*


class UserInfoActivity : AppCompatActivity() {
    private lateinit var adapter: UserInfoAdapter
    val userInfoViewModel by lazy { ViewModelProvider(this)[UserInfoViewModel::class.java] }
    val infoList = ArrayList<UserInfoItem>()
    private lateinit var behavior: BottomSheetBehavior<View>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        // 设置顶部标题栏
        setSupportActionBar(toolbar_userInfo)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setTitle("")
        }
        // 设置返回值
        setResult(ActivityDao.RESULT_CANCEL)
        // 列表框初始化
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = UserInfoAdapter(infoList)
        recyclerView.adapter = adapter
        // viewModel
        userInfoViewModel.userInfoLiveData.observe(this, Observer { result ->
            val data = result.getOrNull()
            if (data != null) {
                // 顶部初始化
                // 背景加载
                Glide.with(this).load(data.portrait).into(image_infoBackGround)
                // 头像加载
                Glide.with(this).load(data.portrait).into(image_portrait)
                // 用户名,昵称,性别，学校显示
                text_username.text = data.username
                text_nickname.text = data.nickname
                val genderMap = mapOf<String, String>(
                    "male" to "男",
                    "female" to "女"
                )
                text_gender.text = genderMap[data.gender] ?: "未知"
                if (data.real_auth == null) {
                    text_school.text = "尚未填写学校信息"
                } else {
                    text_school.text = data.real_auth.school ?: "尚未填写学校信息"
                }
                // 封装信息
                infoList.clear()
                adapter.notifyDataSetChanged()
                infoList.apply {
//                    add(getInfoItem("email", data.email, true))
                    add(getInfoItem("phone", data.phone, true))
                    add(getInfoItem("birthday", TimeStampUtil.transToString(data.birthday), true))
                    var realAuthState: String = "未实名"
                    if (data.real_auth != null) {
                        realAuthState = "已实名"
                        add(getInfoItem("real_auth", realAuthState, true))
                        add(getInfoItem("id", data.real_auth.id.toString(), false))
                        add(getInfoItem("name", data.real_auth.name, false))
                        add(getInfoItem("dept", data.real_auth.dept, false))
                        add(getInfoItem("major", data.real_auth.major, false))
                        add(getInfoItem("cls", data.real_auth.cls, false))
                    } else {
                        add(getInfoItem("real_auth", realAuthState, true))
                    }
                }
                adapter.notifyDataSetChanged()
                setResult(ActivityDao.RESULT_OK)
            } else {
                result.exceptionOrNull()?.printStackTrace()
                Snackbar.make(
                    recyclerView,
                    "获取用户信息失败|${result.exceptionOrNull()?.message}",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })
        // 滚动组件事件监听，用于实现标题栏
        appbar_userInfo.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                val totalScrollRange = appBarLayout?.totalScrollRange ?: 0
                val dy = verticalOffset + totalScrollRange
                if (dy < 140) {
                    toolbarText_userInfo.setTextColor(Color.WHITE)
                } else if (dy > 140) {
                    toolbarText_userInfo.setTextColor(Color.TRANSPARENT)
                }
            }
        })
        // 底部菜单背景初始化
        bottom_background.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        // 底部菜单初始化
        behavior = BottomSheetBehavior.from(bottom_setting)
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    bottom_background.visibility = ViewGroup.GONE
                } else {
                    if (bottom_background.visibility == ViewGroup.GONE)
                        bottom_background.visibility = ViewGroup.VISIBLE
                }
            }
        })
        // 按钮事件监听
        btn_logout.setOnClickListener {
            userInfoViewModel.removeToken()
            finish()
        }
        // 界面刷新显示
        initUI()
    }

    private fun initUI() {
        if (userInfoViewModel.isTokenSaved()) {
            val token = userInfoViewModel.getToken()
            userInfoViewModel.getUserInfo(token)
        } else {
            Toast.makeText(this, "请先登录！", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.user_info_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 监听顶部菜单点击事件
        when (item.itemId) {
            R.id.menu_settings -> {
                if (behavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                    behavior.state = BottomSheetBehavior.STATE_HIDDEN

                } else {
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
                }

            }

            android.R.id.home -> {
                finish()
            }
        }
        return true
    }
}