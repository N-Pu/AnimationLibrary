package com.project.toko.presentation


import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.project.toko.domain.models.cache.DataCacheSingleton
import com.project.toko.presentation.appConstraction.AppActivator
import com.project.toko.presentation.theme.TokoTheme
import com.project.toko.presentation.theme.LightGreen
import com.project.toko.domain.viewModel.viewModelFactory.MyViewModelFactory
import com.project.toko.repository.MalApiService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity @Inject constructor() : ComponentActivity() {

//    @Inject
    lateinit var navController: NavHostController
    private val modifier: Modifier = Modifier

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        navigationBarColorChanger()


        setContent {
            HideStatusBar()
            val myViewModelFactory =
                MyViewModelFactory(malApiRepository = MalApiService.instance, context = this)
            val viewModelProvider = ViewModelProvider(this, myViewModelFactory)
            navController = rememberNavController()

            TokoTheme {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = modifier.fillMaxSize(),
                ) {
                    AppActivator(
                        navController = navController,
                        viewModelProvider = viewModelProvider,
                        modifier = modifier
                    )
                }
            }
        }
    }

    private fun navigationBarColorChanger() {
        window.navigationBarColor = LightGreen.copy(alpha =  1f).toArgb()
    }

    @Composable
    private fun HideStatusBar() {
        SideEffect {
            MainScope().launch {
                window.statusBarColor = Color.Transparent.toArgb()
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

            window.decorView.doOnLayout {
                WindowCompat.setDecorFitsSystemWindows(window, false)
                windowInsetsController.hide(WindowInsetsCompat.Type.statusBars())
            }
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
    }
        override fun onDestroy() {
            super.onDestroy()
            DataCacheSingleton.dataCache.clear()
        }

    }