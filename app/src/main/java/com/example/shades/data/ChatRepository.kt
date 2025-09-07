package com.example.shades.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

object ChatRepository {
    private val db = FirebaseFirestore.getInstance()
    private val chatsRef = db.collection("chats")

    // deterministic chatId for pair of uids
    private fun makeChatId(a: String, b: String): String {
        return listOf(a, b).sorted().joinToString("_")
    }

    // ensure chat doc exists
    suspend fun ensureChat(currentUid: String, otherUid: String) : String {
        val chatId = makeChatId(currentUid, otherUid)
        val docRef = chatsRef.document(chatId)
        val snapshot = docRef.get().await()
        if (!snapshot.exists()) {
            val data = mapOf(
                "chatId" to chatId,
                "participants" to listOf(currentUid, otherUid),
                "lastMessage" to "",
                "lastTimestamp" to System.currentTimeMillis()
            )
            docRef.set(data, SetOptions.merge()).await()
        }
        return chatId
    }

    // send message
    suspend fun sendMessage(chatId: String, senderId: String, receiverId: String, text: String) {
        val msgId = db.collection("dummy").document().id
        val msgData = mapOf(
            "senderId" to senderId,
            "receiverId" to receiverId,
            "message" to text,
            "timestamp" to System.currentTimeMillis()
        )
        chatsRef.document(chatId).collection("messages").add(msgData).await()
        // update parent chat lastMessage and lastTimestamp
        chatsRef.document(chatId).set(
            mapOf("lastMessage" to text, "lastTimestamp" to System.currentTimeMillis()),
            SetOptions.merge()
        ).await()
    }
}