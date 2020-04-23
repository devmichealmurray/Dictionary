package com.devmmurray.dictionary

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URL

private const val BASE_URL = "https://od-api.oxforddictionaries.com/api/v2/entries/en/"

class MainActivity : AppCompatActivity() {

    private var searchWord: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
    }

    override fun onResume() {
        super.onResume()
        searchEditText.text = null
    }

    inner class myAsyncTask : AsyncTask<String, Void, Data>() {
        override fun doInBackground(vararg params: String?): Data? {
            val url = URL(params[0])
            var data: Data? = null
            try {
                val jsonResponse = makeHTTPResponse(url)
                data = jsonResponse?.let { extractJson(it) }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("myAsyncTask", "${e.message}")
            }
            return data
        }

        override fun onPostExecute(result: Data?) {
            super.onPostExecute(result)

            if (result != null) {
                result.definition?.let { showDefinition(it) }
            } else {
                return
            }


        }

    }

    fun findWord(view: View) {
        val wordId = searchEditText.text.toString()
        searchWord = wordId
        val url = "${BASE_URL}${wordId}"

        val task = myAsyncTask()
        task.execute(url)
    }

    private fun showDefinition(definition: String) {
        val intent = Intent(this, Definition::class.java)
        intent.putExtra("definition", definition)
        intent.putExtra("searchWord", searchWord)
        startActivity(intent)
    }

}
