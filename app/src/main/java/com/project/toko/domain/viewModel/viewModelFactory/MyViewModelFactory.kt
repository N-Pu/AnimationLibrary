package com.project.toko.domain.viewModel.viewModelFactory

import com.project.toko.repository.MalApiService
import com.project.toko.domain.viewModel.HomeScreenViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.toko.domain.viewModel.DetailScreenViewModel
import com.project.toko.domain.viewModel.FavoriteScreenViewModel

@Suppress("UNCHECKED_CAST")
class MyViewModelFactory(
    private val malApiRepository: MalApiService,
) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeScreenViewModel::class.java) -> HomeScreenViewModel(
                malApiRepository
            ) as T

            modelClass.isAssignableFrom(DetailScreenViewModel::class.java) -> DetailScreenViewModel(
                malApiRepository
            ) as T

            modelClass.isAssignableFrom(FavoriteScreenViewModel::class.java) -> FavoriteScreenViewModel() as T


            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

}