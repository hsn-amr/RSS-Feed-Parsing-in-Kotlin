package com.example.rssfeedparsinginkotlin

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {

    lateinit var adapter: RVQuestion
    lateinit var rvMain: RecyclerView
    var questions = mutableListOf<Question>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvMain = findViewById(R.id.rvMain)

        FetchTopSongs().execute()

    }

    private inner class FetchTopSongs : AsyncTask<Void, Void, MutableList<Question>>(){
        val parser = XMLParser()

        override fun doInBackground(vararg params: Void?): MutableList<Question> {
            val url = URL("https://stackoverflow.com/feeds")
            val urlConnection = url.openConnection() as HttpsURLConnection
            questions = urlConnection.getInputStream()?.let {
                parser.parse(it)
            } as MutableList<Question>
            Log.e("TAG", "$questions")
            return questions
        }

        override fun onPostExecute(result: MutableList<Question>?) {
            super.onPostExecute(result)
            adapter = RVQuestion(questions)
            rvMain.adapter = adapter
            rvMain.layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }
}