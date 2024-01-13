package com.example.chatbot.chathistory

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.chatbot.R
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions


class TopicListAdapter(private val topicNames: List<String>, private val context: Context) : RecyclerView.Adapter<TopicListAdapter.TopicViewHolder>() {

//    (options: FirestoreRecyclerOptions<ChatroomModel>, private val context: Context):
//    FirestoreRecyclerAdapter<ChatroomModel, TopicListAdapter.TopicViewHolder>(options)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.topic_list_recycler_row,parent,false)
        return TopicViewHolder(view)
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {

        val topicName = topicNames[position]
        holder.bind(topicName)
        holder.itemView.setOnClickListener{
            val fragmentTransaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.topicList, ChatHistory())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }
//        holder.topic?.text = model.topicName
       // holder.lastMessage?.text = model.getlastMessage
    }



    override fun getItemCount(): Int {

        return topicNames.size
    }

//    fun startListening() {
//        TODO("Not yet implemented")
//    }

    inner class TopicViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(topicName: String) {
            topic?.text = topicName
        }

        val topic: TextView? = itemView.findViewById(R.id.chat_topic)
        //val lastMessage: TextView? = itemView.findViewById(R.id.last_message_text)
    }

}