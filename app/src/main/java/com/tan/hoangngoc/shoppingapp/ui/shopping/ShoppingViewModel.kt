package com.tan.hoangngoc.shoppingapp.ui.shopping

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tan.hoangngoc.shoppingapp.data.local.ShoppingItem
import com.tan.hoangngoc.shoppingapp.data.remote.response.ImageResponse
import com.tan.hoangngoc.shoppingapp.other.Constants
import com.tan.hoangngoc.shoppingapp.other.Event
import com.tan.hoangngoc.shoppingapp.other.Resource
import com.tan.hoangngoc.shoppingapp.repository.ShoppingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(
    private val repository: ShoppingRepository
) : ViewModel() {

    val shoppingItems = repository.observeAllShoppingItems()

    val totalPrice = repository.observeTotalPrice()

    private val _images = MutableLiveData<Event<Resource<ImageResponse>>>()
    val image: LiveData<Event<Resource<ImageResponse>>> = _images

    private val _curImageUrl = MutableLiveData<String>()
    val curImageUrl: LiveData<String> = _curImageUrl

    private val _insertShoppingItemStatus = MutableLiveData<Event<Resource<ShoppingItem>>>()
    val insertShoppingItemStatus: LiveData<Event<Resource<ShoppingItem>>> = _insertShoppingItemStatus

    fun setCurrentImageUrl(url: String) {
        _curImageUrl.postValue(url)
    }

    fun deleteShoppingItemFromDb(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.deleteShoppingItem(shoppingItem)
    }

    fun insertShoppingItemIntoDB(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.insertShoppingItem(shoppingItem)
    }

    fun insertShoppingItem(name: String, amountStr: String, priceStr: String) {
        if (name.isEmpty() || amountStr.isEmpty() || priceStr.isEmpty()) {
            _insertShoppingItemStatus.postValue(
                Event(Resource.error("The price of the item $name must not exceed ${Constants.MAX_PRICE_LENGTH} characters", null))
            )
            return
        }

        if (name.length > Constants.MAX_NAME_LENGTH) {
            _insertShoppingItemStatus.postValue(
                Event(Resource.error("The price of the item $name must not exceed ${Constants.MAX_PRICE_LENGTH} characters", null))
            )
            return
        }

        if (priceStr.length > Constants.MAX_PRICE_LENGTH) {
            _insertShoppingItemStatus.postValue(
                Event(Resource.error("The price of the item $name must not exceed ${Constants.MAX_PRICE_LENGTH} characters", null))
            )
            return
        }

        try {
            viewModelScope.launch {
                val shoppingItem = ShoppingItem(name, amountStr.toInt(), priceStr.toFloat(), "")
                insertShoppingItemIntoDB(shoppingItem)
                setCurrentImageUrl("")
                _insertShoppingItemStatus.postValue(
                    Event(Resource.success(shoppingItem))
                )
            }
        } catch (e: Exception) {
            _insertShoppingItemStatus.postValue(
                Event(Resource.error("Please enter the valid amount or price", null))
            )
        }
    }

    fun searchForImage(imageQuery: String) {
        if (imageQuery.isEmpty()) {
            return
        }
        _images.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = repository.searchForImage(imageQuery)
            _images.value = Event(response)
        }
    }
}
