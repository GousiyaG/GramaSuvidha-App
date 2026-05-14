package com.ecosathi.gramasuvidha

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val btn = findViewById<Button>(R.id.addProjectBtn)

        btn.setOnClickListener {
            Toast.makeText(this, "Add Project Clicked", Toast.LENGTH_SHORT).show()
        }
    }
}