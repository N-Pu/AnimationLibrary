package com.project.toko.presentation.navigation


import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.project.toko.dao.Dao
import com.project.toko.domain.viewModel.DetailScreenViewModel
import com.project.toko.presentation.screens.detailScreen.mainPage.ActivateDetailScreen
import com.project.toko.presentation.screens.homeScreen.MainScreen
import com.project.toko.presentation.screens.noId.NoId
import com.project.toko.presentation.screens.detailScreen.sideContent.castList.ShowWholeCast
import com.project.toko.presentation.screens.detailScreen.sideContent.staffList.ShowWholeStaff
import com.project.toko.presentation.screens.favoritesScreen.FavoriteScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    viewModelProvider: ViewModelProvider,
    modifier: Modifier,
    dao: Dao
    ) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {
            MainScreen(
                navController = navController,
                viewModelProvider = viewModelProvider,
                modifier = modifier,
                dao = dao
            )
        }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val id = backStackEntry.arguments!!.getInt("id")
            ActivateDetailScreen(
                navController = navController,
                viewModelProvider = viewModelProvider,
                id = id,
                modifier = modifier,
                dao = dao
            )
            Log.d("CATCHED ID = ", id.toString())
        }

        composable(route = Screen.Nothing.value) {
            NoId()
        }
        composable(route = Screen.Favorites.route) {
            FavoriteScreen(
                navController = navController,
                viewModelProvider = viewModelProvider,
                modifier = modifier,
                dao = dao
            )
        }
        composable(route =  Screen.DetailOnCast.value) {
            ShowWholeCast(
            viewModelProvider[DetailScreenViewModel::class.java],
                modifier = modifier
            )
        }
        composable(route =  Screen.DetailOnStaff.value) {
            ShowWholeStaff(
                viewModelProvider[DetailScreenViewModel::class.java],
                modifier = modifier
            )
        }

    }
}