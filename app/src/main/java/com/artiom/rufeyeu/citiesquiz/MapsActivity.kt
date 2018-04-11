package com.artiom.rufeyeu.citiesquiz

import android.location.Location
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_maps.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var mScore: Int = 1500
    private var mCityIndex: Int = 0
    private val DISTANCE_ACCURACY: Int = 50000
    private val M_IN_KM: Int = 1000
    private val MAX_SCORE: Int = 1500

    private lateinit var cities: List<City>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        cities = listOf(City("Zurich", 47.223, 8.3230), City("Paris", 48.5000, 2.2000), City("Madrid", 40.4168, -3.7038),
                City("London", 51.3026, 00.0739), City("Berlin", 52.3100, 13.2300), City("Amsterdam", 52.2300, 4.5400),
                City("Rome", 41.5400, 12.3000), City("Oslo", 59.5440, 10.4510), City("Vienna", 48.1300, 16.2224))

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)



        showCorrectAnswersCount()
        showKmLeft()
        showSelectCityQuestion()

    }


    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        val europe = LatLng(48.5, 18.5)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(europe))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(3.5f))




        mMap.setOnMapClickListener({ point -> Toast.makeText(applicationContext, point.toString(), Toast.LENGTH_SHORT).show() })




        mMap.setOnMapClickListener({ point ->
            val (_, latitude, longitude) = cities[mCityIndex]
            val cityLocation = Location("")
            cityLocation.latitude = latitude
            cityLocation.longitude = longitude

            val userSelectedLocation = Location("")
            userSelectedLocation.latitude = point.latitude
            userSelectedLocation.longitude = point.longitude
            val distance = cityLocation.distanceTo(userSelectedLocation)
            if (distance < DISTANCE_ACCURACY) {

                mCityIndex++

            }
            mScore = (mScore - distance / M_IN_KM).toInt()
            if (mCityIndex >= cities.size || mScore < 0) {
                Toast.makeText(this@MapsActivity, getString(R.string.game_is_over_text) + (mCityIndex + 1), Toast.LENGTH_LONG).show()
                startNewGame()
            }

            showCorrectAnswersCount()
            showKmLeft()
            showSelectCityQuestion()
        })


    }

    private fun showKmLeft() {
        tvKmLeft.text = resources.getString(R.string.km_left) + mScore.toString()
    }

    private fun showCorrectAnswersCount() {
        tvShowCorrectAnswers.text = resources.getString(R.string.correct_answers) + (mCityIndex ).toString()
    }

    private fun showSelectCityQuestion() {
        tvSelectLocation.text = resources.getString(R.string.select_location) + cities[mCityIndex].name
    }

    private fun startNewGame() {
        mCityIndex = 0
        mScore = MAX_SCORE
    }


}
