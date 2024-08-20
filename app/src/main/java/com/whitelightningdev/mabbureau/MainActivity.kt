package com.whitelightningdev.mabbureau

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var employeeName: TextView
    private lateinit var signInButton: Button
    private lateinit var signOutButton: Button
    private lateinit var lunchInButton: Button
    private lateinit var lunchOutButton: Button
    private lateinit var userSpinner: Spinner
    private lateinit var adminButton: Button
    private lateinit var logButton: Button

    private val sharedPreferences by lazy {
        getSharedPreferences("EmployeePrefs", Context.MODE_PRIVATE)
    }

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    private val employeeList = mutableListOf<String>() // Mutable list for employee names

    private val addUserResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Refresh employee names when returning from AdminActivity
                loadUsers()
                updateUserSpinner()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userSpinner = findViewById(R.id.userSpinner)
        employeeName = findViewById(R.id.employeeName)
        signInButton = findViewById(R.id.signInButton)
        signOutButton = findViewById(R.id.signOutButton)
        lunchInButton = findViewById(R.id.lunchInButton)
        lunchOutButton = findViewById(R.id.lunchOutButton)
        adminButton = findViewById(R.id.adminButton)
        logButton = findViewById(R.id.logButton)

        // Load employee names from SharedPreferences
        loadUsers()
        updateUserSpinner()

        // Set listeners for buttons
        signInButton.setOnClickListener { signIn() }
        signOutButton.setOnClickListener { signOut() }
        lunchInButton.setOnClickListener { lunchIn() }
        lunchOutButton.setOnClickListener { lunchOut() }
        adminButton.setOnClickListener { navigateToAdmin() }
        logButton.setOnClickListener { navigateToAttendanceLog() }
    }

    private fun loadUsers() {
        val savedUsers = sharedPreferences.getStringSet("employee_names", null)
        if (savedUsers != null) {
            employeeList.clear() // Clear existing employee list to avoid duplicates
            employeeList.addAll(savedUsers) // Add saved users to the list
        }
    }

    private fun updateUserSpinner() {
        // Create a list for the spinner including the placeholder
        val userList = mutableListOf("Select User") // Add placeholder text
        userList.addAll(employeeList) // Add employee names

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, userList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        userSpinner.adapter = adapter
    }

    private fun signIn() {
        val currentTime = dateFormat.format(Date())
        val employee = userSpinner.selectedItem.toString()

        if (employee.isNotBlank() && employee != "Select User") {
            // Save sign-in time in SharedPreferences
            sharedPreferences.edit().putString("${employee}_signIn", currentTime).apply()
            Toast.makeText(this, "$employee signed in at $currentTime", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Please select an employee", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signOut() {
        val currentTime = dateFormat.format(Date())
        val employee = userSpinner.selectedItem.toString()

        if (employee.isNotBlank() && employee != "Select User") {
            // Save sign-out time in SharedPreferences
            sharedPreferences.edit().putString("${employee}_signOut", currentTime).apply()
            Toast.makeText(this, "$employee signed out at $currentTime", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Please select an employee", Toast.LENGTH_SHORT).show()
        }
    }

    private fun lunchIn() {
        val currentTime = dateFormat.format(Date())
        val employee = userSpinner.selectedItem.toString()

        if (employee.isNotBlank() && employee != "Select User") {
            // Save lunch in time in SharedPreferences
            sharedPreferences.edit().putString("${employee}_lunchIn", currentTime).apply()
            Toast.makeText(this, "$employee lunch in at $currentTime", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Please select an employee", Toast.LENGTH_SHORT).show()
        }
    }

    private fun lunchOut() {
        val currentTime = dateFormat.format(Date())
        val employee = userSpinner.selectedItem.toString()

        if (employee.isNotBlank() && employee != "Select User") {
            // Save lunch out time in SharedPreferences
            sharedPreferences.edit().putString("${employee}_lunchOut", currentTime).apply()
            Toast.makeText(this, "$employee lunch out at $currentTime", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Please select an employee", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToAdmin() {
        val intent = Intent(this, AdminActivity::class.java)
        addUserResultLauncher.launch(intent) // Launch AdminActivity with result
    }

    private fun navigateToAttendanceLog() {
        val intent = Intent(this, AttendanceLogActivity::class.java)
        startActivity(intent)
    }
}
