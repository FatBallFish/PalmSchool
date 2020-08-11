package com.fatballfish.palmschool.ui.mine

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.fatballfish.palmschool.R
import com.fatballfish.palmschool.logic.model.mine.UserInfoItem
import com.google.android.material.snackbar.Snackbar

class UserInfoAdapter(private val userInfoList: List<UserInfoItem>) :
    RecyclerView.Adapter<UserInfoAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val text_info: TextView = view.findViewById(R.id.text_info)
        val image_info: ImageView = view.findViewById(R.id.image_info)
        val image_arrow: ImageView = view.findViewById(R.id.image_arrow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.user_info_item, parent, false)
        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            val userInfo = userInfoList[position]
            if (userInfo.clickable) {
                when(userInfo.key){
                    "phone"->{}
                    "birthday"->{}
                    "real_auth"->{}
                    "id"->{}
                    "name"->{}
                    "dept"->{}
                    "major"->{}
                    "cls"->{}
                }
                val intent = Intent()
//                Snackbar.make(view, "can be clicked", Snackbar.LENGTH_SHORT).show()
            } else {
//                Snackbar.make(view, "can't be clicked", Snackbar.LENGTH_SHORT).show()
            }
            // 单击事件处理
        }
        return holder
    }

    override fun getItemCount() = userInfoList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userInfo = userInfoList[position]
        println("userInfo:" + userInfo.toString())
        holder.image_info.setImageResource(userInfo.icon_id)
        holder.text_info.text = userInfo.value
        if (userInfo.clickable) {
            holder.image_arrow.visibility = View.VISIBLE
        }
    }
}