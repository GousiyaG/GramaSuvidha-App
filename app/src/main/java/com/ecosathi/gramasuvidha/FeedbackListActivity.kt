package com.ecosathi.gramasuvidha

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class FeedbackListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FeedbackAdapter

    private val feedbackList = mutableListOf<Feedback>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback_list)

        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = FeedbackAdapter(feedbackList)
        recyclerView.adapter = adapter

        loadFeedback()
    }

    private fun loadFeedback() {

        val db = FirebaseDatabase.getInstance().getReference("feedback")

        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                feedbackList.clear()

                if (!snapshot.exists()) {
                    Toast.makeText(this@FeedbackListActivity, "No feedback found ❌", Toast.LENGTH_SHORT).show()
                    return
                }

                for (snap in snapshot.children) {
                    val feedback = snap.getValue(Feedback::class.java)
                    if (feedback != null) {
                        feedbackList.add(feedback)
                    }
                }

                adapter.updateList(feedbackList)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@FeedbackListActivity, "Error loading data ❌", Toast.LENGTH_SHORT).show()
            }
        })
    }
}