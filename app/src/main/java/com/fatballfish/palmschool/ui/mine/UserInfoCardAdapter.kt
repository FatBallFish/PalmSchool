package com.fatballfish.palmschool.ui.mine

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fatballfish.palmschool.R
import com.fatballfish.palmschool.logic.dao.ActivityDao
import com.fatballfish.palmschool.logic.model.mine.UserInfoItem
import com.fatballfish.palmschool.logic.model.user.UserInfoUpdateRequest
import com.fatballfish.palmschool.logic.util.TimeStampUtil
import com.google.android.material.snackbar.Snackbar
import com.yuyh.library.imgsel.ISNav
import com.yuyh.library.imgsel.config.ISListConfig
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.Exception
import java.util.*

class UserInfoCardAdapter(
    private val userInfoList: List<UserInfoItem>,
    private val activity: UserInfoCardActivity
) :
    RecyclerView.Adapter<UserInfoCardAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val text_key: TextView = view.findViewById(R.id.text_key)
        val text_value: TextView = view.findViewById(R.id.text_value)
        val image_arrow: ImageView = view.findViewById(R.id.image_arrow)
        val image_portrait: CircleImageView = view.findViewById(R.id.image_portrait)
    }

    private val keyMap = mapOf(
        "portrait" to "头像",
        "username" to "用户名",
        "phone" to "手机",
        "nickname" to "昵称",
        "gender" to "性别",
        "email" to "邮箱",
        "birthday" to "生日",
        "noRealAuth" to "尚未实名",
        "id" to "学号",
        "name" to "用户名",
        "school" to "学校",
        "dept" to "学院",
        "major" to "专业",
        "cls" to "班级"
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.user_info_card_item, parent, false)
        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener {
            // 单击事件处理
            val position = holder.adapterPosition
            val userInfo = userInfoList[position]
            if (userInfo.clickable) {
                when (userInfo.key) {
                    "portrait" -> {
                        activity.verifyStoragePermissions()
                    }
                    "nickname", "dept", "major", "cls", "email", "school", "noRealAuth" -> {
                        val intent = Intent(parent.context, UserInfoEditActivity::class.java)
                        intent.apply {
                            putExtra("key", userInfo.key)
                            putExtra("value", userInfo.value)
                            activity.startActivityForResult(intent, ActivityDao.REQUEST_INFO_EDIT)
                        }
                    }
                    "gender" -> {
                        val genderMap = mapOf(
                            "男" to "male",
                            "女" to "female"
                        )
                        val genderList = arrayOf("男", "女")
                        val alertBuilder = AlertDialog.Builder(activity)
                        alertBuilder.setTitle("选择性别")
                        alertBuilder.setItems(
                            genderList,
                            DialogInterface.OnClickListener { dialog, which ->
                                val userInfoUpdateRequest =
                                    UserInfoUpdateRequest(gender = genderMap[genderList[which]])
                                activity.userInfoUpdateViewModel.updateUserInfo(
                                    userInfoUpdateRequest
                                )
                            })
                        alertBuilder.show()
                    }
                    "birthday" -> {
                        val calendar = Calendar.getInstance()
                        val listener =
                            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                                val timestamp =
                                    TimeStampUtil.transDateToTimeStamp("$year.${month + 1}.$dayOfMonth")

                                val userInfoUpdateRequest =
                                    UserInfoUpdateRequest(birthday = timestamp)
                                activity.userInfoUpdateViewModel.updateUserInfo(
                                    userInfoUpdateRequest
                                )
                            }
                        DatePickerDialog(
                            activity,
                            listener,
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    }
                    else -> {
                    }
                }
//                Snackbar.make(view, "can be clicked", Snackbar.LENGTH_SHORT).show()
            } else {
//                Snackbar.make(view, "can't be clicked", Snackbar.LENGTH_SHORT).show()
            }
        }
        return holder
    }

    override fun getItemCount() = userInfoList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userInfo = userInfoList[position]
        holder.text_key.text = keyMap[userInfo.key] ?: "未知"
        when (userInfo.key) {
            "portrait" -> {
                holder.image_portrait.visibility = View.VISIBLE
                holder.text_value.visibility = View.INVISIBLE
                if (userInfo.value.isNullOrEmpty()) {
                    Glide.with(holder.itemView).load(R.drawable.ic_default)
                        .into(holder.image_portrait)
                } else {
                    Glide.with(holder.itemView).load(userInfo.value).into(holder.image_portrait)
                }
            }
            else -> {
                holder.text_value.text = userInfo.value
            }
        }
        if (userInfo.clickable) {
            holder.image_arrow.visibility = View.VISIBLE
        } else {
            holder.image_arrow.visibility = View.INVISIBLE
        }
    }
}