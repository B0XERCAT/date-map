package edu.skku.cs.datemap

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.coroutines.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import kotlin.coroutines.suspendCoroutine

class LoadingActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var loadingText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        progressBar = findViewById(R.id.progressBar)
        loadingText = findViewById(R.id.loadingText)

        val location = intent.getStringExtra("location") ?: ""

        fetchDataFromApi(location)
    }

    private fun fetchDataFromApi(location: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val (restaurant, cafe, attraction) = fetchData(location)

                if (restaurant.address.isEmpty() || cafe.address.isEmpty() || attraction.address.isEmpty()) {
                    Toast.makeText(this@LoadingActivity, "코스 생성에 실패했습니다.", Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(this@LoadingActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    return@launch
                }

                progressBar.visibility = ProgressBar.GONE
                loadingText.visibility = TextView.GONE

                val intent = Intent(this@LoadingActivity, ResultActivity::class.java)
                intent.putExtra("restaurant", Gson().toJson(restaurant))
                intent.putExtra("cafe", Gson().toJson(cafe))
                intent.putExtra("attraction", Gson().toJson(attraction))

                startActivity(intent)
                finish()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    this@LoadingActivity,
                    "코스 생성에 실패했습니다. 장소를 찾을 수 없습니다.",
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(this@LoadingActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
                return@launch
            }
        }
    }

    private suspend fun fetchData(location: String): Triple<DataModel.Item, DataModel.Item, DataModel.Item> {
        val restaurant = fetchPlaceData(location, "맛집")
        val cafe = fetchPlaceData(location, "카페")
        val attraction = fetchPlaceData(location, "관광지")

        val shuffledRestaurant = restaurant.shuffled()
        val shuffledCafe = cafe.shuffled()
        val shuffledAttraction = attraction.shuffled()
        return Triple(
            shuffledRestaurant.firstOrNull() ?: DataModel.Item("No restaurant", "", "", "", "", ""),
            shuffledCafe.firstOrNull() ?: DataModel.Item("No cafe", "", "", "", "", ""),
            shuffledAttraction.firstOrNull() ?: DataModel.Item("No attraction", "", "", "", "", "")
        )
    }

    private suspend fun fetchPlaceData(
        location: String,
        category: String
    ): MutableList<DataModel.Item> {
        val url =
            "https://openapi.naver.com/v1/search/local.json?query=$location$category&start=1&sort=random&display=5"
        val clientId = BuildConfig.X_NAVER_CLIENT_ID
        val clientSecret = BuildConfig.X_NAVER_CLIENT_SECRET

        val request = Request.Builder()
            .url(url)
            .addHeader("X-Naver-Client-Id", clientId)
            .addHeader("X-Naver-Client-Secret", clientSecret)
            .build()

        val client = OkHttpClient()

        return suspendCoroutine { continuation ->
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWith(Result.failure(e))
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) {
                            continuation.resumeWith(Result.failure(IOException("Unexpected code $response")))
                            return
                        }
                        val str = response.body!!.string()
                        val data = Gson().fromJson(str, DataModel::class.java)

                        data.items.forEach { item ->
                            item.title = removeHtmlTags(item.title)
                        }

                        continuation.resumeWith(Result.success(data.items) as Result<MutableList<DataModel.Item>>)
                    }
                }
            })
        }
    }

    private fun removeHtmlTags(input: String): String {
        // HTML 엔티티 제거: &amp;, &lt; 등
        var cleanedString = input.replace(Regex("&[a-zA-Z0-9#]+;"), "")

        // 모든 HTML 태그 제거: <b>, <div>, <i>, <p>, <br> 등
        cleanedString = cleanedString.replace(Regex("<[^>]*>"), "")

        return cleanedString
    }

}
