package emilien.tetu.taptogo.presentation.controller

import android.content.Context
import emilien.tetu.taptogo.domain.interactor.HomeInteractor

class HomeController(private val interactor: HomeInteractor) {
    fun getAllStations() {
        interactor.getAllStation()
    }

    fun getStationsWithFilter(nameStation : String?, nbBikeAvailable : Int?, onlyOpenStation : Boolean?) {
        val nameFilter = nameStation ?: ""
        val numberFilter = nbBikeAvailable ?: 0
        val open = onlyOpenStation ?: false
        interactor.getStationWithFilter(nameFilter,numberFilter,open)
    }

    fun getNavigation(context : Context, departure : String? , arrival : String?){
        interactor.getNavigation(context,departure,arrival)
    }
}