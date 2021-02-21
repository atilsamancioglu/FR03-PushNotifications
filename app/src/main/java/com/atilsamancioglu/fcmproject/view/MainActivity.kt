package com.atilsamancioglu.fcmproject.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.atilsamancioglu.fcmproject.R
import com.atilsamancioglu.fcmproject.model.NotificationData
import com.atilsamancioglu.fcmproject.model.PushNotification
import com.atilsamancioglu.fcmproject.service.RetrofitObject
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    val topic = "/topics/generalInfo"
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseMessaging.getInstance().subscribeToTopic(topic)

        db = Firebase.firestore




        val token = FirebaseMessaging.getInstance().token.addOnSuccessListener {
            println("token: ${it}")

            val dataMap = hashMapOf<String, String>()
            dataMap.put("token",it)
            dataMap.put("useremail","atilsam@gmail.com")

            db.collection("Users").add(dataMap).addOnSuccessListener {
                //do some stuff
            }

        }

    }

    fun send(view : View) {
        val title = titleText.text.toString()
        val message = messageText.text.toString()

        if (title.isNotEmpty() && message.isNotEmpty()) {
            val data = NotificationData(title,message)
            val notification = PushNotification(data,"faQs48LHSneN_5O2aQ6JZu:APA91bFNLFEfeXFT2Yono2jzwWNNkoCdUF1ULsmHXL9wIs19dzEUIjBn_nTwgMrtQxHx3WYD-SKBHtzuR-s2o0Lvm-VUt5HVCbuu0YD4Zx6W2HK8p4C_7gpEAgpAqMmtbAWC0ShaZTFZ")
            sendNotification(notification)
        }

    }

    private fun sendNotification(pushNotification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {

            val response = RetrofitObject.api.postNotification(pushNotification)
            if (response.isSuccessful) {
                println(response)
            } else {
                println(response.errorBody())
            }

        } catch (e : Exception) {
            e.printStackTrace()
        }
    }

}