package com.project.toko.domain.viewModel.viewModelFactory

import android.content.Context
import com.project.toko.repository.MalApiService
import com.project.toko.presentation.screens.homeScreen.vm.HomeScreenViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.toko.presentation.screens.favoritesScreen.vm.DaoViewModel
import com.project.toko.presentation.screens.detailScreen.vm.DetailScreenViewModel

@Suppress("UNCHECKED_CAST")
class MyViewModelFactory(
    private val malApiRepository: MalApiService,
    private val context: Context
) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeScreenViewModel::class.java) -> HomeScreenViewModel(
                malApiRepository
            ) as T

            modelClass.isAssignableFrom(DetailScreenViewModel::class.java) -> DetailScreenViewModel(
                malApiRepository
            ) as T

            modelClass.isAssignableFrom(DaoViewModel::class.java) -> DaoViewModel(context) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

}