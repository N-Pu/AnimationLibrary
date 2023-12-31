package com.project.toko.dao

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [AnimeItem::class], version = 6, exportSchema = false)
abstract class MainDb : RoomDatabase() {

    abstract fun getDao(): Dao

    private lateinit var dao: Dao

    companion object {
        private var INSTANCE: MainDb? = null

        @JvmStatic
        fun getDb(context: Context): MainDb {
            synchronized(this) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        MainDb::class.java,
                        "MainDb.db"
                    ).fallbackToDestructiveMigration().build()
                }

                val dbPath = context.getDatabasePath("MainDb.db").absolutePath
                Log.d("Database Path", dbPath)

                INSTANCE?.let {
                    it.dao = it.getDao()
                }

                return INSTANCE as MainDb
            }
        }
    }
}
