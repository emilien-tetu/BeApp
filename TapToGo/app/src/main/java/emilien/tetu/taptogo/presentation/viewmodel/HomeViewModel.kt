package emilien.tetu.taptogo.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import emilien.tetu.taptogo.presentation.presenter.Station

class HomeViewModel : ViewModel() {
    var state = MutableLiveData<HomeState>()

    fun displayStation(list: List<Station>){
        state.postValue(LoadStation(list))
    }

    fun displayError(){
        state.postValue(LoadDataError)
    }

    fun displayLoading(){
        state.postValue(Loading)
    }
}

sealed class HomeState
data class LoadStation(val stations: List<Station>) : HomeState()
object LoadDataError: HomeState()
object Loading: HomeState()