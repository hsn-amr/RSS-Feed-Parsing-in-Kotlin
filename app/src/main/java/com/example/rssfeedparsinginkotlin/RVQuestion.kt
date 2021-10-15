package com.example.rssfeedparsinginkotlin

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_row.view.*

class RVQuestion(val questions: MutableList<Question>): RecyclerView.Adapter<RVQuestion.ItemViewHolder>() {
    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val question = questions[position]

        holder.itemView.apply {
            tvTitle.text = question.title
            tvSummary.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(question.summary, Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml(question.summary)
            }
        }
    }

    override fun getItemCount() = questions.size
}