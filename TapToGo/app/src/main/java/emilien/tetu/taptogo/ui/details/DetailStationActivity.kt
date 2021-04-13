package emilien.tetu.taptogo.ui.details

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.textview.MaterialTextView
import emilien.tetu.taptogo.R
import emilien.tetu.taptogo.domain.model.StateStation
import emilien.tetu.taptogo.domain.model.toStringStatus
import emilien.tetu.taptogo.presentation.presenter.Station

class DetailStationActivity : AppCompatActivity() {

    companion object {
        const val NAME_STATION = "NAME_STATION"
        const val ADDRESS_STATION = "ADDRESS_STATION"
        const val BIKE_STATION = "BIKE_STATION"
        const val BIKE_STAND_STATION = "BIKE_STAND_STATION"
        const val STATUS_STATION = "STATUS_STATION"
        const val LATITUDE_STATION = "LATITUDE_STATION"
        const val LONGITUDE_STATION = "LONGITUDE_STATION"

        const val NAME = "NAME : "
        const val ADDRESS = "ADDRESS : "
        const val STATUS = "STATUS : "
        const val LONGITUDE = "LONGITUDE : "
        const val LATITUDE = "LATITUDE : "
        const val BIKE = "BIKES AVAILABLE : "
        const val BIKE_STAND = "BIKES STAND AVAILABLE : "
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_station)

        val nameStation = intent.getStringExtra(NAME_STATION)
        val addressStation = intent.getStringExtra(ADDRESS_STATION)
        val bikeStation = intent.getIntExtra(BIKE_STATION,0)
        val bikeStandStation = intent.getIntExtra(BIKE_STAND_STATION,0)
        val statusStation = intent.getStringExtra(STATUS_STATION)
        val latitudeStation = intent.getDoubleExtra(LATITUDE_STATION,0.0)
        val longitudeStation = intent.getDoubleExtra(LONGITUDE_STATION,0.0)

        val name = findViewById<MaterialTextView>(R.id.detailName)
        name.text = NAME+nameStation
        val address = findViewById<MaterialTextView>(R.id.detailAddress)
        address.text = ADDRESS+addressStation
        val bikes = findViewById<MaterialTextView>(R.id.detailBikeAvailable)
        bikes.text = BIKE+bikeStation.toString()
        val bikesStand = findViewById<MaterialTextView>(R.id.detailBikeStandAvailable)
        bikesStand.text = BIKE_STAND+bikeStandStation.toString()
        val status = findViewById<MaterialTextView>(R.id.detailOpen)
        status.text = STATUS+statusStation
        val longitude = findViewById<MaterialTextView>(R.id.detailLongitude)
        longitude.text = LONGITUDE+longitudeStation.toString()
        val latitude = findViewById<MaterialTextView>(R.id.detailLatitude)
        latitude.text = LATITUDE+latitudeStation.toString()
    }
}