package com.whitelightningdev.mabbureau

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AdminActivity : AppCompatActivity() {

    private lateinit var employeeNameInput: EditText
    private lateinit var addUserButton: Button
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var employeeListView: ListView
    private lateinit var adapter: EmployeeAdapter // Declare the adapter as a member variable
    private val employeeList = mutableListOf<String>() // List to store employee names

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        employeeNameInput = findViewById(R.id.employeeNameInput)
        addUserButton = findViewById(R.id.addUserButton)
        employeeListView = findViewById(R.id.employeeListView)

        sharedPreferences = getSharedPreferences("EmployeePrefs", MODE_PRIVATE)

        // Load existing users when the activity is created
        loadUsers()

        // Set up the custom adapter for the ListView
        adapter = EmployeeAdapter(this, employeeList) { position ->
            deleteUser(position) // Call deleteUser directly
        }
        employeeListView.adapter = adapter

        addUserButton.setOnClickListener { addUser() }
    }

    private fun addUser() {
        val employeeName = employeeNameInput.text.toString()
        if (employeeName.isNotBlank() && !employeeList.contains(employeeName)) {
            employeeList.add(employeeName) // Add user to the list
            saveUserToLocal(employeeName) // Save user to SharedPreferences
            employeeNameInput.text.clear() // Clear the input field

            adapter.notifyDataSetChanged() // Notify adapter of data change
            Toast.makeText(this, "User added: $employeeName", Toast.LENGTH_SHORT).show()

            // Set the result to indicate success
            setResult(RESULT_OK)
            finish() // Close AdminActivity
        } else {
            Toast.makeText(this, "Please enter a unique name", Toast.LENGTH_SHORT).show()
        }
    }


    private fun deleteUser(position: Int) {
        val employeeName = employeeList[position]
        employeeList.removeAt(position) // Remove user from the list
        saveUsersToLocal() // Save updated list to SharedPreferences
        adapter.notifyDataSetChanged() // Notify adapter of data change
        Toast.makeText(this, "User deleted: $employeeName", Toast.LENGTH_SHORT).show()
    }

    private fun saveUserToLocal(employeeName: String) {
        val editor = sharedPreferences.edit()
        editor.putStringSet("employee_names", employeeList.toSet()) // Save the list as a Set
        editor.apply() // Save changes
    }

    private fun saveUsersToLocal() {
        val editor = sharedPreferences.edit()
        editor.putStringSet("employee_names", employeeList.toSet()) // Save the updated list as a Set
        editor.apply() // Save changes
    }

    private fun loadUsers() {
        // Load existing users from SharedPreferences
        val savedUsers = sharedPreferences.getStringSet("employee_names", null)
        savedUsers?.let {
            employeeList.addAll(it) // Add saved users to the list
        }
    }
}
