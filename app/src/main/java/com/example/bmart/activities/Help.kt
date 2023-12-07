package com.example.bmart.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bmart.R
import com.example.bmart.adapters.FAQAdapter
import com.example.bmart.models.FAQModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.github.serpro69.kfaker.Faker
import java.util.Calendar

class Help : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var issueEditText: TextInputEditText
    private lateinit var submitIssueButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)

        val backBtn: ImageButton = findViewById(R.id.back_button)

        backBtn.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            intent.putExtra("fragment", "home")
            setResult(RESULT_OK, intent)
            startActivity(intent)
            finish()
        }

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        val faker = Faker()

        val faqList = mutableListOf<FAQModel>()
// Generate fake FAQ data
        for (i in 1..20) { // Adjust the loop count based on how many FAQs you want
            val question = "Q: ${faker.lorem.words()}"
            val answer = "A: ${faker.chuckNorris.fact()}"

            faqList.add(FAQModel(question, answer))
        }

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewFAQ)
        val faqAdapter = FAQAdapter(faqList) // Create an adapter for FAQs
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = faqAdapter

        submitIssueButton = findViewById(R.id.submitIssueButton)
        issueEditText = findViewById(R.id.issueEditText)

        submitIssueButton.setOnClickListener {
            val issueText = issueEditText.text.toString().trim()
            if (issueText.isNotEmpty()) {
                uploadIssueToFirestore(issueText)
            }
        }
    }

    private fun uploadIssueToFirestore(issueText: String) {
        val user = auth.currentUser
        val userEmail = user?.email

        val issueData = hashMapOf(
            "userEmail" to userEmail,
            "issueText" to issueText,
            "timestamp" to Calendar.getInstance().time
        )

        firestore.collection("Issues")
            .add(issueData)
            .addOnSuccessListener {
                Toast.makeText(this, "Issue Uploaded Successfully", Toast.LENGTH_SHORT).show()
                issueEditText.text?.clear()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show()
            }
    }
}