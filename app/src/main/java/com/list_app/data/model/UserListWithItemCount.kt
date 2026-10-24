package com.list_app.data.model

import androidx.room.Embedded

data class UserListWithItemCount(
    @Embedded val userList: UserList,
    val itemCount: Int = 0
)
