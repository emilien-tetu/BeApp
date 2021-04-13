package emilien.tetu.taptogo.domain.interactor

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import emilien.tetu.taptogo.data.api.ApiResponse
import emilien.tetu.taptogo.data.model.JCDecauxStationResponse
import emilien.tetu.taptogo.data.repository.HomeRepository
import emilien.tetu.taptogo.domain.interactor.HomeInteractor.Companion.earthRadiusKm
import emilien.tetu.taptogo.domain.model.StateStation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException
import kotlin.coroutines.CoroutineContext
import kotlin.math.*


interface HomePresenter {
    fun presentStations(stationData: List<StationData>)
    fun presentAPIError()
    fun startLoading()
    fun presentNavigation(stationData: List<StationData>)
    fun presentNavigationError(message: String)
}

class HomeInteractor(val repository: HomeRepository, val presenter: HomePresenter) {

    companion object {
        const val earthRadiusKm: Double = 6372.8
    }

    private val job: Job = Job()
    private val coroutineContext: CoroutineContext = job + Dispatchers.IO
    private val coroutineScope = CoroutineScope(coroutineContext)

    fun getAllStation() {
        presenter.startLoading()
        coroutineScope.launch {
            runCatching {
                repository.getAllStation()
            }.onSuccess {
                if (it is ApiResponse.Success) {
                    presenter.presentStations(it.data.transformToStation())
                } else {
                    presenter.presentStations(emptyList())
                }
            }.onFailure {
                presenter.presentAPIError()
            }
        }
    }

    fun getStationWithFilter(nameStation: String, numberBike: Int, onlyOpen: Boolean) {
        presenter.startLoading()
        coroutineScope.launch {
            runCatching {
                repository.getAllStation()
            }.onSuccess {
                if (it is ApiResponse.Success) {
                    val listOfStation = it.data.transformToStation()
                    val listOfStationWithFilter: MutableList<StationData> = mutableListOf()
                    listOfStation.forEach { stationData ->
                        if (
                            stationData.name.toUpperCase().contains(nameStation.toUpperCase())
                            && stationData.availableBikes >= numberBike
                            && checkIsOpen(stationData.status, onlyOpen)
                        ) {
                            listOfStationWithFilter.add(stationData)
                        }
                    }
                    presenter.presentStations(listOfStationWithFilter)
                } else {
                    presenter.presentStations(emptyList())
                }
            }.onFailure {
                presenter.presentAPIError()
            }
        }
    }

    fun getNavigation(context: Context, departure: String?, arrival: String?) {
        if (departure == null || arrival == null || departure == "" || arrival == "") {
            presenter.presentNavigationError("Departure or arrival address is empty")
        } else {
            presenter.startLoading()
            var departurePoint: LatLng? = null
            var arrivalPoint: LatLng? = null
            coroutineScope.launch {
                runCatching {
                    departurePoint = getLocationFromAddress(context, departure)
                    arrivalPoint = getLocationFromAddress(context, arrival)
                    repository.getAllStation()
                }.onSuccess {
                    if (it is ApiResponse.Success) {
                        val listOfStation = it.data.transformToStation()
                        val stationDeparture: StationData? = getNearestStation(
                            departurePoint,
                            listOfStation.filter { station -> station.availableBikes != 0 })
                        val stationArrival: StationData? = getNearestStation(
                            arrivalPoint,
                            listOfStation.filter { station -> station.availableBikeStands != 0 })
                        if (stationDeparture == null || stationArrival == null) {
                            presenter.presentNavigationError("no stations found")
                        } else if (stationDeparture == stationArrival) {
                            presenter.presentNavigationError("The departure and arrival station are the same")
                        } else {
                            presenter.presentNavigation(listOf(stationDeparture, stationArrival))
                        }
                    } else {
                        presenter.presentStations(emptyList())
                    }
                }.onFailure {
                    presenter.presentAPIError()
                }
            }
        }
    }
}

fun getLocationFromAddress(context: Context?, strAddress: String?): LatLng? {
    val coder = Geocoder(context)
    val address: List<Address>?
    var p1: LatLng? = null
    try {
        address = coder.getFromLocationName(strAddress, 5)
        if (address == null) {
            return null
        }
        val location: Address = address[0]
        p1 = LatLng(location.latitude, location.longitude)
    } catch (ex: IOException) {
        ex.printStackTrace()
    }
    return p1
}

fun getNearestStation(userLatLng: LatLng?, listOfStation: List<StationData>): StationData? {
    var nearestStation: StationData? = null
    var distanceMin = 100.0
    if (userLatLng != null) {
        listOfStation.forEach {
            val latit = it.latitude
            val longit = it.longitude
            val distance = distanceHaversine(userLatLng, LatLng(latit, longit))
            if (distance < distanceMin) {
                nearestStation = it
                distanceMin = distance
            }
        }
    }
    return nearestStation
}

fun distanceHaversine(userLatLng: LatLng, point: LatLng): Double {
    val dLat = Math.toRadians(point.latitude - userLatLng.latitude)
    val dLon = Math.toRadians(point.longitude - userLatLng.longitude)
    val originLat = Math.toRadians(userLatLng.latitude)
    val destinationLat = Math.toRadians(point.latitude)

    val a =
        sin(dLat / 2).pow(2.toDouble()) + sin(dLon / 2).pow(2.toDouble()) * cos(originLat) * cos(
            destinationLat
        );
    val c = 2 * asin(sqrt(a))
    return earthRadiusKm * c
}

fun checkIsOpen(status: StateStation, onlyOpen: Boolean): Boolean {
    return when (status) {
        StateStation.CLOSE -> {
            !onlyOpen
        }
        else -> true
    }
}

fun List<JCDecauxStationResponse>.transformToStation(): List<StationData> {
    return this.map {
        StationData(
            number = it.number,
            name = it.name,
            address = it.address,
            latitude = it.position.latitude,
            longitude = it.position.longitude,
            status = setStatus(it.status == "OPEN", it.availableBikes, it.availableBikeStands),
            availableBikes = it.availableBikes,
            availableBikeStands = it.availableBikeStands
        )
    }
}

private fun setStatus(
    isOpen: Boolean,
    availableBikes: Int,
    availableBikeStands: Int
): StateStation {
    var status: StateStation = StateStation.OPEN
    if (isOpen) {
        if (availableBikes == 0) {
            status = StateStation.EMPTY
        } else if (availableBikeStands == 0) {
            status = StateStation.FULL
        }
    } else {
        status = StateStation.CLOSE
    }
    return status
}

data class StationData(
    val number: Int,
    val name: String = "",
    val address: String = "",
    val latitude: Double,
    val longitude: Double,
    val status: StateStation,
    val availableBikeStands: Int,
    val availableBikes: Int,
)