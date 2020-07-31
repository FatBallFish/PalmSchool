package com.fatballfish.palmschool.ui.template

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.fatballfish.palmschool.R
import com.fatballfish.palmschool.logic.dao.ActivityDao
import com.fatballfish.palmschool.logic.model.Template.Template
import com.fatballfish.palmschool.logic.util.TimeStampUtil

class TemplateAdapter(
    private val fragment: TemplateFragment,
    private val templateList: List<Template>
) : RecyclerView.Adapter<TemplateAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val text_template_name: TextView = view.findViewById(R.id.text_template_name)
        val text_content: TextView = view.findViewById(R.id.text_content)
        val text_count: TextView = view.findViewById(R.id.text_count)
        val text_submiter: TextView = view.findViewById(R.id.text_submiter)
        val text_submit_time: TextView = view.findViewById(R.id.text_submit_time)
        val text_public: TextView = view.findViewById(R.id.text_public)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.temlate_item, parent, false)
        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            val template = templateList[position]
            Toast.makeText(parent.context, "${template.name} be clicked", Toast.LENGTH_SHORT).show()
            // activity 判断
            val activity = fragment.activity
            if (activity is TemplateActivity) {
                println("设置返回时tid:${template.tid}")
                val intent = Intent()
                    .putExtra("tid", template.tid)
                activity.setResult(ActivityDao.RESULT_OK, intent)
                activity.finish()
            }


        }
        return holder
    }

    override fun getItemCount() = templateList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val template = templateList[position]
        holder.text_template_name.text = template.name
        holder.text_content.text = template.content
        holder.text_count.text = "应用次数：${template.used}"
        holder.text_submiter.text = "上传者：${template.submiter}"
        holder.text_submit_time.text = "上传时间：${TimeStampUtil.transToString(template.addTime)}"
        if (template.public) {
            holder.text_public.text = "公开"
            holder.text_public.setTextColor(Color.parseColor("#00ff00")) // 绿色
        } else {
            holder.text_public.text = "私有"
            holder.text_public.setTextColor(Color.parseColor("#ff0000")) // 红色
        }
    }

}