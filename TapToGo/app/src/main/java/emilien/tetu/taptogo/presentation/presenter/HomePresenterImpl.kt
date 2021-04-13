package emilien.tetu.taptogo.presentation.presenter

import emilien.tetu.taptogo.domain.interactor.HomePresenter
import emilien.tetu.taptogo.domain.interactor.StationData
import emilien.tetu.taptogo.domain.model.StateStation
import emilien.tetu.taptogo.presentation.viewmodel.HomeViewModel

class HomePresenterImpl(val viewModel: HomeViewModel) : HomePresenter {
    override fun presentStations(stationData: List<StationData>) {
        viewModel.displayStation(stationData.transformToStation())
    }

    override fun presentAPIError() {
        viewModel.displayError()
    }

    override fun startLoading() {
        viewModel.displayLoading()
    }

    override fun presentNavigation(stationData: List<StationData>) {
        viewModel.displayNavigationStation(stationData.transformToStation())
    }

    override fun presentNavigationError(message: String) {
        viewModel.displayNavigationError(message)
    }
}

fun List<StationData>.transformToStation() : List<Station> {
    return this.map {
        Station(
            number = it.number,
            name = it.name,
            address = it.address,
            latitude = it.latitude,
            longitude = it.longitude,
            status = it.status,
            availableBikes = it.availableBikes,
            availableBikeStands = it.availableBikeStands
        )
    }
}

data class Station(
    val number: Int,
    val name: String = "",
    val address: String = "",
    val latitude: Double,
    val longitude: Double,
    val status: StateStation,
    val availableBikeStands: Int,
    val availableBikes: Int,
)