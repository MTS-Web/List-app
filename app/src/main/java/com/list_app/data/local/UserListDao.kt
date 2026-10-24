package com.list_app.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.list_app.data.model.UserList
import com.list_app.data.model.UserListWithItemCount
import kotlinx.coroutines.flow.Flow

@Dao
interface UserListDao {
    @Query("SELECT * FROM user_lists ORDER BY createdAt DESC")
    fun getAllListsFlow(): Flow<List<UserList>>

    @Query("""
        SELECT user_lists.*, COUNT(list_items.id) AS itemCount
        FROM user_lists
        LEFT JOIN list_items ON user_lists.id = list_items.listId
        GROUP BY user_lists.id
        ORDER BY user_lists.createdAt DESC
    """)
    fun getAllListsWithItemCountFlow(): Flow<List<UserListWithItemCount>>

    @Query("SELECT * FROM user_lists WHERE id = :listId LIMIT 1")
    fun getListByIdFlow(listId: String): Flow<UserList?>

    @Query("SELECT * FROM user_lists WHERE id = :listId LIMIT 1")
    suspend fun getListById(listId: String): UserList?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: UserList)

    @Update
    suspend fun updateList(list: UserList)

    @Query("UPDATE user_lists SET coverImageUri = :coverImageUri WHERE id = :listId")
    suspend fun updateListCoverUri(listId: String, coverImageUri: String?)

    @Delete
    suspend fun deleteList(list: UserList)

    @Query("DELETE FROM user_lists WHERE id = :listId")
    suspend fun deleteListById(listId: String)
}
