package emilien.tetu.taptogo.data.model

import com.squareup.moshi.Json

data class JCDecauxStationResponse(
    @field:Json(name = "number") val number: Int,
    @field:Json(name = "name") val name: String = "",
    @field:Json(name = "address") val address: String = "",
    @field:Json(name = "position") val position: JCDecauxPositionResponse,
    @field:Json(name = "status") val status: String,
    @field:Json(name = "available_bike_stands") val availableBikeStands: Int,
    @field:Json(name = "available_bikes") val availableBikes: Int,
)

data class JCDecauxPositionResponse(
    @field:Json(name = "lat") val latitude: Double,
    @field:Json(name = "lng") val longitude: Double,
)