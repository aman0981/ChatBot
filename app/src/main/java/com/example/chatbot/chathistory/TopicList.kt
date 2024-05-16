package com.example.chatbot.chathistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatbot.R
import com.example.chatbot.firebase.FireBaseUtil


class TopicList : Fragment() {


    lateinit var recyclerView: RecyclerView
    lateinit var adapter: TopicListAdapter
    var chatRoomId: String = FireBaseUtil.getChatroomId(FireBaseUtil.currentUserId())

    lateinit var topicNames: MutableList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_topic_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        val toolbar = requireView().findViewById<Toolbar>(R.id.toolbar)
            .setupWithNavController(navController, appBarConfiguration)



        recyclerView = view.findViewById(R.id.recyclerTopic)

        topicNames = mutableListOf()

        setupTopicRecyclerView()

        recyclerView.setOnClickListener {
            findNavController().navigate(R.id.chatHistory)
        }

    }

    fun setupTopicRecyclerView(){

//        val query : CollectionReference = FireBaseUtil.allTopicCollection(chatRoomId)
//
//
//        val options : FirestoreRecyclerOptions<ChatroomModel> =  FirestoreRecyclerOptions.Builder<ChatroomModel>()
//                         .setQuery(query,ChatroomModel::class.java)
//                         .build()

        FireBaseUtil.allTopicCollection(chatRoomId)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val topicName = document.getString("topicName")
                    if (topicName != null) {
                        topicNames.add(topicName)
                    }
                }

                adapter = TopicListAdapter(topicNames, requireContext())
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()

            }

    }


}