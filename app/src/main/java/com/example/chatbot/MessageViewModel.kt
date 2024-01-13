package com.example.chatbot

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbot.chathistory.ChatMessageModel
import com.example.chatbot.chathistory.ChatroomModel
import com.example.chatbot.data.ApiClient
import com.example.chatbot.login.FireBaseUtil

import com.example.chatbot.model.CompletionRequest
import com.example.chatbot.model.CompletionResponse
import com.example.chatbot.model.Message
import com.google.firebase.Timestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MessageViewModel (application: Application): AndroidViewModel(application){


    val _messageList = MutableLiveData<MutableList<Message>>()
    val messageList : LiveData<MutableList<Message>> get() = _messageList

    init {
        _messageList.value = mutableListOf()
    }


    var chatRoomId: String = FireBaseUtil.getChatroomId(FireBaseUtil.currentUserId())
    var chatroomModel: ChatroomModel? = null
    var topicName: String = FireBaseUtil.currTopicName()



    fun addToChat(message : String , sentBy : String , timestamp : String){


        sendMessage(message, sentBy)

        val currentList = _messageList.value ?: mutableListOf()
        currentList.add(Message(message,sentBy,timestamp))
        _messageList.postValue(currentList)

    }


    private fun addResponse(response : String){
//        _messageList.value?.removeAt(_messageList.value?.size?.minus(0) ?: 0)
        addToChat(response,Message.SENT_BY_BOT,getCurrentTimestamp())
    }

    fun callApi(question : String){
        //addToChat("Typing....",Message.SENT_BY_BOT,getCurrentTimestamp())



        val completionRequest = CompletionRequest(
            model = "text-davinci-003",
            prompt = question,
            max_tokens = 4000
        )

        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getCompletions(completionRequest)
                handleApiResponse(response)
            }catch (e: SocketTimeoutException){
                addResponse("Timeout :  $e")
            }
        }
    }

    private suspend fun handleApiResponse(response: Response<CompletionResponse>) {
        withContext(Dispatchers.Main){
            if (response.isSuccessful){
                response.body()?.let { completionResponse ->
                    val result = completionResponse.choices.firstOrNull()?.text
                    if (result != null){
                        addResponse(result.trim())
                    }else{
                        addResponse("No choices found")
                    }
                }
            }else{
                addResponse("Failed to get response as API got Expired!! ${response.errorBody()}")
            }
        }

    }

    fun getCurrentTimestamp(): String {
        return SimpleDateFormat("hh : mm a", Locale.getDefault()).format(Date())
    }



    fun sendMessage( message:String, sentBy: String){

        chatroomModel?.let {
            it.getlastMessageTimestamp=Timestamp.now()
            //it.getlastMessage = message
            FireBaseUtil.getChatroomReference(chatRoomId, topicName).set(it)

            val chatMessageModel = ChatMessageModel(message, sentBy, Timestamp.now())
            FireBaseUtil.getChatroomMessageReference(chatRoomId, topicName).add(chatMessageModel)
                .addOnCompleteListener { task ->

//                    if (task.isSuccessful) {
//                        messageInput.setText("")
//                    }
                }

        }
    }
    fun getOrCreateChatroomModel() {
        FireBaseUtil.getChatroomReference(chatRoomId, topicName).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                chatroomModel = task.result.toObject(ChatroomModel::class.java)
                if (chatroomModel == null) {
                    chatroomModel = ChatroomModel(
                        topicName,
                        chatRoomId,
                        listOf(FireBaseUtil.currentUserId()),
                        Timestamp.now()
                    )
                    FireBaseUtil.getChatroomReference(chatRoomId, topicName).set(chatroomModel!!)
                }
            }
        }
    }


}