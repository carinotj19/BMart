package com.example.bmart.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bmart.R
import com.example.bmart.adapters.ConversationAdapter
import com.example.bmart.models.ConversationModel
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class Conversation : AppCompatActivity() {
    private lateinit var userName: String
    private lateinit var messageAdapter: ConversationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)

        val backBtn = findViewById<ImageButton>(R.id.back_button)
        backBtn.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            intent.putExtra("fragment", "messages")
            setResult(RESULT_OK, intent)
            startActivity(intent)
            finish()
        }

        // Get user name and profile picture from extras
        val extras = intent.extras
        if (extras != null && extras.containsKey("USER_NAME") && extras.containsKey("PROFILE_PICTURE")) {
            val userName = extras.getString("USER_NAME")!!
            val profilePicture = extras.getInt("PROFILE_PICTURE")

            val textViewUserName = findViewById<TextView>(R.id.textViewUserName)
            textViewUserName.text = userName

            val imageViewProfilePicture = findViewById<CircleImageView>(R.id.imageViewProfilePicture)
            Picasso.get().load(profilePicture).into(imageViewProfilePicture)
        }

        val recyclerViewMessages = findViewById<RecyclerView>(R.id.recyclerViewMessages)
        recyclerViewMessages.layoutManager = LinearLayoutManager(this)

        messageAdapter = ConversationAdapter(mutableListOf())
        recyclerViewMessages.adapter = messageAdapter
    }
    fun onSendButtonClick(view: View) {
        val editTextMessage = findViewById<EditText>(R.id.editTextMessage)
        val userMessage = editTextMessage.text.toString().trim()

        if (userMessage.isNotEmpty()) {
            // Generate a random reply based on the user's message
            val randomReply = generateRandomReply(userMessage)

            // Add the user's message and the generated reply to the RecyclerView
            messageAdapter.addMessage(ConversationModel("You", userMessage))
            messageAdapter.addMessage(ConversationModel("Bot", randomReply))

            // Add these lines to your onSendButtonClick method in the Conversation activity
            Log.d("ConversationActivity", "User message: $userMessage")
            Log.d("ConversationActivity", "Bot reply: $randomReply")
            // Clear the input field
            editTextMessage.text.clear()
        }
    }

    private fun generateRandomReply(userMessage: String): String {
        // Hard-coded logic to generate a random reply based on the user's message
        return when (userMessage.toLowerCase()) {
            "hello", "hi" -> "Hi there! How can I help you?"
            "how are you" -> "I'm just a bot, but thanks for asking!"
            "goodbye" -> "Goodbye! Have a great day!"
            else -> "Sorry, I didn't understand that. Ask me something else!"
        }
    }
}