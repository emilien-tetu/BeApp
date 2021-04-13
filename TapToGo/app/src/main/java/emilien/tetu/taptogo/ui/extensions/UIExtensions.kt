package emilien.tetu.taptogo.ui.extensions

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import emilien.tetu.taptogo.domain.model.StateStation

fun GoogleMap?.addMarker(latLng : LatLng, title : String, status : StateStation) : Marker?{
    val iconFull = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
    val iconOpen = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
    val iconClose = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
    val iconEmpty = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
    var marker : Marker? = null
    this?.apply {
        when(status){
            StateStation.OPEN -> marker = addMarker(
                MarkerOptions().position(latLng).title(title).icon(iconOpen)
            )
            StateStation.CLOSE -> marker = addMarker(
                MarkerOptions().position(latLng).title(title).icon(iconClose)
            )
            StateStation.FULL -> marker = addMarker(
                MarkerOptions().position(latLng).title(title).icon(iconFull)
            )
            StateStation.EMPTY -> marker = addMarker(
                MarkerOptions().position(latLng).title(title).icon(iconEmpty)
            )
        }
    }
    return marker
}

fun GoogleMap?.moveTo(latLng : LatLng, zoom : Float){
    this?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
}