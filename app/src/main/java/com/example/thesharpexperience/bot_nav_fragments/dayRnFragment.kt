package com.example.thesharpexperience.bot_nav_fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.thesharpexperience.R
import com.example.thesharpexperience.R.*
import com.example.thesharpexperience.RecyclerView.DaysAdapter
import com.example.thesharpexperience.person_data.days_person
import com.google.android.play.core.integrity.e
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [dayRnFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class dayRnFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    private lateinit var daysRV: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(layout.fragment_day_rn, container, false)
        daysRV = view.findViewById(R.id.dayRN_RV)

        getDaysList()
        return view
    }

    private fun getDaysList() {
        val dayList = mutableListOf<days_person>()
        //create firebase instance to be opened
        val db = FirebaseFirestore.getInstance()
        //open days collection and store data in a days_person data type
        db.collection("Days")
            .get()
            .addOnCompleteListener {
                val result: StringBuffer = StringBuffer()
                if (isAdded) {
                    if (it.isSuccessful) {
                        //try {
                            for (document in it.result!!) {
                                val name: String = document.data["name"].toString()
                                val title: Int =
                                    document.data["nurseType"]?.toString()?.toIntOrNull() ?: -1

                                val item = days_person(name, title, "00-00-0000")
                                dayList.add(item)

                            }
                            daysRV.layoutManager = LinearLayoutManager(requireContext())
                            daysRV.adapter = DaysAdapter(dayList)
                        //} catch (e: Exception) {
                            // Handle any exceptions (like parsing errors)
                            //.e(
                                //"dayRnFragment", "Error parsing document.", e
                            //)
                        //}
                    } else {
                        it.exception?.let {
                            Log.e("dayRnFragment", "Error getting documents: ", it)
                        }

                    }
                }
            }

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment dayRn.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            dayRnFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}