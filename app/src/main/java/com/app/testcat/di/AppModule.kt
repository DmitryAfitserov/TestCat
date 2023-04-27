package com.app.testcat.di

import androidx.room.Room
import com.app.testcat.api.ApiCat
import com.app.testcat.api.DaoCat
import com.app.testcat.app.CatApplication
import com.app.testcat.di.room.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    // ----- retrofit ---------------

    @Provides
    @Singleton
    fun challengeRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/v1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun httpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun httpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor { chain: Interceptor.Chain ->
                val requestBuilder = chain.request().newBuilder()

                //  requestBuilder.addHeader("Authorization", "Bearer ktjqe7PluMIIuQJ9EQRR96wxkEu4gSaVKKZi2zQcbjGfhaK4T7rXGdjWbQSyiTQg")

                val response = chain.proceed(requestBuilder.build())

                response
            }
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    fun provideCatApi(retrofit: Retrofit): ApiCat =
        retrofit.create(ApiCat::class.java)


    // ---------------  Room -----------

    @Provides
    @Singleton
    fun challengeRoom(): AppDatabase =
        Room.databaseBuilder(
            CatApplication.instance,
            AppDatabase::class.java, "database"
        ).build()

    @Provides
    @Singleton
    fun provideCatDao(appDatabase: AppDatabase): DaoCat =
        appDatabase.userDao()


}