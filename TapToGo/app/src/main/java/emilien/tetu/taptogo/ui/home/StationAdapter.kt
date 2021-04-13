package emilien.tetu.taptogo.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import emilien.tetu.taptogo.R
import emilien.tetu.taptogo.domain.model.StateStation
import emilien.tetu.taptogo.presentation.presenter.Station
import java.lang.ref.WeakReference

interface StationListener{
    fun onClickListener(
        stationName: String,
        stationStatus: StateStation,
        stationLatLng: LatLng
    )
    fun onClickOnInfo(station : Station)
}

class StationAdapter(
    private val stations: MutableList<Station>, private var listener: StationListener
) : RecyclerView.Adapter<StationAdapter.ViewHolder>() {

    inner class ViewHolder(
            view: View,
            listener: StationListener?
    ) : RecyclerView.ViewHolder(view) {

        private val itemTitle: MaterialTextView = view.findViewById(R.id.title_card)
        private val itemAddress: MaterialTextView = view.findViewById(R.id.address_card)
        private val itemAvailableBikes: MaterialTextView = view.findViewById(R.id.available_bike_card)
        private val itemAvailableBikeStands: MaterialTextView = view.findViewById(R.id.available_stand_bike_card)
        private val itemDetail: ImageButton = view.findViewById(R.id.detail)
        private val listenerReference: WeakReference<StationListener?> = WeakReference(listener)

        init {
            view.setOnClickListener {
                listenerReference.get()?.onClickListener(
                    stationName = stations[adapterPosition].name,
                    stationStatus = stations[adapterPosition].status,
                    stationLatLng = LatLng(stations[adapterPosition].latitude,stations[adapterPosition].longitude)
                )
            }
            itemDetail.setOnClickListener {
                listenerReference.get()?.onClickOnInfo(stations[adapterPosition])
            }
        }

        @SuppressLint("SetTextI18n")
        fun bindStation(){
            itemDetail.visibility = View.VISIBLE
            itemTitle.text = stations[adapterPosition].name
            itemAddress.text = "Address : ${stations[adapterPosition].address}"
            itemAvailableBikes.text = "Bikes available : ${stations[adapterPosition].availableBikes}"
            itemAvailableBikeStands.text = "Bikes stands available : ${stations[adapterPosition].availableBikeStands}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_station, parent, false)
        return ViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: StationAdapter.ViewHolder, position: Int) {
        holder.bindStation()
    }

    override fun getItemCount(): Int = stations.count()

}