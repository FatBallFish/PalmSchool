package com.fatballfish.palmschool.ui.mine

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fatballfish.palmschool.R
import com.fatballfish.palmschool.logic.dao.ActivityDao
import com.fatballfish.palmschool.logic.model.mine.UserInfoItem
import com.fatballfish.palmschool.logic.model.mine.getInfoItem
import com.fatballfish.palmschool.logic.util.Base64Util
import com.fatballfish.palmschool.logic.util.TimeStampUtil
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import kotlinx.android.synthetic.main.activity_user_info_card.*


class UserInfoCardActivity : AppCompatActivity() {
    private val userInfoViewModel by lazy { ViewModelProvider(this)[UserInfoViewModel::class.java] }
    val userInfoUpdateViewModel by lazy { ViewModelProvider(this)[UserInfoUpdateViewModel::class.java] }
    val portraitUploadViewModel by lazy { ViewModelProvider(this)[PortraitUploadViewModel::class.java] }
    private lateinit var baseInfoadapter: UserInfoCardAdapter
    private lateinit var realInfoadapter: UserInfoCardAdapter
    val baseInfoList = ArrayList<UserInfoItem>()
    val realAuthInfoList = ArrayList<UserInfoItem>()
    val REQUEST_EXTERNAL_STORAGE = 1
    val PERMISSIONS_STORAGE = arrayOf(
        "android.permission.READ_EXTERNAL_STORAGE",
        "android.permission.WRITE_EXTERNAL_STORAGE",
        "android.permission.CAMERA"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info_card)
        // 设置顶部标题栏
        setSupportActionBar(toolbar_userInfoEdit)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setTitle("资料卡")
        }
        // 设置返回值
        setResult(ActivityDao.RESULT_CANCEL)
        // 列表框初始化
        recyclerView_base_info.layoutManager = LinearLayoutManager(this)
        baseInfoadapter = UserInfoCardAdapter(baseInfoList, this)
        recyclerView_base_info.adapter = baseInfoadapter
        recyclerView_real_info.layoutManager = LinearLayoutManager(this)
        realInfoadapter = UserInfoCardAdapter(realAuthInfoList, this)
        recyclerView_real_info.adapter = realInfoadapter
        // viewModel
        userInfoViewModel.userInfoLiveData.observe(this, Observer { result ->
            val data = result.getOrNull()
            if (data != null) {
                // 信息封装
                baseInfoList.clear()
                realAuthInfoList.clear()
                val genderMap = mapOf(
                    "male" to "男",
                    "female" to "女"
                )
                baseInfoList.apply {
                    add(getInfoItem("portrait", data.portrait, true))
                    add(getInfoItem("username", data.username, false))
                    add(getInfoItem("phone", data.phone, false))
                    add(getInfoItem("nickname", data.nickname, true))
                    add(getInfoItem("gender", genderMap[data.gender], true))
                    add(getInfoItem("email", data.email, true))
                    add(
                        getInfoItem(
                            "birthday",
                            TimeStampUtil.transTimeStampToDateString(data.birthday),
                            true
                        )
                    )
                }
                if (data.real_auth != null) {
                    realAuthInfoList.apply {
                        add(getInfoItem("id", data.real_auth.id.toString(), false))
                        add(getInfoItem("name", data.real_auth.name, false))
                        add(getInfoItem("school", data.real_auth.school, true))
                        add(getInfoItem("dept", data.real_auth.dept, true))
                        add(getInfoItem("major", data.real_auth.major, true))
                        add(getInfoItem("cls", data.real_auth.cls, true))
                    }
                } else {
                    realAuthInfoList.add(getInfoItem("noRealAuth", "点击进行实名认证", true))
                }
                baseInfoadapter.notifyDataSetChanged()
                realInfoadapter.notifyDataSetChanged()
            }
        })
        userInfoViewModel.getUserInfo(userInfoViewModel.getToken())
        userInfoUpdateViewModel.userInfoUpdateLiveData.observe(this, Observer { result ->
            val data = result.getOrNull()
            if (data != null) {
                setResult(ActivityDao.RESULT_OK)
                userInfoViewModel.getUserInfo(userInfoViewModel.getToken())
            }
        })
        portraitUploadViewModel.portraitUploadLiveData.observe(this, Observer { result ->
            val data = result.getOrNull()
            if (data != null) {
                setResult(ActivityDao.RESULT_OK)
                userInfoViewModel.getUserInfo(userInfoViewModel.getToken())
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            ActivityDao.REQUEST_INFO_EDIT -> {
                if (resultCode == ActivityDao.RESULT_OK) {
                    setResult(ActivityDao.RESULT_OK)
                    userInfoViewModel.getUserInfo(userInfoViewModel.getToken())
                } else {
                    Log.d("UserInfoCardActivity", "请求取消")
                }
            }
            ActivityDao.REQUEST_SELECT_PORTRAIT -> {
                Toast.makeText(this, "Result Code:$resultCode", Toast.LENGTH_SHORT).show()
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val pathList = data?.getStringArrayListExtra("result") ?: ArrayList<String>()
                    val builder = StringBuilder()
                    for (path in pathList) {
                        builder.append("$path\n")
                    }
                    Toast.makeText(this, builder.toString(), Toast.LENGTH_SHORT).show()
                }
            }
            PictureConfig.CHOOSE_REQUEST -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val selectList = PictureSelector.obtainMultipleResult(data)
                    val uri = selectList[0].compressPath
                    Log.d("UserInfoCard", uri.toString())
                    println(uri)
                    val base64String = Base64Util.imageToBase64(uri)
                    portraitUploadViewModel.uploadPortrait(base64String)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty()) {
                    var flag = true
                    for (result in grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            flag = false
                            break
                        }
                    }
                    if (flag) {
                        loadImageSelector()
                    } else {
                        Toast.makeText(this, "部分权限被拒绝", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun verifyStoragePermissions() {
        try {
            // 检查是否拥有写的权限
            var flag = true
            for (permission in PERMISSIONS_STORAGE) {
                val result = ActivityCompat.checkSelfPermission(
                    this,
                    permission
                )
                if (result != PackageManager.PERMISSION_GRANTED) {
                    flag = false
                    break
                }
            }
            if (!flag) {
                // 没有写的权限，就去申请权限
                ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
                )
            } else {
                loadImageSelector()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadImageSelector() {
        val imagePath =
            this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath
        PictureSelector.create(this)
            .openGallery(PictureConfig.TYPE_IMAGE)  // 开启展示列表
            .theme(R.style.picture_mine_style)
            .imageSpanCount(4)  // 每行显示个数
            .maxSelectNum(1)  // 最大选择数
            .selectionMode(PictureConfig.SINGLE)  // 多选或单选，PictureConfig.MULTIPLE || PictureConfig.SINGLE
            .isSingleDirectReturn(true)  //PictureConfig.SINGLE模式下是否直接返回
            .previewImage(true)  // 是否可预览图片 true or false
            .isCamera(true)  // 是否显示拍照按钮 true or false
            .imageFormat(PictureMimeType.JPEG)  // 拍照保存图片格式后缀,默认jpeg
            .isZoomAnim(true)  // 图片列表点击 缩放效果 默认true
            .enableCrop(true)  // 是否裁剪
            .cropWH(600, 600) // 裁剪宽高比，已废弃
            .withAspectRatio(600, 600)  // 裁剪比例
            .hideBottomControls(false)  // 隐藏底部uCrop工具栏
            .rotateEnabled(true)  // 裁剪是否可旋转图片
            .scaleEnabled(true)  // 裁剪是否可放大缩小图片
            .compress(true)  // 是否压缩
            .minimumCompressSize(1024)  // 小于多少kb的图片不压缩
            .compressSavePath(imagePath)  // 设置裁剪图片保存路径
            .freeStyleCropEnabled(false)  // 裁剪框是否可拖拽
            .showCropFrame(true)  // 是否显示裁剪框矩形网格 圆形裁剪时建议为false
            .forResult(PictureConfig.CHOOSE_REQUEST)  // 结果回调onActivityResult code
    }
}