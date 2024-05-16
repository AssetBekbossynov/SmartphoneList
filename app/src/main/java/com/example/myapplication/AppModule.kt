package com.example.myapplication

import android.content.Context
import com.example.myapplication.data.localSource.LocalDatabase
import com.example.myapplication.data.repository.SmartphoneRepository
import com.example.myapplication.data.service.ApiService
import com.example.myapplication.ui.screens.smartphoneList.MainScreenViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun provideHttpClient(): OkHttpClient = OkHttpClient
    .Builder()
    .addInterceptor(
        HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    )
    .build()

val networkModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://www.mechta.kz/api/v2/")
            .client(provideHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

val databaseModule = module {
    single { LocalDatabase(androidContext().getSharedPreferences("my_pref", Context.MODE_PRIVATE)) }
}

val repositoryModule = module {
    single { SmartphoneRepository(get(), get()) }
}

val viewModelModule = module {
    viewModel { MainScreenViewModel(get()) }
}