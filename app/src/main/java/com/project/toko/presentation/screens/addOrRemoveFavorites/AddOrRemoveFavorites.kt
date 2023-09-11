package com.project.toko.presentation.screens.addOrRemoveFavorites

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.project.toko.dao.AnimeItem
import com.project.toko.presentation.screens.favoritesScreen.vm.DaoViewModel
import com.project.toko.presentation.theme.LightGreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


// Функция для добавления или удаления
// из локальной базы данных текущего объекта

@Composable
fun AddOrRemoveFavorites(
    mal_id: Int,
    anime: String,
    score: String,
    scoredBy: String,
    animeImage: String,
    modifier: Modifier,
    viewModelProvider: ViewModelProvider,
) {
    val addCircle = Icons.Default.AddCircle
    val check = Icons.Default.Check

    var isAnimeInDb by remember { mutableStateOf(false) }
    val vm = viewModelProvider[DaoViewModel::class.java]
    val containsInDao by vm.containsInDataBase(mal_id)
        .collectAsStateWithLifecycle(
            initialValue = false
        )

    // Update the icon based on whether the anime is in the database or not
    isAnimeInDb = containsInDao

    BoxWithConstraints(
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            modifier = modifier
                .fillMaxWidth()
        ) {
            IconButton(
                onClick = {
                    vm.viewModelScope.launch(Dispatchers.IO) {
                        if (containsInDao) {
                            vm.removeFromDataBase(mal_id)
                        } else {
                            vm.addToFavorites(
                                AnimeItem(
                                    mal_id,
                                    anime = anime,
                                    score = score,
                                    scored_by = scoredBy,
                                    animeImage = animeImage,
                                )
                            )
                        }
                        // Toggle the state of isAnimeInDb
                        isAnimeInDb = !isAnimeInDb
                    }
                },
            ) {
                Icon(
                    if (isAnimeInDb) check else addCircle,
                    contentDescription = null,
                    tint = LightGreen,
                    modifier = modifier
                )
            }
        }
    }
}
