package com.project.toko.presentation.screens.favoritesScreen.ui

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.project.toko.presentation.screens.favoritesScreen.vm.DaoViewModel
import com.project.toko.presentation.screens.detailScreen.vm.DetailScreenViewModel
import com.project.toko.presentation.theme.LightGreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun RemoveFromDb(
    mal_id: Int,
    modifier: Modifier,
    viewModelProvider: ViewModelProvider
) {
    val addCircle = Icons.Default.Clear
    val detailViewModel = viewModelProvider[DetailScreenViewModel::class.java]
    val daoViewModel = viewModelProvider[DaoViewModel::class.java]
    // Fetch data when the button is clicked on a specific item
    BoxWithConstraints(
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            modifier = modifier
                .fillMaxWidth()
        ) {
            IconButton(
                onClick = {
                    detailViewModel.viewModelScope.launch(Dispatchers.IO) {
                        daoViewModel.removeFromDataBase(mal_id)
                    }
                },
            ) {
                Icon(
                    imageVector = addCircle,
                    contentDescription = null,
                    tint = LightGreen
                )
            }
        }
    }
}