package emilien.tetu.taptogo.ui.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.android.material.textview.MaterialTextView
import emilien.tetu.taptogo.R
import emilien.tetu.taptogo.presentation.presenter.Station

class CustomInfoWindow(context: Context, private val listItem : List<Station>) : GoogleMap.InfoWindowAdapter {

    var mWindow = (context as Activity).layoutInflater.inflate(R.layout.card_station, null)

    @SuppressLint("SetTextI18n")
    private fun rendowWindowText(marker: Marker, view: View){

        val itemTitle: MaterialTextView = view.findViewById(R.id.title_card)
        val itemAddress: MaterialTextView = view.findViewById(R.id.address_card)
        val itemAvailableBikes: MaterialTextView = view.findViewById(R.id.available_bike_card)
        val itemAvailableBikeStands: MaterialTextView = view.findViewById(R.id.available_stand_bike_card)

        val item = getItemFormMarker(marker, listItem)

        itemTitle.text = item.name
        itemAddress.text = "Address : ${item.address}"
        itemAvailableBikes.text = "Bikes available : ${item.availableBikes}"
        itemAvailableBikeStands.text = "Bikes stand available : ${item.availableBikeStands}"
    }

    private fun getItemFormMarker(marker: Marker, listItem: List<Station>): Station{
        var item = listItem.first()
        listItem.forEach {
            if (marker.title == it.name){
                item = it
            }
        }
        return item
    }

    override fun getInfoContents(marker: Marker): View {
        rendowWindowText(marker, mWindow)
        return mWindow
    }

    override fun getInfoWindow(marker: Marker): View? {
        rendowWindowText(marker, mWindow)
        return mWindow
    }
}