package com.project.toko.domain.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class FavoriteScreenViewModel :
    ViewModel() {
    private val _searchQuery = mutableStateOf("")
    val searchQuery = _searchQuery
}