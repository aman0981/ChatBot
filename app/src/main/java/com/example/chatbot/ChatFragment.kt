package com.example.chatbot

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject


class ChatFragment : Fragment() {


    lateinit var  queryEdt: TextView
    lateinit var  messageRV: RecyclerView
    lateinit var  messageAdapter: MessageAdapter
    lateinit var messageList: ArrayList<MessageViewModel>
    var url ="https://api.openai.com/v1/completions"

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


        queryEdt = view.findViewById(R.id.edtText)
        messageRV = view.findViewById(R.id.recyclerChat)
        messageList = ArrayList()
        messageAdapter= MessageAdapter(messageList)
        val layoutManager = LinearLayoutManager(requireContext())
        messageRV.layoutManager= layoutManager
        messageRV.adapter = messageAdapter

        queryEdt.setOnEditorActionListener(TextView.OnEditorActionListener{ textView, i, keyEvent ->
            if( i == EditorInfo.IME_ACTION_SEND) {
                if(queryEdt.text.toString().length > 0){
                    messageList.add(MessageViewModel(queryEdt.text.toString(), "user"))
                    messageAdapter.notifyDataSetChanged()
                    getResponse(queryEdt.text.toString())
                }else{
                    Toast.makeText(requireContext(), "Please enter your Query..", Toast.LENGTH_SHORT).show()
                }

                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun getResponse(query: String){
        queryEdt.setText("")
        val queue: RequestQueue = Volley.newRequestQueue(requireContext())
        val jsonObject: JSONObject? = JSONObject()
        jsonObject?.put("model","text-davinci-003")
        jsonObject?.put("prompt",query)
        jsonObject?.put("temperature",0)
        jsonObject?.put("max_tokens",100)
        jsonObject?.put("top_p", 1)
        jsonObject?.put("frequency_penalty", 0.0)
        jsonObject?.put("presence_penalty",0.0)

        val postRequest: JsonObjectRequest =
            @SuppressLint("NotifyDataSetChanged")
            object: JsonObjectRequest(Method.POST, url, jsonObject, Response.Listener{ response ->
                val  responseMsg: String= response.getJSONArray("choices").getJSONObject(0).getString("text")
                messageList.add(MessageViewModel(responseMsg,"bot"))
                messageAdapter.notifyDataSetChanged()

        },  Response.ErrorListener{
            Toast.makeText(requireActivity(), "Fail to get response..", Toast.LENGTH_SHORT).show()
            }){
                override fun getHeaders(): MutableMap<String, String>
                {
                    val params: MutableMap<String,String> = HashMap()
                    params["Content-Type"] = "application/json"
                    params["Authorization"] = " Bearer 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"
                    return params
                }
            }

        postRequest.setRetryPolicy(object: RetryPolicy {
            override fun getCurrentTimeout(): Int {
                return 50000
            }

            override fun getCurrentRetryCount(): Int {
                return 50000
            }

            override fun retry(error: VolleyError?) {

            }
        })

        queue.add(postRequest)
    }
}