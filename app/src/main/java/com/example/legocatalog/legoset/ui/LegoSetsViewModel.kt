package com.example.legocatalog.legoset.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.legocatalog.*
import com.example.legocatalog.legoset.data.LegoSetRepository
import com.example.legocatalog.legotheme.data.LegoThemeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

/**
 * The ViewModel for [LegoSetsFragment].
 */
class LegoSetsViewModel(application: Application) : AndroidViewModel(application) {

    var connectivityAvailable: Boolean = false
    var themeId: Int? = null
    private val repository = LegoSetRepository(
        provideLegoSetDao(provideDb(application)),
        provideLegoSetRemoteDataSource(provideLegoService())
    )
    private val ioCoroutineScope = provideCoroutineScopeIO()

    val legoSets by lazy {
        repository.observePagedSets(
            connectivityAvailable, themeId, ioCoroutineScope)
    }

    /**
     * Cancel all coroutines when the ViewModel is cleared.
     */
    override fun onCleared() {
        super.onCleared()
        ioCoroutineScope.cancel()
    }
}
