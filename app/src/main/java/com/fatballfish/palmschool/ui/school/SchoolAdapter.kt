package com.fatballfish.palmschool.ui.school

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fatballfish.palmschool.R
import com.fatballfish.palmschool.logic.model.realAuth.RealAuthUpdateRequest
import com.fatballfish.palmschool.logic.model.school.School

class SchoolAdapter(val schoolList: ArrayList<School>, val fragment: SchoolChoosenFragment) :
    RecyclerView.Adapter<SchoolAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val text_school_name: TextView = view.findViewById(R.id.text_school_name)
        val text_school_location: TextView = view.findViewById(R.id.text_school_location)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.school_item, parent, false)
        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            val school = schoolList[position]
            val realAuthUpdateRequest = RealAuthUpdateRequest(school = school.id)
            fragment.realAuthUpdateViewModel.updateRealAuth(realAuthUpdateRequest)
        }
        return holder
    }

    override fun getItemCount() = schoolList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val school = schoolList[position]
        holder.text_school_name.text = school.name
        holder.text_school_location.text = school.location
    }
}