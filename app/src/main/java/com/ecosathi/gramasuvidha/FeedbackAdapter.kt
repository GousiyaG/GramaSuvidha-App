package com.ecosathi.gramasuvidha

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FeedbackAdapter(private var list: List<Feedback>) :
    RecyclerView.Adapter<FeedbackAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val projectNo: TextView = view.findViewById(R.id.projectNo)
        val name: TextView = view.findViewById(R.id.name)
        val message: TextView = view.findViewById(R.id.message)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.feedback_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val f = list[position]

        holder.projectNo.text = "Project: ${f.projectNo}"
        holder.name.text = "By: ${f.name}"
        holder.message.text = f.message
    }

    fun updateList(newList: List<Feedback>) {
        list = newList
        notifyDataSetChanged()
    }
}