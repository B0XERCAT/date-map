package edu.skku.cs.datemap

import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker

class ResultActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var listView: ListView
    private lateinit var naverMap: NaverMap

    private lateinit var restaurant: DataModel.Item
    private lateinit var cafe: DataModel.Item
    private lateinit var attraction: DataModel.Item

    private val mapView: MapView by lazy {
        findViewById<MapView>(R.id.mapView)
    }

    companion object {
        private const val MAP_SCALE = 10000000.0
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map

        val averageLat =
            (restaurant.mapy.toLong() + cafe.mapy.toLong() + attraction.mapy.toLong()) / 3.0
        val averageLng =
            (restaurant.mapx.toLong() + cafe.mapx.toLong() + attraction.mapx.toLong()) / 3.0

        val lat = averageLat / MAP_SCALE
        val lng = averageLng / MAP_SCALE

        val cameraUpdate = CameraUpdate.scrollTo(LatLng(lat, lng)).animate(CameraAnimation.Easing)
        naverMap.moveCamera(cameraUpdate)

        val restaurantMarker = Marker()
        restaurantMarker.position =
            LatLng(restaurant.mapy.toLong() / MAP_SCALE, restaurant.mapx.toLong() / MAP_SCALE)
        restaurantMarker.map = naverMap
        restaurantMarker.captionText = restaurant.title

        val cafeMarker = Marker()
        cafeMarker.position = LatLng(cafe.mapy.toLong() / MAP_SCALE, cafe.mapx.toLong() / MAP_SCALE)
        cafeMarker.map = naverMap
        cafeMarker.captionText = cafe.title

        val attractionMarker = Marker()
        attractionMarker.position =
            LatLng(attraction.mapy.toLong() / MAP_SCALE, attraction.mapx.toLong() / MAP_SCALE)
        attractionMarker.map = naverMap
        attractionMarker.captionText = attraction.title

        print(restaurantMarker.position.latitude)
        print(restaurantMarker.position.longitude)
        print(cafeMarker.position.latitude)
        print(cafeMarker.position.longitude)
        print(attractionMarker.position.latitude)
        print(attractionMarker.position.longitude)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        listView = findViewById(R.id.listView)

        val restaurantJson = intent.getStringExtra("restaurant")
        val cafeJson = intent.getStringExtra("cafe")
        val attractionJson = intent.getStringExtra("attraction")

        if (restaurantJson == null || cafeJson == null || attractionJson == null) {
            Toast.makeText(this, "데이터가 누락되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        restaurant = Gson().fromJson(restaurantJson, DataModel.Item::class.java)
        cafe = Gson().fromJson(cafeJson, DataModel.Item::class.java)
        attraction = Gson().fromJson(attractionJson, DataModel.Item::class.java)

        val items = arrayListOf<DataModel.Item>()
        items.add(restaurant)
        items.add(cafe)
        items.add(attraction)

        val adapter = ItemAdapter(this, items)
        listView.adapter = adapter
        adapter.notifyDataSetChanged()
    }
}
