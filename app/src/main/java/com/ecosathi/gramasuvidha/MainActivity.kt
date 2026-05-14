package com.ecosathi.gramasuvidha

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val projectsBtn = findViewById<Button>(R.id.viewProjectsBtn)
        val feedbackBtn = findViewById<Button>(R.id.feedbackBtn)
        val viewFeedbackListBtn = findViewById<Button>(R.id.viewFeedbackListBtn)

        // 🔐 Check login
        val user = FirebaseAuth.getInstance().currentUser

        if (user == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // 📋 PROJECTS
        projectsBtn.setOnClickListener {
            startActivity(Intent(this, ProjectsActivity::class.java))
        }

        // ✍️ GIVE FEEDBACK
        feedbackBtn.setOnClickListener {
            startActivity(Intent(this, FeedbackActivity::class.java))
        }

        // 👀 VIEW FEEDBACK LIST
        viewFeedbackListBtn.setOnClickListener {
            startActivity(Intent(this, FeedbackListActivity::class.java))
        }
    }
}