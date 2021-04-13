package emilien.tetu.taptogo.data.api

import com.squareup.moshi.Moshi
import emilien.tetu.taptogo.data.model.JCDecauxStationResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

object JCDecauxAPi {

    private val apiKey = "2590dc856231c0ab710a781c9277e68dd73cda6a"
    private val TIMEOUT = 30L
    private val BASE_URL = "https://api.jcdecaux.com/vls/v1/";

    private fun <T> createRestService(
            service: Class<T>
    ): T {
        val httpInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder().apply {
            addInterceptor(httpInterceptor)
            connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            readTimeout(TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(TIMEOUT, TimeUnit.SECONDS)
        }.build()

        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
                .build()
                .create(service)
    }

    suspend fun getAllStation(): Response<List<JCDecauxStationResponse>> {
        return createRestService(JCDecauxService::class.java)
                .getAllStation(apiKey = apiKey, contractName = "nantes")
    }
}


interface JCDecauxService {
    @GET("stations")
    suspend fun getAllStation(
        @Query("apiKey") apiKey: String,
        @Query("contract") contractName: String,
    ): Response<List<JCDecauxStationResponse>>
}