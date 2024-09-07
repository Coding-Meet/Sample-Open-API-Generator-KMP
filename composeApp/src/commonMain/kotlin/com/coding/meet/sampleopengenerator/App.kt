package com.coding.meet.sampleopengenerator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.coding.meet.sampleopengenerator.code.apis.PostsApi
import com.coding.meet.sampleopengenerator.code.models.Post
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.jetbrains.compose.ui.tooling.preview.Preview
import io.ktor.client.HttpClient
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.url

@Composable
@Preview
fun App() {
    MaterialTheme {
        var apiResponse by remember { mutableStateOf(Greeting().greet())}
        val coroutineScope = rememberCoroutineScope()
        LaunchedEffect(Unit) {
            coroutineScope.launch() {
                try {

                    // normal api calling
                    val httpClient = HttpClient {
                        defaultRequest {
                            url("https://jsonplaceholder.typicode.com/")
                            contentType(ContentType.Application.Json)
                        }
                        install(HttpTimeout) {
                            socketTimeoutMillis = 60_000
                            requestTimeoutMillis = 60_000
                        }
                        install(ContentNegotiation) {
                            json(Json {
                                prettyPrint = true
                                isLenient = true
                                ignoreUnknownKeys = true
                                explicitNulls = false
                            })
                        }

                    }
                    val posts = httpClient.get {
                        url("posts")
                    }.body<List<Post>>()
                    println("postList=$posts")


                    // open api calling
                    val httpApiClient = PostsApi(
                        "https://jsonplaceholder.typicode.com/",
                        httpClientConfig = {
                            it.install(ContentNegotiation) {
                                json(Json {
                                    prettyPrint = true
                                    isLenient = true
                                    ignoreUnknownKeys = true
                                    explicitNulls = false
                                })
                            }
                        }
                    )
                    apiResponse = httpApiClient.getPosts().body().toString()
                } catch (e: Exception) {
                    apiResponse = e.message.toString()
                    e.printStackTrace()
                }
            }
        }
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Api: $apiResponse")
        }
    }
}