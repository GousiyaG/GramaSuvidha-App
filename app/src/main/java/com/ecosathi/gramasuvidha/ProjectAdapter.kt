package com.ecosathi.gramasuvidha

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProjectAdapter(private var list: List<Project>) :
    RecyclerView.Adapter<ProjectAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val address: TextView = view.findViewById(R.id.address)
        val date: TextView = view.findViewById(R.id.date)
        val status: TextView = view.findViewById(R.id.status)
        val completed: TextView = view.findViewById(R.id.completed)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.project_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = list[position]

        holder.title.text = "Project #${position + 1}: ${p.name}"
        holder.address.text = "📍 ${p.address}"
        holder.date.text = "📅 Start: ${p.startDate}"
        holder.status.text = "Status: ${p.status}"
        holder.completed.text = "Completed: ${p.completedDate}"

        when (p.status.lowercase()) {
            "completed" -> holder.status.setTextColor(Color.parseColor("#2E7D32"))
            "in progress" -> holder.status.setTextColor(Color.parseColor("#F57C00"))
            else -> holder.status.setTextColor(Color.RED)
        }
    }

    fun updateList(newList: List<Project>) {
        list = newList
        notifyDataSetChanged()
    }
}
