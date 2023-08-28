package com.project.toko.presentation.appConstraction


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.project.toko.dao.Dao
import com.project.toko.domain.viewModel.DetailScreenViewModel
import com.project.toko.presentation.navigation.Nothing
import com.project.toko.presentation.navigation.Screen
import com.project.toko.presentation.navigation.SetupNavGraph
import com.project.toko.presentation.theme.LightGreen


@Composable
fun TokoAppActivator(
    navController: NavHostController,
    viewModelProvider: ViewModelProvider,
    modifier: Modifier,
    dao: Dao
) {

    val currentDetailScreenId = viewModelProvider[DetailScreenViewModel::class.java].loadedId

    navController.addOnDestinationChangedListener { _, _, arguments ->
        currentDetailScreenId.value = arguments?.getInt("id") ?: 0
    }

    Scaffold(bottomBar = {
        Column(
            modifier = Modifier
                .fillMaxWidth(1f)
                .fillMaxHeight(1f)
                .padding(bottom = 45.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Row {
                BottomNavigationBar(
                    navController = navController,
                    currentDetailScreenId = currentDetailScreenId,
                    modifier = modifier,
                    dao = dao
                )

            }


        }
    },
        floatingActionButtonPosition = FabPosition.Center,
        content = { padding ->
            padding.calculateTopPadding()
            SetupNavGraph(
                navController = navController, viewModelProvider = viewModelProvider,
                modifier = modifier, dao = dao
            )
        }
    )
}


@Composable
fun BottomNavigationBar(
    navController: NavController,
    currentDetailScreenId: MutableState<Int>,
    modifier: Modifier,
    dao: Dao
) {


    val items = listOf(
        Screen.Home, Screen.Detail, Screen.Favorites
    )
    val isDaoEmpty by dao.isDataBaseEmpty().collectAsStateWithLifecycle(initialValue = false)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Row(
        modifier = modifier
            .clip(
                RoundedCornerShape(10.dp)
            )
            .fillMaxWidth(1f)
            .background(LightGreen.copy(alpha = 0.6f))
            .background(Color.Transparent)
            .height(50.dp),

        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (currentRoute != null) {
            Log.d("currentRoute", currentRoute + "==" + items[0].route)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier.padding(horizontal = 10.dp, vertical = 10.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = items[0].iconId),
                contentDescription = items[0].contentDescription,
                modifier = modifier
                    .size(30.dp)
                    .clickable {
                        try {
                            navController.navigate(items[0].route) {

                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                navController.graph.startDestinationRoute?.let { _ ->
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                }

                            }
                        } catch (e: IllegalArgumentException) {

                            Log.e("CATCH", items[0].route + " " + e.message.toString())

                        }
                    }
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier.padding(horizontal = 10.dp, vertical = 0.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = items[1].iconId),
                contentDescription = items[1].contentDescription,
                modifier = modifier
                    .size(30.dp)
                    .clickable {
                        try {
                            if (currentDetailScreenId.value != 0) {
                                navController.navigate("detail_screen/${currentDetailScreenId.value}") {
                                    navController.graph.startDestinationRoute?.let { _ ->
                                        launchSingleTop = true
                                    }
                                }
                            } else {
                                navController.navigate(Nothing.value) {
                                    launchSingleTop = true
                                }
                            }

                        } catch (e: IllegalArgumentException) {
                            Log.e("CATCH", items[1].route + " " + e.message.toString())
                        }
                    }
            )
        }


        if (!isDaoEmpty) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = modifier.padding(horizontal = 10.dp, vertical = 0.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = items[2].iconId),
                    contentDescription = items[2].contentDescription,
                    modifier = modifier
                        .size(30.dp)
                        .clickable {
                            try {
                                navController.navigate(items[2].route) {

                                    // Avoid multiple copies of the same destination when
                                    // reselecting the same item
                                    navController.graph.startDestinationRoute?.let { _ ->
                                        launchSingleTop = true
                                        // Restore state when reselecting a previously selected item
                                    }

                                }
                            } catch (e: IllegalArgumentException) {

                                Log.e("CATCH", items[2].route + " " + e.message.toString())

                            }
                        }
                )
            }
        }
    }
}

