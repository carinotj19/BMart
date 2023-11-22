package com.example.bmart.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bmart.Adapters.MessageAdapter
import com.example.bmart.Models.MessageModel
import com.example.bmart.R

class Messages : Fragment(), MessageAdapter.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var messagesArrayList: ArrayList<MessageModel>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataInitialize()

        recyclerView = view.findViewById(R.id.messages_list)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        val messageAdapter = MessageAdapter(messagesArrayList, this)
        recyclerView.adapter = messageAdapter

    }

    override fun onItemClick(position: Int) {
        Toast.makeText(requireContext(), "Conversation #$position is private", Toast.LENGTH_SHORT).show()
        val clickedItem = messagesArrayList[position]
        clickedItem.name = "clicked"
    }

    private fun dataInitialize() {
        messagesArrayList = arrayListOf<MessageModel>()

        val profilePicture = intArrayOf(
            R.drawable.man2,
            R.drawable.man2,
            R.drawable.man2,
            R.drawable.man2,
            R.drawable.man2,
            R.drawable.man2,
            R.drawable.man2,
            R.drawable.man2,
            R.drawable.man2,
            R.drawable.man2
        )

        val name = arrayOf(
            "Name A",
            "Name B",
            "Name C",
            "Name D",
            "Name E",
            "Name F",
            "Name G",
            "Name H",
            "Name I",
            "Name J",
        )

        val recentText = arrayOf(
            "Recent Text A",
            "Recent Text B",
            "Recent Text C",
            "Recent Text D",
            "Recent Text E",
            "Recent Text F",
            "Recent Text G",
            "Recent Text H",
            "Recent Text I",
            "Recent Text J",
        )

        for (i in name.indices) {
            val messages = MessageModel(profilePicture[i], name[i], recentText[i])
            messagesArrayList.add(messages)
        }
    }
}