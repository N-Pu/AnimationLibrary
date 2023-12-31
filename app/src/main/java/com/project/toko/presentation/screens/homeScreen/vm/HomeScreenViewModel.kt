package com.project.toko.presentation.screens.homeScreen.vm

import com.project.toko.repository.MalApiService
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.toko.domain.models.cache.DataCacheSingleton
import com.project.toko.domain.models.linkChangeModel.Genre
import com.project.toko.domain.models.linkChangeModel.OrderBy
import com.project.toko.domain.models.linkChangeModel.Rating
import com.project.toko.domain.models.linkChangeModel.Score
import com.project.toko.domain.models.linkChangeModel.Types
import com.project.toko.domain.models.linkChangeModel.getGenres
import com.project.toko.domain.models.linkChangeModel.getOrderBy
import com.project.toko.domain.models.linkChangeModel.getRating
import com.project.toko.domain.models.linkChangeModel.getTypes
import com.project.toko.domain.models.newAnimeSearchModel.Data
import com.project.toko.domain.models.newAnimeSearchModel.Items
import com.project.toko.domain.models.newAnimeSearchModel.NewAnimeSearchModel
import com.project.toko.domain.models.newAnimeSearchModel.Pagination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


@OptIn(FlowPreview::class)
class HomeScreenViewModel(private val malApiRepository: MalApiService) : ViewModel() {

    private var _isDropdownVisible = mutableStateOf(false)
    var isDropdownMenuVisible = _isDropdownVisible

    private val animeCache = DataCacheSingleton.dataCache

    private val emptyItem = Items(0, 0, 0)
    private val emptyNewAnimeSearchModel = NewAnimeSearchModel(
        data = ArrayList(), pagination = Pagination(false, emptyItem, 0)
    )

    private val _isPerformingSearch = MutableStateFlow(false)
    val isPerformingSearch = _isPerformingSearch.asStateFlow()

    private val _currentPage = MutableStateFlow(1)
    val currentPage = _currentPage.asStateFlow()


    private val _isNextPageLoading = MutableStateFlow(false)
    val isNextPageLoading = _isNextPageLoading.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _animeSearch = MutableStateFlow(emptyNewAnimeSearchModel)
    val animeSearch = _animeSearch.asStateFlow()

    private val searchDebouncer = MutableSharedFlow<String>()

    private var hasNextPage = MutableStateFlow(true)


    // _____________________________________________________________________ //

    //link parameters section

    private val arrayOfGenres = MutableStateFlow(arrayListOf<Int>())

    private val preSelectedGenre = MutableStateFlow(getGenres())
    val selectedGenre: MutableStateFlow<List<Genre>> = preSelectedGenre


    private var pre_genres = ""
    private val _genres = MutableStateFlow("")

    private val _type = MutableStateFlow("")
    val type = _type.value


    private val _ratingList = MutableStateFlow(getRating())
    val ratingList: StateFlow<List<Rating>> = _ratingList


    private val preSelectedRating = MutableStateFlow<Rating?>(null)
    val selectedRating: StateFlow<Rating?> = preSelectedRating

    private val _selectedRating = MutableStateFlow<Rating?>(null)
    fun setSelectedRating(rating: Rating) {
        preSelectedRating.value = if (rating == preSelectedRating.value) null else rating
    }


    private val pre_min_score = MutableStateFlow<Score?>(null)
    private val _min_score = MutableStateFlow<Score?>(null)


    fun setSelectedMinScore(score: Score) {
        pre_min_score.value = if (score == pre_min_score.value) null else score
    }

    private val pre_max_score = MutableStateFlow<Score?>(null)
    private val _max_score = MutableStateFlow<Score?>(null)

    fun setSelectedMaxScore(score: Score) {
        pre_max_score.value = if (score == pre_max_score.value) null else score
    }

    private val preSelectedMinMax = mutableStateOf(false)
    val selectedMinMax: MutableState<Boolean> = preSelectedMinMax

    private val _scoreState = mutableStateOf(0)
    val scoreState = _scoreState


    private val _safeForWork = mutableStateOf(false)
    var safeForWork = _safeForWork

    private val _typeList = MutableStateFlow(getTypes())
    val typeList: StateFlow<List<Types>> = _typeList
    private val pre_selectedType = MutableStateFlow<Types?>(null)
    val selectedType: StateFlow<Types?> = pre_selectedType
    private val _selectedType = MutableStateFlow<Types?>(null)
    fun setSelectedType(type: Types) {
        pre_selectedType.value = if (type == pre_selectedType.value) null else type
    }

    private val _orderByList = MutableStateFlow(getOrderBy())
    val orderByList: StateFlow<List<OrderBy>> = _orderByList
    private val pre_selectedOrderBy = MutableStateFlow<OrderBy?>(null)
    val selectedOrderBy: StateFlow<OrderBy?> = pre_selectedOrderBy
    private val _selectedOrderBy = MutableStateFlow<OrderBy?>(null)
    fun setSelectedOrderBy(orderBy: OrderBy) {
        pre_selectedOrderBy.value = if (orderBy == pre_selectedOrderBy.value) null else orderBy
    }


    init {
        viewModelScope.launch(Dispatchers.IO) {
            searchDebouncer
                .debounce(500L)
                .collectLatest { searchQuery ->
                    if (searchQuery.length > 2) {
                        performSearch(searchQuery)
                    } else {
                        _animeSearch.value = emptyNewAnimeSearchModel
                    }
                }
        }
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
        viewModelScope.launch(Dispatchers.IO) {
            if (!isPerformingSearch.value) {
                searchDebouncer.emit(text)
            }
        }
    }

    // note: if you hit the"ok" button without tapping on genres - it will show you whole list of animes.
    private fun performSearch(query: String) {
        try {
            // This "if" statement is temporary added because
            // Jikan.Api isn't working properly with query that
            // contains less then 3 letters.

            _currentPage.value = 1
            _isPerformingSearch.value = true

            val response = malApiRepository.getAnimeSearchByName(
                sfw = safeForWork.value,
                query = query,
                page = currentPage.value,
                genres = _genres.value,
                rating = _selectedRating.value?.ratingName,
                type = _selectedType.value?.typeName,
                orderBy = _selectedOrderBy.value?.orderBy,
                max_score = _max_score.value?.score,
                min_score = _min_score.value?.score

            )

            homeScreenCaching(response.data)
            hasNextPage.value = response.pagination.has_next_page
            _animeSearch.value = response


        } catch (e: Exception) {
            Log.e("HomeScreenViewModel", "Failed to perform search: ${e.message}")
        } finally {
            _isPerformingSearch.value = false
        }
    }

    fun loadNextPage() {
        val query = searchText.value
        if (!hasNextPage.value) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val nextPage = currentPage.value + 1
                val response =
                    malApiRepository.getAnimeSearchByName(
                        sfw = safeForWork.value,
                        query = query,
                        page = nextPage,
                        genres = _genres.value,
                        rating = _selectedRating.value?.ratingName,
                        type = _selectedType.value?.typeName,
                        orderBy = _selectedOrderBy.value?.orderBy,
                        max_score = _max_score.value?.score,
                        min_score = _min_score.value?.score

                    )


                homeScreenCaching(response.data)

                hasNextPage.value = response.pagination.has_next_page

                response.let { newAnimeSearchModel ->
                    _animeSearch.value =
                        _animeSearch.value.copy(data = _animeSearch.value.data + newAnimeSearchModel.data)
                    _currentPage.value = nextPage
                    _isNextPageLoading.value = newAnimeSearchModel.pagination.has_next_page
                }

            } catch (e: Exception) {
                Log.e("HomeScreenViewModel", "Failed to load next page: ${e.message}")
            } finally {
                _isNextPageLoading.value = false
            }
        }
    }


    fun tappingOnGenre(number: Int) {
        if (arrayOfGenres.value.contains(number)) {
            arrayOfGenres.value.remove(number)
        } else {
            arrayOfGenres.value.add(number)
        }
    }


    private fun makeArrayToLinkWithCommas(array: ArrayList<Int>): String {
        if (array.isEmpty()) {
            return ""
        }

        val stringBuilder = StringBuilder()
        for (i in 0 until array.size - 1) {
            stringBuilder.append(array[i])
            stringBuilder.append(",")
        }
        stringBuilder.append(array[array.size - 1])

        Log.d("link", stringBuilder.toString())
        return stringBuilder.toString()
    }


    suspend fun addAllParams() {
        val jobGenres = viewModelScope.async(Dispatchers.IO) {
            pre_genres = makeArrayToLinkWithCommas(arrayOfGenres.value)
            _genres.value = pre_genres
        }
        val jobRating = viewModelScope.async(Dispatchers.IO) {
            _selectedRating.value = preSelectedRating.value
        }
        val jobTypes = viewModelScope.async(Dispatchers.IO) {
            _selectedType.value = pre_selectedType.value
        }
        val jobOrderBy = viewModelScope.async(Dispatchers.IO) {
            _selectedOrderBy.value = pre_selectedOrderBy.value
        }
        val jobMinMaxScore = viewModelScope.async(Dispatchers.IO) {
            _min_score.value = pre_min_score.value
            _max_score.value = pre_max_score.value
        }

        jobGenres.join()
        jobRating.join()
        jobTypes.join()
        jobOrderBy.join()
        jobMinMaxScore.join()
        performSearch(searchText.value)

    }

    //------------------------------------------------------
    private fun homeScreenCaching(list: List<Data>) {
        list.forEachIndexed { _, data ->
            if (animeCache.containsId(data.mal_id).not()) {
                animeCache.setData(data.mal_id, data)
            }
        }
    }
}


