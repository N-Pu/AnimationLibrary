package com.project.toko.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface Dao {

    // Функция для добавления аниме в категорию
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorites(animeItem: AnimeItem)

    @Query("SELECT NOT EXISTS (SELECT 1 FROM animeItems LIMIT 1)")
    fun isDataBaseEmpty(): Flow<Boolean>

    @Query("DELETE FROM animeItems WHERE id = :id")
    suspend fun removeFromDataBase(id: Int)

    @Query("DELETE FROM animeItems")
    suspend fun removeAll()

    // Функция для проверки наличия ID в базе данных
    @Query("SELECT EXISTS(SELECT 1 FROM animeItems WHERE id = :id LIMIT 1)")
    fun containsInDataBase(id: Int): Flow<Boolean>

    @Query("SELECT * FROM animeItems WHERE anime LIKE '%' || :searchQuery || '%' OR :searchQuery = ''")
    fun searchAnime(searchQuery: String?): Flow<List<AnimeItem>>

}
