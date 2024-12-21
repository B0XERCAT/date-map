package edu.skku.cs.datemap

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.ProgressBar
import android.widget.TextView
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class LoadingActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var loadingText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        progressBar = findViewById(R.id.progressBar)
        loadingText = findViewById(R.id.loadingText)

        val location = intent.getStringExtra("location") ?: ""

        // 비동기적으로 네이버 API 호출
        fetchDataFromApi(location)
    }

    private fun fetchDataFromApi(location: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // API 호출 시뮬레이션 (실제 네이버 API를 호출해야 함)
                val (restaurant, cafe, attraction) = fetchData(location)

                // 로딩 끝나면 결과 화면으로 이동
                progressBar.visibility = ProgressBar.GONE
                loadingText.visibility = TextView.GONE

                val intent = Intent(this@LoadingActivity, ResultActivity::class.java)
                intent.putExtra("restaurant", restaurant)
                intent.putExtra("cafe", cafe)
                intent.putExtra("attraction", attraction)
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                e.printStackTrace()
                // 에러 처리
            }
        }
    }

    private suspend fun fetchData(location: String): Triple<String, String, String> {
        // API 요청 코드
        val client = OkHttpClient()

        // 맛집, 카페, 명소 GET 요청
        val restaurant = fetchPlaceData(location, "맛집")
        val cafe = fetchPlaceData(location, "카페")
        val attraction = fetchPlaceData(location, "명소")

        // 랜덤으로 하나씩 뽑아오기 (여기선 예시로 첫 번째 값만 사용)
        return Triple(restaurant[0], cafe[0], attraction[0])
    }

    private suspend fun fetchPlaceData(location: String, category: String): List<String> {
        // 실제 네이버 API 호출
        val url = "https://openapi.naver.com/v1/search/local.json?query=$location+$category"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        val response = client.newCall(request).execute()
        val jsonResponse = response.body?.string()
        val jsonObject = JSONObject(jsonResponse)
        val items = jsonObject.getJSONArray("items")

        val places = mutableListOf<String>()
        for (i in 0 until items.length()) {
            val place = items.getJSONObject(i).getString("title")
            places.add(place)
        }
        return places
    }
}
