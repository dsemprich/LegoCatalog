package com.example.legocatalog

import android.app.Application
import com.example.legocatalog.api.AuthInterceptor
import com.example.legocatalog.api.LegoService
import com.example.legocatalog.data.AppDatabase
import com.example.legocatalog.legoset.data.LegoSetRemoteDataSource
import com.example.legocatalog.legotheme.data.LegoThemeRemoteDataSource
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE

fun provideLegoService() = provideLegoService(
    providePrivateOkHttpClient(provideOkHttpClient(provideLoggingInterceptor())),
    provideGsonConverterFactory(provideGson())
)

fun provideLegoService(okhttpClient: OkHttpClient,
                       converterFactory: GsonConverterFactory
) = provideService(okhttpClient, converterFactory, LegoService::class.java)

fun provideLegoSetRemoteDataSource(legoService: LegoService)
        = LegoSetRemoteDataSource(legoService)

fun provideLegoThemeRemoteDataSource(legoService: LegoService)
        = LegoThemeRemoteDataSource(legoService)

fun providePrivateOkHttpClient(
    upstreamClient: OkHttpClient
): OkHttpClient {
    return upstreamClient.newBuilder()
        .addInterceptor(AuthInterceptor(BuildConfig.API_DEVELOPER_TOKEN)).build()
}

fun provideDb(app: Application) = AppDatabase.getInstance(app)

fun provideLegoSetDao(db: AppDatabase) = db.legoSetDao()

fun provideLegoThemeDao(db: AppDatabase) = db.legoThemeDao()

fun provideCoroutineScopeIO() = CoroutineScope(Dispatchers.IO)

fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient =
    OkHttpClient.Builder().addInterceptor(interceptor)
        .addNetworkInterceptor(StethoInterceptor())
        .build()

fun provideLoggingInterceptor() =
HttpLoggingInterceptor().apply { level = if (BuildConfig.DEBUG) BODY else NONE }

fun provideGson(): Gson = Gson()

fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory =
    GsonConverterFactory.create(gson)


private fun createRetrofit(
    okhttpClient: OkHttpClient,
    converterFactory: GsonConverterFactory
): Retrofit {
    return Retrofit.Builder()
        .baseUrl(LegoService.ENDPOINT)
        .client(okhttpClient)
        .addConverterFactory(converterFactory)
        .build()
}

private fun <T> provideService(okhttpClient: OkHttpClient,
                               converterFactory: GsonConverterFactory, clazz: Class<T>): T {
    return createRetrofit(okhttpClient, converterFactory).create(clazz)
}

