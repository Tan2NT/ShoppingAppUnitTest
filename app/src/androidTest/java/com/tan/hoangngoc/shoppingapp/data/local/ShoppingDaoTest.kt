package com.tan.hoangngoc.shoppingapp.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import com.tan.hoangngoc.shoppingapp.getOrAwaitValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.*
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class ShoppingDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: ShoppingItemDatabase
    private lateinit var dao: ShoppingDao

    @Before
    fun setup() {
        hiltRule.inject()
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
