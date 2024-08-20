package com.whitelightningdev.mabbureau

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ArrayAdapter

class EmployeeAdapter(
    context: Context,
    private val employeeList: MutableList<String>,
    private val deleteListener: (Int) -> Unit
) : ArrayAdapter<String>(context, 0, employeeList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_employee, parent, false)

        val employeeNameTextView: TextView = view.findViewById(R.id.employeeNameTextView)
        val deleteIcon: ImageView = view.findViewById(R.id.deleteIcon)

        val employeeName = employeeList[position]
        employeeNameTextView.text = employeeName

        // Set the delete icon click listener
        deleteIcon.setOnClickListener {
            deleteListener(position) // Trigger the delete action
        }

        return view
    }
}
