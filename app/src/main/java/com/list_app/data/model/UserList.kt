package com.list_app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "user_lists")
data class UserList(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String = "",
    val coverImageUri: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
