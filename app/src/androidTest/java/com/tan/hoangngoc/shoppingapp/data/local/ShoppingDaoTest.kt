package com.tan.hoangngoc.shoppingapp.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.tan.hoangngoc.shoppingapp.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class ShoppingDaoTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: ShoppingItemDatabase
    private lateinit var dao: ShoppingDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ShoppingItemDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.shoppingDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertShoppingItem() = runBlocking {
        val shoppingItem = ShoppingItem("Banana", 1, 2.0f, "", 1)
        dao.insertShoppingItem(shoppingItem)

        val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()

        assert(allShoppingItems.contains(shoppingItem))
    }

    @Test
    fun deleteShoppingItem() = runBlocking {
        val shoppingItem = ShoppingItem("Banana", 1, 2.0f, "", 1)
        dao.insertShoppingItem(shoppingItem)
        dao.deleteShoppingItem(shoppingItem)

        val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()

        assert(!allShoppingItems.contains(shoppingItem))
    }

    @Test
    fun observeAllShoppingItems() = runBlocking {
        val shoppingItem1 = ShoppingItem("Banana", 1, 2.0f, "", 1)
        val shoppingItem2 = ShoppingItem("Apple", 1, 3.0f, "", 2)
        dao.insertShoppingItem(shoppingItem1)
        dao.insertShoppingItem(shoppingItem2)

        val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()

        assert(allShoppingItems.size == 2)
    }

    @Test
    fun observeTotalPrice() = runBlocking {
        val shoppingItem1 = ShoppingItem("Banana", 1, 2.0f, "", 1)
        val shoppingItem2 = ShoppingItem("Apple", 1, 3.0f, "", 2)
        dao.insertShoppingItem(shoppingItem1)
        dao.insertShoppingItem(shoppingItem2)

        val totalPrice = dao.observeTotalPrice().getOrAwaitValue()

        Assert.assertEquals(5.0f, totalPrice)
    }
}