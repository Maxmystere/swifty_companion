package tech.maxdevcoveries.swiftycompanion.lib

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class FortyTwoApiClient {
    private lateinit var apiService: FortyTwoService

    fun getApiService(context: Context): FortyTwoService {

        // Initialize ApiService if not initialized yet
        if (!::apiService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl(FORTY_TWO_BASE_URL)
                .client(okhttpTokenInterceptor(context)) // Add our Okhttp client
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            apiService = retrofit.create(FortyTwoService::class.java)
        }

        return apiService
    }

    /**
     * Initialize OkhttpClient with our interceptor
     */
    private fun okhttpTokenInterceptor(context: Context): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(FortyTwoAuthInterceptor(context))
            .addInterceptor(interceptor)
            .build()
    }
}