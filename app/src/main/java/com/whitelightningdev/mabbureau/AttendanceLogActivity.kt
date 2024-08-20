package com.whitelightningdev.mabbureau

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class AttendanceLogActivity : AppCompatActivity() {

    private lateinit var attendanceListView: ListView
    private lateinit var backButton: Button
    private val attendanceList = mutableListOf<String>() // This will hold attendance records
    private lateinit var adapter: ArrayAdapter<String> // Declare adapter here

    // Use lazy initialization for SharedPreferences
    private val sharedPreferences by lazy {
        getSharedPreferences("EmployeePrefs", Context.MODE_PRIVATE)
    }

    // Date formats
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    private val dayFormat = SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance_log)

        // Initialize views
        attendanceListView = findViewById(R.id.attendanceListView)
        backButton = findViewById(R.id.backButton) // Ensure you have this button in your layout

        // Initialize the adapter and set it to the ListView
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, attendanceList)
        attendanceListView.adapter = adapter

        // Load attendance data
        loadAttendanceData()

        // Set up back button listener
        backButton.setOnClickListener {
            navigateToMainActivity()
        }
    }

    private fun loadAttendanceData() {
        // Clear the current attendance list
        attendanceList.clear()

        // Load attendance data from SharedPreferences
        val allEntries = sharedPreferences.all
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        // Add today's attendance records with a header
        attendanceList.add("Attendance Log for $currentDate:")

        // Loop through SharedPreferences entries
        for ((key, value) in allEntries) {
            // Ensure that the key contains a valid employee identifier and action
            // (e.g., "Alice_signIn", "Bob_signOut", "Alice_lunchIn", "Alice_lunchOut")
            if (key.endsWith("_signIn") || key.endsWith("_signOut") ||
                key.endsWith("_lunchIn") || key.endsWith("_lunchOut")) {

                // Extract employee name and action
                val parts = key.split("_")
                if (parts.size == 2) {
                    val employeeName = parts[0]
                    val action = parts[1]
                    val timestamp = value.toString()

                    // Attempt to format the output
                    attendanceList.add("$employeeName - ${action.replace("_", " ").capitalize()} at $timestamp")
                }
            }
        }

        // Notify the adapter of data changes
        adapter.notifyDataSetChanged()
    }

    private fun navigateToMainActivity() {
        // Create an Intent to navigate back to MainActivity
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Optional: finish current activity if you don't want it in the back stack
    }
}
