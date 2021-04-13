package emilien.tetu.taptogo.domain.interactor

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import emilien.tetu.taptogo.data.api.ApiResponse
import emilien.tetu.taptogo.data.model.JCDecauxStationResponse
import emilien.tetu.taptogo.data.repository.HomeRepository
import emilien.tetu.taptogo.domain.model.StateStation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException
import kotlin.coroutines.CoroutineContext


interface HomePresenter{
    fun presentStations(stationData: List<StationData>)
    fun presentAPIError()
    fun startLoading()
    fun presentNavigationError(message: String)
}

class HomeInteractor(val repository: HomeRepository, val presenter: HomePresenter) {

    private val job: Job = Job()
    private val coroutineContext: CoroutineContext = job + Dispatchers.IO
    private val coroutineScope = CoroutineScope(coroutineContext)

    fun getAllStation() {
        presenter.startLoading()
        coroutineScope.launch {
            runCatching {
                repository.getAllStation()
            }.onSuccess {
                if (it is ApiResponse.Success){
                    presenter.presentStations(it.data.transformToStation())
                }
                else{
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
                if (it is ApiResponse.Success){
                    val listOfStation = it.data.transformToStation()
                    val listOfStationWithFilter : MutableList<StationData> = mutableListOf()
                    listOfStation.forEach { stationData ->
                        if (
                            stationData.name.toUpperCase().contains(nameStation.toUpperCase())
                            && stationData.availableBikes >= numberBike
                            && checkIsOpen(stationData.status, onlyOpen)
                        ){
                            listOfStationWithFilter.add(stationData)
                        }
                    }
                    presenter.presentStations(listOfStationWithFilter)
                }
                else{
                    presenter.presentStations(emptyList())
                }
            }.onFailure {
                presenter.presentAPIError()
            }
        }
    }

    fun getNavigation(context : Context, departure: String?, arrival: String?){
        if (departure == null || arrival == null){
            presenter.presentNavigationError("Departure or arrival address is empty")
        } else {
            presenter.startLoading()
            coroutineScope.launch {
                runCatching {
                    val departurePoint: LatLng? = getLocationFromAddress(context,departure)
                    val arrivalPoint: LatLng? = getLocationFromAddress(context,arrival)
                    repository.getAllStation()
                }.onSuccess {
                    if (it is ApiResponse.Success){
                        val listOfStation = it.data.transformToStation()
                        val stationDepature : StationData? = null
                        val stationArrival : StationData? = null
                        //presenter.presentStations(listOfStationWithFilter)
                    }
                    else{
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

fun checkIsOpen(status: StateStation, onlyOpen: Boolean) : Boolean{
    var bool = true
    when(status){
        StateStation.CLOSE -> {
            bool = !onlyOpen
        }
    }
    return bool
}

fun List<JCDecauxStationResponse>.transformToStation() : List<StationData> {
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

private fun setStatus(isOpen: Boolean, availableBikes: Int, availableBikeStands: Int) : StateStation{
    var status : StateStation = StateStation.OPEN
    if (isOpen){
        if (availableBikes == 0){
            status = StateStation.EMPTY
        }
        else if (availableBikeStands == 0){
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