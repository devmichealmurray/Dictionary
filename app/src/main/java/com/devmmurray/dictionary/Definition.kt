package com.devmmurray.dictionary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_definition.*

class Definition : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_definition)
        supportActionBar?.hide()

        val searchWord = intent.getStringExtra("searchWord")
        val definition = intent.getStringExtra("definition")
        search_word.text = searchWord.capitalize()
        definition_tv.text = definition

        imageView.setOnClickListener {
            finish()
        }

    }




}
