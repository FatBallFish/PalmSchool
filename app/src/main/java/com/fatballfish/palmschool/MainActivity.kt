package com.fatballfish.palmschool

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.fatballfish.palmschool.logic.dao.ActivityDao
import com.fatballfish.palmschool.logic.model.PalmSchoolDataBaseHelper
import com.fatballfish.palmschool.logic.model.index.BoomButtonInfo
import com.fatballfish.palmschool.ui.lesson.LessonFragment
import com.fatballfish.palmschool.ui.login.LoginActivity
import com.fatballfish.palmschool.ui.mine.UserInfoActivity
import com.fatballfish.palmschool.ui.mine.UserInfoViewModel
import com.fatballfish.palmschool.ui.template.TemplateActivity
import com.google.android.material.snackbar.Snackbar
import com.nightonke.boommenu.BoomButtons.*
import com.nightonke.boommenu.ButtonEnum
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header.*
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {
    val userInfoViewModel by lazy { ViewModelProvider(this)[UserInfoViewModel::class.java] }
    val boomButtonInfoList = ArrayList<BoomButtonInfo>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 设置顶部标题栏
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }
        // 初始化数据库
        // viewModel
        userInfoViewModel.userInfoLiveData.observe(this, Observer { result ->
            val data = result.getOrNull()
            if (data != null) {
                // 数据库数据更新
                insertTokens(userInfoViewModel.getToken(), data.username)
                // 设置名称显示
                val nameBuilder = StringBuilder()
                if (data.nickname == "" || data.nickname == null) {
                    nameBuilder.append(data.username)
                } else {
                    nameBuilder.append("${data.nickname}(${data.username})")
                }
                // 设置学校显示
                text_navName.text = nameBuilder.toString()
                val schoolBuilder = StringBuilder()
                if (data.real_auth == null) {
                    schoolBuilder.append("未实名认证")
                } else {
                    if (data.real_auth.school == "") {
                        schoolBuilder.append("未设置学校")
                    } else {
                        schoolBuilder.append(data.real_auth.school)
                    }
                }
                text_navSchool.text = schoolBuilder.toString()
                // 设置头像显示
                if (data.portrait != "") {
                    Glide.with(this).load(data.portrait).into(img_portrait)
                }
            } else {
                result.exceptionOrNull()?.printStackTrace()
                Snackbar.make(
                    fab,
                    "获取用户信息失败|${result.exceptionOrNull()?.message}",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })
        // 设置侧滑容器
        // 手动设置header_layout,为了定义头布局的响应事件
        val nav_headerView = navView.inflateHeaderView(R.layout.nav_header)
        // 获得头像组件
        val image_portrait = nav_headerView.findViewById<CircleImageView>(R.id.img_portrait)
        image_portrait.setOnClickListener {
            if (userInfoViewModel.isTokenSaved()) {
                val intent = Intent(this, UserInfoActivity::class.java)
                startActivityForResult(intent, ActivityDao.REQUEST_USER_INFO)
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivityForResult(intent, ActivityDao.REQUEST_LOGIN)
            }
        }
        // 侧边菜单设置默认选中菜单
        navView.setCheckedItem(R.id.navLessons)
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navLessons -> {
                    Toast.makeText(this, "点击了课程页面", Toast.LENGTH_SHORT).show()
                }
                R.id.navTasks -> {
                    Toast.makeText(this, "点击了任务页面", Toast.LENGTH_SHORT).show()
                }
                R.id.navcommunication -> {
                    Toast.makeText(this, "点击了发现页面", Toast.LENGTH_SHORT).show()
                }
                R.id.navMine -> {
                    val intent = Intent(this, UserInfoActivity::class.java)
                    startActivityForResult(intent, ActivityDao.REQUEST_USER_INFO)
                }
            }
            view_drawerLayout.closeDrawers()
            true
        }

//        // 设置浮动按钮
//        fab.setOnClickListener {
//            Toast.makeText(this, "浮动按钮被单击", Toast.LENGTH_SHORT).show()
//            Snackbar.make(it, "浮动按钮被单击", Snackbar.LENGTH_SHORT).show()
//        }
        // 设置bmb按钮
        boomButtonInfoList.add(BoomButtonInfo("添加课程", R.drawable.ic_plus))
        boomButtonInfoList.add(BoomButtonInfo("替换模版", R.drawable.ic_template))
        Log.d("MainActivity", "bmb num:${bmb.piecePlaceEnum.pieceNumber()}")
        for (i in 0 until bmb.piecePlaceEnum.pieceNumber()) {
            Log.d("MainActivity", "bmb placeEnum index:${i}")
            val boomButtonInfo = boomButtonInfoList[i]
            val builder = TextOutsideCircleButton.Builder()
                .listener(OnBMClickListener { index ->
                    when (boomButtonInfoList[index].text) {
                        "替换模版" -> {
                            val intent = Intent(this, TemplateActivity::class.java)
                            startActivityForResult(intent, ActivityDao.REQUEST_TEMPLATE_SEARCH)
                        }
                    }
                    Toast.makeText(
                        this,
                        "Clicked ${boomButtonInfoList[index].text}",
                        Toast.LENGTH_SHORT
                    ).show()
                })
                .normalImageRes(boomButtonInfo.image)
                .normalText(boomButtonInfo.text)
                .textSize(16)
            bmb.addBuilder(builder)
        }
        initUI()
    }

    private fun initUI() {
        if (userInfoViewModel.isTokenSaved()) {
            val token = userInfoViewModel.getToken()
            userInfoViewModel.getUserInfo(token)
        }
    }

    private fun insertTokens(token: String, username: String) {
        val db = PalmSchoolApplication.db
        val cursor = db.rawQuery("SELECT * FROM tokens WHERE token = ?", arrayOf(token))
        Log.d("Template", "insert count:${cursor.count}")
        if (cursor.count == 0) {
            val tokens = ContentValues().apply {
                put("token", token)
                put("username", username)
            }
            val ret_id = db.insert(PalmSchoolApplication.TABLE_TOKEN, null, tokens)
            Toast.makeText(this, "insert ret_id:$ret_id", Toast.LENGTH_SHORT).show()
            Log.d("Template", "insert ret_id:$ret_id")
        }
        cursor.close()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Toast.makeText(this, "MainActivity 结果返回", Toast.LENGTH_SHORT).show()
        when (requestCode) {
            ActivityDao.REQUEST_LOGIN -> {
                if (resultCode == ActivityDao.RESULT_OK) {
                    val username = data?.getStringExtra("phone") ?: ""
                    val token = data?.getStringExtra("token") ?: ""
                    val loginType = data?.getStringExtra("login_type") ?: ""
                    // 写入数据库
                    insertTokens(token, username)
                    Toast.makeText(this, "$token|$loginType", Toast.LENGTH_SHORT).show()
                    userInfoViewModel.saveToken(token)
                    userInfoViewModel.getUserInfo(token)
                    val tid = intent.getIntExtra("tid", -1)

                    val intent = Intent(ActivityDao.ACTION_REFRESH_TEMPLATE).putExtra("tid", tid)
                    sendBroadcast(intent)
//                    // 重载 课程页面，测试不可行
//                    layout_HomeFragment.removeAllViews()
//                    val lessonFragment = LayoutInflater.from(this)
//                        .inflate(R.layout.fragment_lessons, layout_HomeFragment, false)
//                    layout_HomeFragment.addView(lessonFragment)
                } else if (resultCode == ActivityDao.RESULT_CANCEL) {
                    Toast.makeText(this, "取消登录", Toast.LENGTH_SHORT).show()
                }
            }
            ActivityDao.REQUEST_USER_INFO -> {
                if (resultCode == ActivityDao.RESULT_OK) {
                    if (userInfoViewModel.isTokenSaved()) {
                        userInfoViewModel.getUserInfo(userInfoViewModel.getToken())
                    } else {
                        Glide.with(this).load(R.drawable.ic_default).into(img_portrait)
                        text_navName.text = resources.getText(R.string.nav_nameHint)
                        text_navSchool.text = resources.getText(R.string.nav_mottoHint)
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivityForResult(intent, ActivityDao.REQUEST_LOGIN)
                    }
                }
            }
            ActivityDao.REQUEST_TEMPLATE_SEARCH -> {
                if (resultCode == ActivityDao.RESULT_OK) {
                    val tid = data?.getIntExtra("tid", -1) ?: -1
                    Log.d("Template", "获得返回时tid:${tid}")
                    val intent = Intent(ActivityDao.ACTION_REFRESH_TEMPLATE).putExtra("tid", tid)
                    sendBroadcast(intent)
                    Toast.makeText(this, "发起通知", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // 初始化菜单
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 监听顶部菜单点击事件
        when (item.itemId) {
            R.id.toolbar_addLesson -> {
                Toast.makeText(this, "点击了添加课程", Toast.LENGTH_SHORT).show()
            }
            R.id.toolbar_editLessons -> {
                Toast.makeText(this, "点击了编辑课程", Toast.LENGTH_SHORT).show()
            }
            R.id.toolbar_deleteLessons -> {
                Toast.makeText(this, "点击了删除课程", Toast.LENGTH_SHORT).show()
            }
            android.R.id.home -> {
                view_drawerLayout.openDrawer(GravityCompat.START, true)
            }
        }
        return true
    }
}