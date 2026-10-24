package com.list_app.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "list_items",
    indices = [Index(value = ["listId"])],
    foreignKeys = [
        ForeignKey(
            entity = UserList::class,
            parentColumns = ["id"],
            childColumns = ["listId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ListItem(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val listId: String,
    val name: String,
    val description: String = "",
    val price: Double = 0.0,
    val imageUrl: String? = null,
    val webUrl: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
