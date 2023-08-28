package com.project.toko.presentation.screens.favoritesScreen

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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.toko.dao.Dao
import com.project.toko.presentation.theme.LightGreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun RemoveFromDb(
    mal_id: Int,
    modifier: Modifier,
    viewModel: ViewModel,
    dao: Dao
) {
    val addCircle = Icons.Default.Clear

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
                    viewModel.viewModelScope.launch(Dispatchers.IO) {
                        dao.removeFromDataBase(mal_id)
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