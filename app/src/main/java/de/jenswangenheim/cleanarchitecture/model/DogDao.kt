package de.jenswangenheim.cleanarchitecture.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DogDao {
    @Insert
    suspend fun insertAll(vararg dogs: DogBreed): List<Long>

    @Query("SELECT * FROM dogbreed")
    suspend fun getAllItems(): List<DogBreed>

    @Query("SELECT * FROM dogbreed WHERE uuid = :id")
    suspend fun getDog(id: Int): DogBreed

    @Query("DELETE FROM dogbreed")
    suspend fun deleteAllItems()
}