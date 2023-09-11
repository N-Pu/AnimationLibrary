package com.project.toko.di

import com.project.toko.presentation.MainActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class) // или другом подходящем компоненте
object MainActivityModule {


    @Provides
    fun provideMainActivity(): MainActivity {
        // You can create and return an instance of MainActivity here if needed.
        return MainActivity()
    }

//    @Provides
//    fun provideNavController(activity: MainActivity): NavHostController {
//        return activity.navController
//    }
}

