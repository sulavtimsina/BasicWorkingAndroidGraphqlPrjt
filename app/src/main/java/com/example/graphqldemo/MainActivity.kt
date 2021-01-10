package com.example.graphqldemo

import BooksQuery
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // First, create an `ApolloClient`
        val apolloClient = ApolloClient.builder()
            .serverUrl("http://10.0.2.2:4000")
            .build()

        lifecycleScope.launch {
            val response = try {
                apolloClient.query(BooksQuery()).toDeferred().await()

            } catch (e: ApolloException) {
                // handle protocol errors
                return@launch
            }
            val booksData = response.data
            if (booksData == null || response.hasErrors()) {
                // handle application errors
                return@launch
            }
            // launch now contains a typesafe model of your data
            println("Launch site: ${booksData.books?.get(0)?.author}")
            findViewById<TextView>(R.id.textview).text = booksData.books?.get(0)?.author
        }

    }
}