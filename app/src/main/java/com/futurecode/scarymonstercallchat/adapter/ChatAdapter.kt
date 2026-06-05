package com.futurecode.scarymonstercallchat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.futurecode.scarymonstercallchat.model.ChatMessage
import android.view.View
import android.widget.TextView
import com.futurecode.scarymonstercallchat.R
import android.widget.LinearLayout
import kotlinx.coroutines.*

class ChatAdapter(private val messages: List<ChatMessage>) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val llMonsterMessage: LinearLayout = view.findViewById(R.id.llMonsterMessage)
        val tvMonsterText: TextView = view.findViewById(R.id.tvMonsterText)
        val llUserMessage: LinearLayout = view.findViewById(R.id.llUserMessage)
        val tvUserText: TextView = view.findViewById(R.id.tvUserText)
        val llTypingIndicator: LinearLayout = view.findViewById(R.id.llTypingIndicator)
        val textAnimate: TextView = view.findViewById(R.id.textAnimate)
        var typingJob: Job? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_message, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = messages[position]

        holder.typingJob?.cancel()

        if (message.isTyping) {
            holder.llTypingIndicator.visibility = View.VISIBLE
            holder.llMonsterMessage.visibility = View.GONE
            holder.llUserMessage.visibility = View.GONE

            holder.typingJob = CoroutineScope(Dispatchers.Main).launch {
                var dots = ""
                while (isActive) {
                    holder.textAnimate.text = "Typing$dots"
                    dots = if (dots.length >= 3) "" else "$dots."
                    delay(400)
                }
            }
        }
        else if (message.isFromMonster) {
            holder.llTypingIndicator.visibility = View.GONE
            holder.llMonsterMessage.visibility = View.VISIBLE
            holder.llUserMessage.visibility = View.GONE
            holder.tvMonsterText.text = message.text
        }
        else {
            holder.llTypingIndicator.visibility = View.GONE
            holder.llMonsterMessage.visibility = View.GONE
            holder.llUserMessage.visibility = View.VISIBLE
            holder.tvUserText.text = message.text
        }
    }

    override fun getItemCount(): Int = messages.size
}