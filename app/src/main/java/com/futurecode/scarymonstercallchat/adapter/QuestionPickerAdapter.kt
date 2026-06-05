package com.futurecode.scarymonstercallchat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.futurecode.scarymonstercallchat.R
import com.futurecode.scarymonstercallchat.model.QnA

class QuestionPickerAdapter(
    private val questions: List<QnA>,
    private val onQuestionClick: (QnA) -> Unit
) : RecyclerView.Adapter<QuestionPickerAdapter.QuestionViewHolder>() {

    class QuestionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvQuestion: TextView = view.findViewById(R.id.tvQuestionText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_question_option, parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val qna = questions[position]
        holder.tvQuestion.text = qna.question

        holder.itemView.setOnClickListener {
            onQuestionClick(qna)
        }
    }

    override fun getItemCount(): Int = questions.size
}