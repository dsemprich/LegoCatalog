package com.example.legocatalog.legoset.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.legocatalog.legoset.data.LegoSetRepository
import com.example.legocatalog.provideDb
import com.example.legocatalog.provideLegoService
import com.example.legocatalog.provideLegoSetDao
import com.example.legocatalog.provideLegoSetRemoteDataSource


/**
 * The ViewModel used in [LegoSetFragment].
 */
class LegoSetViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var id: String
    private val repository = LegoSetRepository(
        provideLegoSetDao(provideDb(application)),
        provideLegoSetRemoteDataSource(provideLegoService())
    )

    val legoSet by lazy { repository.observeSet(id) }

}
