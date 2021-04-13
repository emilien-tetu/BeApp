package emilien.tetu.taptogo.data.repository

import emilien.tetu.taptogo.data.api.ApiResponse
import emilien.tetu.taptogo.data.api.ApiWrapper
import emilien.tetu.taptogo.data.api.JCDecaux
import emilien.tetu.taptogo.data.api.JCDecauxService
import emilien.tetu.taptogo.data.model.JCDecauxStationResponse

class HomeRepository : ApiWrapper {
    suspend fun getAllStation() : ApiResponse<List<JCDecauxStationResponse>> {
        return manageResponse(JCDecaux.getAllStation())
    }
}