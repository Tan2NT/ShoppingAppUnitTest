package com.tan.hoangngoc.shoppingapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_items")
data class ShoppingItem(
    val name: String,
    val amount: Int,
    val price: Float,
    val imageUtl: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
)
