package emilien.tetu.taptogo.data.api

import emilien.tetu.taptogo.data.model.JCDecauxStationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface IJCDecaux {
    @GET("stations")
    suspend fun getAllStation(
        @Query("apiKey") apiKey: String,
        @Query("contract") contractName: String,
    ): Response<List<JCDecauxStationResponse>>
}

object JCDecaux {

    private val apiKey = "2590dc856231c0ab710a781c9277e68dd73cda6a"

    suspend fun getAllStation(): Response<List<JCDecauxStationResponse>> {
        return JCDecauxService.createRestService(IJCDecaux::class.java)
            .getAllStation(apiKey = apiKey, contractName = "nantes")
    }
}