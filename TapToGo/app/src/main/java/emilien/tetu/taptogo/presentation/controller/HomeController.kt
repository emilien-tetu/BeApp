package emilien.tetu.taptogo.presentation.controller

import android.content.Context
import emilien.tetu.taptogo.domain.interactor.HomeInteractor

class HomeController(private val interactor: HomeInteractor) {
    fun getAllStations() {
        interactor.getAllStation()
    }

    fun getStationsWithFilter(nameStation : String?, nbBikeAvailable : Int?, onlyOpenStation : Boolean?) {
        val nameFilter = nameStation?.let { it } ?: run { "" }
        val numberFilter = nbBikeAvailable?.let { it } ?: run { 0 }
        val open = onlyOpenStation?.let { it } ?: run { false }
        interactor.getStationWithFilter(nameFilter,numberFilter,open)
    }

    fun getNavigation(context : Context, departure : String? , arrival : String?){
        interactor.getNavigation(context,departure,arrival)
    }
}