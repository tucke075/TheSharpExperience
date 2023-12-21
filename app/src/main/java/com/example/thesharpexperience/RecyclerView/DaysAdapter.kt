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
            /*
            General case to make sure everyone display on this recycler view is a registered nurse,
            displayed their title, hiredate and name
             */
            holder.title.text = "Registered Nurse"
            holder.hiredate.text = person.hireDate
            holder.name.text = person.name
        }else{
            /*
            Special case where person is in Days firestore database but not does not have the title
            of registered nurse which indicated they are probably a lead or a nursing assistant,
            removes all their values form the recycler view
             */
            holder.title.visibility = View.GONE
            holder.hiredate.visibility = View.GONE
            holder.name.visibility = View.GONE
        }
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