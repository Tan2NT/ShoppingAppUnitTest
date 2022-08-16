package com.tan.hoangngoc.shoppingapp.repository

import androidx.lifecycle.LiveData
import com.tan.hoangngoc.shoppingapp.data.local.ShoppingItem
import com.tan.hoangngoc.shoppingapp.data.remote.response.ImageResponse
import com.tan.hoangngoc.shoppingapp.other.Resource
import retrofit2.Response

interface ShoppingRepository {
    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun observeAllShoppingItems(): LiveData<List<ShoppingItem>>

    suspend fun observeTotalPrice(): LiveData<Float>

    suspend fun searchForImage(imageQuery: String): Resource<ImageResponse>
}
