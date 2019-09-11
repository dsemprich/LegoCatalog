package com.example.legocatalog.legoset.data

import com.example.legocatalog.api.BaseDataSource
import com.example.legocatalog.api.LegoService

/**
 * Works with the Lego API to get data.
 */
//@OpenForTesting
class LegoSetRemoteDataSource(private val service: LegoService) : BaseDataSource() {

    suspend fun fetchSets(page: Int, pageSize: Int? = null, themeId: Int? = null)
            = getResult { service.getSets(page, pageSize, themeId, "-year") }

    suspend fun fetchSet(id: String)
            = getResult { service.getSet(id) }
}