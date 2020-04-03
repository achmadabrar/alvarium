package com.bs.ecommerce.networking

import android.util.Log

import com.bs.ecommerce.BuildConfig
import com.google.gson.GsonBuilder
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor

import java.util.concurrent.TimeUnit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors

/**
 * Created by bs156 on 09-Dec-16.
 */

object RetroClient
{

    val api: Api
        get() = client.create(Api::class.java)

    private val client: Retrofit
        get()
        {
            val gson = GsonBuilder().serializeNulls().create()

            val httpClient = OkHttpClient.Builder()
                    .addInterceptor(interceptor)

            httpClient.addInterceptor(
                    LoggingInterceptor.Builder()
                            .loggable(BuildConfig.DEBUG)
                            .setLevel(Level.BASIC)
                            .log(Platform.INFO)
                            .request("LOG_REQUEST")
                            .response("LOG_RESPONSE")
                            .executor(Executors.newSingleThreadExecutor())
                            .build())

            val fullLogger = httpClient.build()

            return Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpLogger)                                       //TODO or fullLogger
                    .build()
        }

    private val okHttpLogger: OkHttpClient
        get()
        {
            val okHttpBuilder = OkHttpClient.Builder()
            okHttpBuilder.readTimeout(60, TimeUnit.SECONDS)
            okHttpBuilder.writeTimeout(60, TimeUnit.SECONDS)
            okHttpBuilder.addInterceptor(interceptor)

            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                okHttpBuilder.addNetworkInterceptor(loggingInterceptor)
            }

            return okHttpBuilder.build()
        }

    private val interceptor: Interceptor
        get() = Interceptor {
            chain ->
            val original = chain.request()
            val builder = original.newBuilder()
            if (NetworkUtil.token.isNotEmpty())
                builder.addHeader("Token", NetworkUtil.token)

            val request = builder
                    .addHeader("Content-Type", "application/json")
                    .addHeader("DeviceId", NetworkUtil.getDeviceId())
                    .addHeader("NST", NetworkUtil.nst)
                    .addHeader("User-Agent", BuildConfig.APPLICATION_ID + "/" + BuildConfig.VERSION_NAME)
                    .method(original.method, original.body)
                    .build()

            if (BuildConfig.DEBUG)
                Log.d("NetworkHeader", "Token: " + request.header("Token") + " DeviceId: " + request.header("DeviceId"))

            chain.proceed(request)
        }

}
