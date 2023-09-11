package com.project.toko.presentation.screens.detailScreen.vm

import com.project.toko.repository.MalApiService
import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.toko.domain.models.cache.DataCacheSingleton
import com.project.toko.domain.models.newAnimeSearchModel.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailScreenViewModel(private val malApiService: MalApiService) :
    ViewModel() {

    //detailData
    private val _animeDetails = MutableStateFlow<Data?>(null)
    val animeDetails: StateFlow<Data?> get() = _animeDetails
    private val _loadedId = mutableStateOf(0)
    val loadedId = _loadedId
    private val animeCache = DataCacheSingleton.dataCache
    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private val _scrollState: ScrollState by mutableStateOf(ScrollState(0))
    var scrollState = _scrollState

    private val _previousId = MutableStateFlow(0)
    var previousId = _previousId

    suspend fun onTapAnime(id: Int) {

        viewModelScope.launch(Dispatchers.IO) {

            // Сохраните предыдущее id
            val previousId = _loadedId.value
            if (previousId != id) {
                _previousId.value = previousId
            }

            if (animeCache.containsId(id)) {
                _animeDetails.value = animeCache.getData(id)
                return@launch
            }
            try {
                _isSearching.value = true
                val response = malApiService.getDetailsFromAnime(id)
                val data = response.data
                _loadedId.value = id
                withContext(Dispatchers.Main) {
                    _animeDetails.value = data
                    animeCache.setData(id, data)
                }
            } catch (e: Exception) {
                Log.e("DetailScreenViewModel", e.message.toString())
            } finally {
                _isSearching.value = false
            }
        }
    }


    //staff
    private val staffCache =
        mutableMapOf<Int, List<com.project.toko.domain.models.staffModel.Data>>()
    private val _staffList =
        MutableStateFlow<List<com.project.toko.domain.models.staffModel.Data>>(emptyList())
    val staffList = _staffList.asStateFlow()

    fun addStaffFromId(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val cachedStaff = staffCache[id]
            if (cachedStaff != null) {
                _staffList.value = cachedStaff
                return@launch
            }

            try {
                viewModelScope.launch(Dispatchers.IO) {
                    val response = malApiService.getStaffFromId(id)
                    val staff = response.data
                    staffCache[id] = staff
                    _staffList.value = staff
                }
            } catch (e: Exception) {
                Log.e("StaffViewModel", e.message.toString())
            } finally {
                if (_staffList.value.isEmpty()) {
                    _staffList.value = emptyList()
                }
            }
        }
    }


    // cast
    private val castCache =
        mutableMapOf<Int, List<com.project.toko.domain.models.castModel.Data>>()
    private val _castList =
        MutableStateFlow<List<com.project.toko.domain.models.castModel.Data>>(emptyList())
    val castList = _castList.asStateFlow()

    fun addCastFromId(id: Int) {
        val cachedCharacters = castCache[id]
        if (cachedCharacters != null) {
            _castList.value = cachedCharacters
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = malApiService.getCharactersFromId(id)

                val characters = response.data
                castCache[id] = characters
                _castList.value = characters
            } catch (e: Exception) {
                Log.e("CastInDetailScreenVM", e.message.toString())
                // если произошла ошибка, присваиваем пустой список
                _castList.value = emptyList()
            }

        }
    }
}
