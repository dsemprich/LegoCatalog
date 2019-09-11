package com.example.legocatalog.legotheme.ui

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.legocatalog.legotheme.data.LegoThemeRepository
import com.example.legocatalog.provideDb
import com.example.legocatalog.provideLegoService
import com.example.legocatalog.provideLegoThemeDao
import com.example.legocatalog.provideLegoThemeRemoteDataSource

/**
 * The ViewModel for [LegoThemeFragment].
 */
class LegoThemeViewModel(@NonNull application: Application) : AndroidViewModel(application) {
    val legoThemes= LegoThemeRepository(
        provideLegoThemeDao(provideDb(application)),
        provideLegoThemeRemoteDataSource(provideLegoService())
    ).themes
}