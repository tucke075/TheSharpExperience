package com.example.thesharpexperience.RecyclerView

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.thesharpexperience.R
import com.example.thesharpexperience.person_data.days_person

class DaysAdapter (private val dataList: List<days_person>) : RecyclerView.Adapter<DaysAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaysAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.days_item_design, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val person = dataList[position]
        Log.d("Here", "Here")
        if(person.title == 1) {
            holder.title.text = "Registered Nurse"
            holder.hiredate.text = person.hireDate
            holder.name.text = person.name
        }
        //else
            //holder.title.text = "Nursing Assistant"

        //holder.hiredate.text = person.hireDate
       // holder.name.text = person.name
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
    inner class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView)
    {
        val name : TextView = itemView.findViewById(R.id.days_name)
        val title : TextView = itemView.findViewById(R.id.days_title)
        val hiredate : TextView = itemView.findViewById(R.id.days_hiredate)
    }
}