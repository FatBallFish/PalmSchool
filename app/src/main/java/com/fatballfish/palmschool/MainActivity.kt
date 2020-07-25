package com.fatballfish.palmschool

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.fatballfish.palmschool.ui.login.LoginActivity
import com.google.android.material.snackbar.Snackbar
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 设置顶部标题栏
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }
        // 设置侧滑容器
        // 手动设置header_layout,为了定义头布局的响应事件
        val nav_headerView = navView.inflateHeaderView(R.layout.nav_header)
        // 获得头像组件
        val image_portrait = nav_headerView.findViewById<CircleImageView>(R.id.img_portrait)
        image_portrait.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        navView.setCheckedItem(R.id.navLessons) // 设置默认选中菜单
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
                    Toast.makeText(this, "点击了我的页面", Toast.LENGTH_SHORT).show()
                }
            }
            view_drawerLayout.closeDrawers()
            true
        }

        // 设置浮动按钮
        fab.setOnClickListener {
            Toast.makeText(this, "浮动按钮被单击", Toast.LENGTH_SHORT).show()
            Snackbar.make(it, "浮动按钮被单击", Snackbar.LENGTH_SHORT).show()
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