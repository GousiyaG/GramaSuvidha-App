package com.ecosathi.gramasuvidha

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class FeedbackActivity : AppCompatActivity() {

    private lateinit var projectNoInput: EditText
    private lateinit var nameInput: EditText
    private lateinit var feedbackInput: EditText
    private lateinit var submitBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        projectNoInput = findViewById(R.id.projectNoInput)
        nameInput = findViewById(R.id.nameInput)
        feedbackInput = findViewById(R.id.feedbackInput)
        submitBtn = findViewById(R.id.submitBtn)

        findViewById<TextView>(R.id.backBtn).setOnClickListener {
            finish()
        }

        submitBtn.setOnClickListener {
            saveFeedback()
        }
    }

    private fun saveFeedback() {

        val projectNo = projectNoInput.text.toString().trim()
        val name = nameInput.text.toString().trim()
        val message = feedbackInput.text.toString().trim()

        if (projectNo.isEmpty() || name.isEmpty() || message.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // ✅ CORRECT PATH
        val db = FirebaseDatabase.getInstance().getReference("feedback")

        val id = db.push().key!!

        val feedback = Feedback(projectNo, name, message)

        db.child(id).setValue(feedback)
            .addOnSuccessListener {
                Toast.makeText(this, "Feedback submitted ✅", Toast.LENGTH_SHORT).show()

                projectNoInput.text.clear()
                nameInput.text.clear()
                feedbackInput.text.clear()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed ❌", Toast.LENGTH_SHORT).show()
            }
    }
}