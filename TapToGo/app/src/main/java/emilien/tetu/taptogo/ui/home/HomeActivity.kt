package emilien.tetu.taptogo.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import emilien.tetu.taptogo.R
import emilien.tetu.taptogo.databinding.ActivityHomeBinding
import emilien.tetu.taptogo.domain.model.StateStation
import emilien.tetu.taptogo.domain.model.toStringStatus
import emilien.tetu.taptogo.presentation.controller.HomeController
import emilien.tetu.taptogo.presentation.presenter.Station
import emilien.tetu.taptogo.presentation.viewmodel.*
import emilien.tetu.taptogo.ui.StationAdapter
import emilien.tetu.taptogo.ui.StationListener
import emilien.tetu.taptogo.ui.details.DetailStationActivity
import emilien.tetu.taptogo.ui.extensions.addMarker
import emilien.tetu.taptogo.ui.extensions.moveTo
import emilien.tetu.taptogo.ui.filter.FilterActivity
import emilien.tetu.taptogo.ui.navigation.NavigationActivity
import org.koin.android.ext.android.inject


class HomeActivity : AppCompatActivity(), OnMapReadyCallback, StationListener {

    companion object {
        private val NANTES = LatLng(47.2173, -1.5534)
        private val REQUEST_CODE_FILTER = 1
        private val REQUEST_CODE_NAV = 2
    }

    private var mGoogleMap: GoogleMap? = null
    private var listOfStation : MutableList<Station> = mutableListOf()
    private var listOfStationSave : MutableList<Station> = mutableListOf()

    private val controller: HomeController by inject()
    private val viewModel: HomeViewModel by inject()

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //setContentView(R.layout.activity_home)

        viewModel.state.observe(this, { updateUI(it) })

        controller.getAllStations()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        val viewAdapter = StationAdapter(listOfStation, this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.recyclerView.adapter = viewAdapter

        binding.homeSearch.doOnTextChanged { text, _, _, _ ->
            refreshData(getSearchResult(text, listOfStation).sortedBy { it.name })
        }

        binding.homeFilterButton.setOnClickListener {
            startActivityForResult(Intent(this, FilterActivity::class.java), REQUEST_CODE_FILTER)
        }

        binding.homeRefreshButton.setOnClickListener {
            listOfStation.clear()
            refreshData(listOfStation)
            mGoogleMap?.clear()
            controller.getAllStations()
        }

        binding.homeNavigation.setOnClickListener {
            startActivityForResult(Intent(this, NavigationActivity::class.java), REQUEST_CODE_NAV)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            REQUEST_CODE_FILTER -> {
                if (resultCode == RESULT_OK) {
                    val nameStation = data?.getStringExtra(FilterActivity.NAME_STATION)
                    val numberBikeAvailable = data?.getIntExtra(FilterActivity.BIKE_AVAILABLE,0)
                    val onlyOpenStation = data?.getBooleanExtra(FilterActivity.ONLY_OPEN, false)
                    controller.getStationsWithFilter(nameStation, numberBikeAvailable, onlyOpenStation)
                }
            }
            REQUEST_CODE_NAV -> {
                if (resultCode == RESULT_OK) {
                    val departureAddress = data?.getStringExtra(NavigationActivity.DEPARTURE_ADDRESS)
                    val arrivalAddress = data?.getStringExtra(NavigationActivity.ARRIVAL_ADDRESS)
                    controller.getNavigation(this,departureAddress, arrivalAddress)
                }
            }
        }
    }

    private fun getSearchResult(text: CharSequence?, listStation: List<Station>) : MutableList<Station>{
        val list : MutableList<Station> = mutableListOf()
        text?.let {
            listStation.forEach { station ->
                if (station.name.contains(it.toString().toUpperCase())){
                    list.add(station)
                }
            }
        }
        return list
    }

    private fun updateUI(state: HomeState) {
        Log.d("DEBUG", "state = ${state::class.java.simpleName}")
        when (state) {
            is LoadDataError -> {
                binding.homeProgressBar.visibility = View.GONE
                Snackbar.make(findViewById(android.R.id.content),"Something append, retry later..",Snackbar.LENGTH_LONG).show()
            }
            is LoadStation -> {
                binding.homeProgressBar.visibility = View.GONE
                listOfStation = state.stations.toMutableList()
                listOfStationSave = state.stations.toMutableList()
                addStations(state.stations)
            }
            is LoadNavigationStation -> {
                binding.homeProgressBar.visibility = View.GONE
                listOfStation = state.stations.toMutableList()
                addNavigationStations(state.stations.first(),state.stations.last())
            }
            is Loading -> {
                mGoogleMap?.clear()
                binding.homeProgressBar.visibility = View.VISIBLE
            }
            is LoadNavigationError -> {
                binding.homeProgressBar.visibility = View.GONE
                Snackbar.make(findViewById(android.R.id.content),state.message,Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun addStations(listOfStation: List<Station>){
        listOfStation.forEach {
            mGoogleMap?.addMarker(LatLng(it.latitude, it.longitude), it.name, it.status)
        }
        mGoogleMap?.setInfoWindowAdapter(CustomInfoWindow(this, listOfStation))
        refreshData(listOfStation.sortedBy { it.name })
    }

    private fun addNavigationStations(departureStation: Station, arrivalStation: Station){
        mGoogleMap?.addMarker(LatLng(departureStation.latitude, departureStation.longitude), departureStation.name, StateStation.OPEN)
        mGoogleMap?.addMarker(LatLng(arrivalStation.latitude, arrivalStation.longitude), arrivalStation.name,  StateStation.CLOSE)
        val listStation = listOf(departureStation,arrivalStation)
        mGoogleMap?.setInfoWindowAdapter(CustomInfoWindow(this, listStation))
        refreshData(listStation.sortedBy { it.name })
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mGoogleMap = googleMap
        mGoogleMap?.moveTo(NANTES, 13f)
    }

    private fun refreshData(stations: List<Station>) {
        binding.recyclerView.adapter = StationAdapter(stations.toMutableList(), this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onClickListener(
        stationName: String,
        stationStatus: StateStation,
        stationLatLng: LatLng
    ) {
        mGoogleMap?.moveTo(stationLatLng, 16f)
        val marker = mGoogleMap?.addMarker(stationLatLng, stationName, stationStatus)
        marker?.showInfoWindow()
        binding.homeSearch.clearFocus()
        binding.homeSearch.setText("")
    }

    override fun onClickOnInfo(station: Station) {
        val intent = Intent(this,DetailStationActivity::class.java)
        intent.putExtra(DetailStationActivity.NAME_STATION,station.name)
        intent.putExtra(DetailStationActivity.ADDRESS_STATION,station.address)
        intent.putExtra(DetailStationActivity.BIKE_STATION,station.availableBikes)
        intent.putExtra(DetailStationActivity.BIKE_STAND_STATION,station.availableBikeStands)
        intent.putExtra(DetailStationActivity.STATUS_STATION,station.status.toStringStatus())
        intent.putExtra(DetailStationActivity.LATITUDE_STATION,station.latitude)
        intent.putExtra(DetailStationActivity.LONGITUDE_STATION,station.longitude)
        startActivity(intent)
    }
}