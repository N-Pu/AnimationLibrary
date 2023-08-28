package com.project.toko.presentation.screens.homeScreen

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.project.toko.dao.AnimeItem
import com.project.toko.dao.Dao
import com.project.toko.domain.viewModel.HomeScreenViewModel
import com.project.toko.presentation.theme.LightGreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


// Function that starts when you click on
// button "+" to add anime in 4 different
// categories and shows dropDownMenu that
// contains those categories (watching,
// dropped, planned, watched). User can
// tap on them and anime that was selected
// will be send to data base and placed in

@Composable
fun AddOrRemoveFavorites(
    mal_id: Int,
    anime: String,
    score: String,
    scoredBy: String,
    animeImage: String,
    modifier: Modifier,
    viewModel: HomeScreenViewModel,
    dao: Dao
) {
    val addCircle = Icons.Default.AddCircle
    val check = Icons.Default.Check

    var isAnimeInDb by remember { mutableStateOf(false) }
    val containsInDao by dao.containsInDataBase(mal_id).collectAsStateWithLifecycle(false)

    // Update the icon based on whether the anime is in the database or not
    isAnimeInDb = containsInDao

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
                        if (containsInDao) {
                            dao.removeFromDataBase(mal_id)
                        } else {
                            dao.addToFavorites(
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
                    tint = LightGreen
                )
            }
        }
    }
}
