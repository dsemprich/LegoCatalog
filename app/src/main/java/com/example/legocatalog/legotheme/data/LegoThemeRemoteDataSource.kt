package com.example.legocatalog.legotheme.data

import com.example.legocatalog.api.BaseDataSource
import com.example.legocatalog.api.LegoService

/**
 * Works with the Lego API to get data.
 */
class LegoThemeRemoteDataSource(private val service: LegoService) : BaseDataSource() {

    suspend fun fetchData() = getResult { service.getThemes(1, 1000, "-id") }

}