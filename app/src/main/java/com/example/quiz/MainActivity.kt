package com.example.quiz

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.io.InputStream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val list: Array<String> = arrayOf("asdasdasd@ads.asd", "asdasdasd@ads.asd")
        Log.e("TAG", "SIZE: ${numUniqueEmails(list)}")

        val questions = getQuestions()
        setContent {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
                MainScreen(questions.shuffled())
            }
        }
    }

    private fun getQuestions(): List<Question> {
        val json: String? = try {
            val stream: InputStream = assets.open("questions.json")
            val size = stream.available()
            val buffer = ByteArray(size)
            stream.read(buffer)
            stream.close()
            String(buffer)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return emptyList()
        }
        return Gson().fromJson(json, object : TypeToken<ArrayList<Question?>?>() {}.type)
    }

    private fun numUniqueEmails(emails: Array<String>): Int {
        val mails: HashMap<String, Int> = hashMapOf()
        for (e in emails) {
            var left = ""
            var right = ""
            var atFound = false
            var plusFound = false
            for (c in e) {
                if (atFound) right += c else if (c == '@') atFound =
                    true else if (c == '+') plusFound =
                    true else if (c != '.' && !plusFound) left += c
            }
            mails["$left@$right"] = 1
        }
        return mails.count()
    }
}
