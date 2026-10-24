package com.list_app.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.list_app.data.model.ListItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ListItemDao {
    @Query("SELECT * FROM list_items WHERE listId = :listId ORDER BY createdAt DESC")
    fun getItemsForListFlow(listId: String): Flow<List<ListItem>>

    @Query("SELECT * FROM list_items WHERE id = :itemId LIMIT 1")
    fun getItemByIdFlow(itemId: String): Flow<ListItem?>

    @Query("SELECT * FROM list_items WHERE id = :itemId LIMIT 1")
    suspend fun getItemById(itemId: String): ListItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ListItem)

    @Update
    suspend fun updateItem(item: ListItem)

    @Delete
    suspend fun deleteItem(item: ListItem)

    @Query("DELETE FROM list_items WHERE id = :itemId")
    suspend fun deleteItemById(itemId: String)

    @Query("DELETE FROM list_items WHERE listId = :listId")
    suspend fun deleteItemsForList(listId: String)
}
