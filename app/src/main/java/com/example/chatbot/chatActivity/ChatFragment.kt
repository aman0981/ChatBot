package com.example.chatbot.chatActivity


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatbot.R
import com.example.chatbot.model.Message
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch


class ChatFragment : Fragment() {


    lateinit var edtText: TextInputEditText
    lateinit var recyclerView: RecyclerView
    lateinit var messageViewModel: MessageViewModel
    lateinit var send_btn: ImageButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        val toolbar = requireView().findViewById<Toolbar>(R.id.toolbar)
            .setupWithNavController(navController, appBarConfiguration)

        edtText = view.findViewById(R.id.edtText)
        recyclerView = view.findViewById(R.id.recyclerChat)
        send_btn = view.findViewById(R.id.send_btn)

        messageViewModel = ViewModelProvider(this)[MessageViewModel::class.java]
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        messageViewModel.getOrCreateChatroomModel()

        messageViewModel.messageList.observe(viewLifecycleOwner) { messages ->
            val adapter = MessageAdapter(messages)
            recyclerView.adapter = adapter
            recyclerView.post {
                messageViewModel._messageList.value?.size?.let { size ->
                    if (size > 0) {
                        recyclerView.smoothScrollToPosition(size - 1) } } } }

        send_btn.setOnClickListener {
            if (edtText.text!!.isEmpty()) {
                Toast.makeText(requireContext(), "Please write your Question", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val question = edtText.text.toString()
                messageViewModel.addToChat(
                    question,
                    Message.SENT_BY_ME,
                    messageViewModel.getCurrentTimestamp()
                )
                edtText.setText("")

                lifecycleScope.launch{
                messageViewModel.callApi(question)}
            }
        }
    }


}