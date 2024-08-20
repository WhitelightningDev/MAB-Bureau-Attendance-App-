package com.whitelightningdev.mabbureau

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AdminActivity : AppCompatActivity() {

    private lateinit var employeeNameInput: EditText
    private lateinit var addUserButton: Button
    private lateinit var sharedPreferences: SharedPreferences
    private val employeeList = mutableListOf<String>() // List to store employee names

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        employeeNameInput = findViewById(R.id.employeeNameInput)
        addUserButton = findViewById(R.id.addUserButton)

        sharedPreferences = getSharedPreferences("EmployeePrefs", MODE_PRIVATE)

        // Load existing users when the activity is created
        loadUsers()

        addUserButton.setOnClickListener { addUser() }
    }

    private fun addUser() {
        val employeeName = employeeNameInput.text.toString()
        if (employeeName.isNotBlank() && !employeeList.contains(employeeName)) {
            employeeList.add(employeeName) // Add user to the list
            saveUserToLocal(employeeName) // Save user to SharedPreferences
            Toast.makeText(this, "User added: $employeeName", Toast.LENGTH_SHORT).show()
            employeeNameInput.text.clear() // Clear the input field

            // Set result to notify MainActivity to refresh its user list
            setResult(RESULT_OK)
            finish() // Close AdminActivity
        } else {
            Toast.makeText(this, "Please enter a unique name", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveUserToLocal(employeeName: String) {
        val editor = sharedPreferences.edit()
        editor.putStringSet("employee_names", employeeList.toSet()) // Save the list as a Set
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
