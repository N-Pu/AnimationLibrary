package com.project.toko.presentation.screens.favoritesScreen.vm

import android.content.Context
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.project.toko.dao.AnimeItem
import com.project.toko.dao.MainDb
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class DaoViewModel(context: Context) : ViewModel() {

    private val db = MainDb.getDb(context).getDao()

    private val _searchQuery = mutableStateOf("")
    val searchQuery = _searchQuery


    //    private var _scrollState by mutableStateOf(LazyGridState(0,0))
    private val _scrollState = MutableStateFlow(LazyGridState(0, 0))
    var scrollState = _scrollState

    suspend fun addToFavorites(animeItem: AnimeItem) {
        db.addToFavorites(animeItem)
    }

    fun containsInDataBase(id: Int): Flow<Boolean> {
        return db.containsInDataBase(id)
    }

    fun isDataBaseEmpty(): Flow<Boolean> {
        return db.isDataBaseEmpty()
    }

    suspend fun removeAll() {
        db.removeAll()
    }

    fun searchAnime(searchQuery: String?): Flow<List<AnimeItem>> {
        return db.searchAnime(searchQuery)
    }

    suspend fun removeFromDataBase(id: Int) {
        db.removeFromDataBase(id)
    }


}