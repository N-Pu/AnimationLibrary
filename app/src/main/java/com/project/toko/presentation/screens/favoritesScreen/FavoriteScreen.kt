package com.project.toko.presentation.screens.favoritesScreen


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import coil.compose.rememberAsyncImagePainter
import com.project.toko.dao.AnimeItem
import com.project.toko.dao.Dao
import com.project.toko.domain.viewModel.DetailScreenViewModel
import com.project.toko.domain.viewModel.FavoriteScreenViewModel
import com.project.toko.presentation.screens.homeScreen.navigateToDetailScreen
import com.project.toko.presentation.theme.LightGreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


// Function that creates 4 grid sections
// - watching, planned, watched, dropped
@Composable
fun FavoriteScreen(
    navController: NavController,
    viewModelProvider: ViewModelProvider,
    modifier: Modifier,
    dao: Dao
) {

    val viewModel = viewModelProvider[FavoriteScreenViewModel::class.java]
    var searchQuery by viewModel.searchQuery //добавить во viewmodel
    val scrollState = rememberLazyGridState()
    val searchResults: List<AnimeItem> by dao.searchAnime(searchQuery)
        .collectAsStateWithLifecycle(emptyList())



    Column(
        modifier
            .fillMaxHeight(1f)
            .fillMaxWidth(1f)
            .background(Color.White)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth(1f)
                .height(100.dp)
                .background(LightGreen),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { newQuery ->
                    searchQuery = newQuery

                },
                modifier = modifier.padding(bottom = 20.dp)
            )
            TextButton(onClick = {
                viewModel.viewModelScope.launch(Dispatchers.IO) { dao.removeAll() }

            }) {
                Text(text = "Delete All", modifier = modifier.padding(bottom = 30.dp))
            }
        }

        Row(
            modifier = modifier
                .fillMaxWidth(1f)
                .fillMaxHeight(1f)
                .background(Color.White)
        ) {
            FavoriteAnimeList(
                dao = dao,
                searchResults = searchResults,
                navController = navController,
                viewModelProvider = viewModelProvider,
                scrollState = scrollState,
                modifier = modifier
            )
        }
        Spacer(modifier = Modifier.height(35.dp))
    }
}


// List of anime in current category
// (watching, planned, watched, dropped)
@Composable
fun FavoriteAnimeList(
    dao: Dao,
    searchResults: List<AnimeItem>,
    navController: NavController,
    viewModelProvider: ViewModelProvider,
    scrollState: LazyGridState,
    modifier: Modifier
) {

    Column(
        modifier = modifier
            .fillMaxWidth(1f)
            .fillMaxHeight(0.95f)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(140.dp), state = scrollState,

            modifier = modifier
                .fillMaxWidth(1f)
                .fillMaxHeight(1f),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            contentPadding = PaddingValues(5.dp)
        ) {
            items(searchResults) { animeItem ->
                FavoriteScreenCardBox(
                    animeItem = animeItem,
                    navController = navController,
                    viewModelProvider = viewModelProvider,
                    modifier = modifier,
                    dao = dao
                )
            }
        }
    }
}


// Function for showing a single
// anime-card with "+" button
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavoriteScreenCardBox(
    animeItem: AnimeItem,
    navController: NavController,
    viewModelProvider: ViewModelProvider,
    modifier: Modifier,
    dao: Dao
) {
    val viewModel = viewModelProvider[DetailScreenViewModel::class.java]
    val painter = rememberAsyncImagePainter(model = animeItem.animeImage)

    Box(
        modifier = modifier
    ) {
        Card(
            modifier = modifier
                .clickable {
                    animeItem.id?.let {
                        navigateToDetailScreen(navController, it)
                    }
                },
            colors = CardDefaults.cardColors(containerColor = LightGreen),
            shape = RectangleShape
        ) {


            Box(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .fillMaxHeight(1f)
            ) {
                Image(
                    painter = painter,
                    contentDescription = "Images for anime: ${animeItem.anime}",
                    modifier = modifier.aspectRatio(9f / 11f),
                    contentScale = ContentScale.FillBounds
                )
                RemoveFromDb(
                    mal_id = animeItem.id ?: 0,
                    viewModel = viewModel,
                    dao = dao,
                    modifier = modifier
                )

                Column(modifier = modifier.background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f))) {
                    ScoreIcon(score = animeItem.score, modifier)
                    ScoredByIcon(scoredBy = animeItem.scored_by, modifier)
                }

            }

            Text(
                text = animeItem.anime,
                textAlign = TextAlign.Center,
                modifier = modifier
                    .fillMaxWidth()
                    .basicMarquee(
                        iterations = Int.MAX_VALUE,
                        delayMillis = 2000,
                        initialDelayMillis = 2000,
                        velocity = 50.dp
                    )
                    .padding(16.dp),
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight(1000),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}

// Icon that placed in FavoriteScreenCardBox
// that shows score
@Composable
private fun ScoreIcon(score: String, modifier: Modifier) {
    Box(modifier = modifier.size(45.dp), contentAlignment = Alignment.Center) {
        Icon(
            Icons.Filled.Star,
            contentDescription = "Score $score",
            tint = MaterialTheme.colorScheme.secondary,
            modifier = modifier.size(45.dp)
        )
        Text(
            text = score,
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

// Icon that placed in FavoriteScreenCardBox
// that shows score by users of MyAnimeList.com
@Composable
private fun ScoredByIcon(scoredBy: String, modifier: Modifier) {
    Box(modifier = modifier.size(45.dp), contentAlignment = Alignment.Center) {
        Icon(
            Icons.Filled.Person,
            contentDescription = "Scored by $scoredBy",
            tint = MaterialTheme.colorScheme.secondary,
            modifier = modifier.size(45.dp)
        )
        Text(
            textAlign = TextAlign.Center,
            text = scoredBy,
            color = Color.White,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold,
            modifier = modifier
                .fillMaxWidth()
                .align(Alignment.BottomEnd)
        )
    }
}

