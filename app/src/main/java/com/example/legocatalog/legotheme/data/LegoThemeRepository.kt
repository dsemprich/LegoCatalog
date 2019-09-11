package com.example.legocatalog.legotheme.data

import com.example.legocatalog.data.resultLiveData


class LegoThemeRepository(
    private val dao: LegoThemeDao,
    private val remoteSource: LegoThemeRemoteDataSource) {

    val themes = resultLiveData(
        databaseQuery = { dao.getLegoThemes() },
        networkCall = { remoteSource.fetchData() },
        saveCallResult = { dao.insertAll(it.results) })

}
