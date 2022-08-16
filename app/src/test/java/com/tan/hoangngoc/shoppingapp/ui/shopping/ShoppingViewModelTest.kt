package com.tan.hoangngoc.shoppingapp.ui.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tan.hoangngoc.shoppingapp.MainCoroutineRule
import com.tan.hoangngoc.shoppingapp.getOrAwaitValue
import com.tan.hoangngoc.shoppingapp.other.Constants
import com.tan.hoangngoc.shoppingapp.other.Status
import com.tan.hoangngoc.shoppingapp.repository.FakeShoppingRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ShoppingViewModelTest {
    private lateinit var viewModel: ShoppingViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        viewModel = ShoppingViewModel(FakeShoppingRepository())
    }

    @Test
    fun `insert shopping item with empty filed, return error`() {
        viewModel.insertShoppingItem("Apple", "", "3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()

        Assert.assertEquals(Status.ERROR, value.getContentIfNotHandled()?.status)
    }

    @Test
    fun `insert shopping item with too long name, return error`() {
        val string = buildString {
            for (i in 1..Constants.MAX_NAME_LENGTH + 1) {
                append(1)
            }
        }
        viewModel.insertShoppingItem(string, "5", "3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()

        Assert.assertEquals(Status.ERROR, value.getContentIfNotHandled()?.status)
    }

    @Test
    fun `insert shopping item with too long price, return error`() {
        val price = buildString {
            for (i in 1..Constants.MAX_PRICE_LENGTH + 1) {
                append(1)
            }
        }
        viewModel.insertShoppingItem("Banana", "5", price)

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()

        Assert.assertEquals(Status.ERROR, value.getContentIfNotHandled()?.status)
    }

    @Test
    fun `insert shopping item with valid input, return success`() {
        viewModel.insertShoppingItem("Banana", "5", "2.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()

        Assert.assertEquals(Status.SUCCESS, value.getContentIfNotHandled()?.status)
    }
}
