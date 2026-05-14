package com.ecosathi.gramasuvidha

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class ProjectsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProjectAdapter
    private lateinit var searchBar: EditText

    private val projectList = mutableListOf<Project>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_projects)

        recyclerView = findViewById(R.id.recyclerView)
        searchBar = findViewById(R.id.searchBar)

        // Back button
        findViewById<TextView>(R.id.backBtn).setOnClickListener {
            finish()
        }

        // Home button
        findViewById<Button>(R.id.homeBtn).setOnClickListener {
            finish()
        }

        // RecyclerView setup
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ProjectAdapter(projectList)
        recyclerView.adapter = adapter

        loadProjects()
        setupSearch()
    }

    private fun loadProjects() {
        val db = FirebaseDatabase.getInstance().getReference("projects")

        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                projectList.clear()

                for (snap in snapshot.children) {
                    val project = snap.getValue(Project::class.java)
                    if (project != null) {
                        projectList.add(project)
                    }
                }

                adapter.updateList(projectList)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun setupSearch() {

        // 🔹 LIVE SEARCH (typing)
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                performSearch(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // 🔹 SEARCH BUTTON (keyboard)
        searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(searchBar.text.toString())
                true
            } else {
                false
            }
        }
    }

    // 🔥 COMMON SEARCH LOGIC (USED BY BOTH)
    private fun performSearch(text: String) {

        val query = text.trim().lowercase()

        val filtered = projectList.filterIndexed { index, project ->

            val projectNumber = (index + 1).toString()

            project.name.lowercase().contains(query) ||
                    project.address.lowercase().contains(query) ||
                    project.startDate.lowercase().contains(query) ||
                    project.completedDate.lowercase().contains(query) ||
                    project.status.lowercase().contains(query) ||
                    projectNumber.contains(query) ||
                    ("project $projectNumber").contains(query)
        }

        adapter.updateList(filtered)
    }
}

