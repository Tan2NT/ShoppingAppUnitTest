package com.tan.hoangngoc.shoppingapp.di

import android.content.Context
import androidx.room.Room
import com.tan.hoangngoc.shoppingapp.data.local.ShoppingDao
import com.tan.hoangngoc.shoppingapp.data.local.ShoppingItemDatabase
import com.tan.hoangngoc.shoppingapp.data.remote.response.PixabayAPI
import com.tan.hoangngoc.shoppingapp.other.Constants.BASE_URL
import com.tan.hoangngoc.shoppingapp.other.Constants.DATABASE_NAME
import com.tan.hoangngoc.shoppingapp.repository.DefaultShoppingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideShoppingItemDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, ShoppingItemDatabase::class.java, DATABASE_NAME)

    @Singleton
    @Provides
    fun provideDefaultShoppingRepository(
        dao: ShoppingDao,
        api: PixabayAPI
    ) = DefaultShoppingRepository(dao, api)

    @Singleton
    @Provides
    fun provideShoppingDao(
        database: ShoppingItemDatabase
    ) = database.shoppingDao()

    @Singleton
    @Provides
    fun providePixabayApi(): PixabayAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(PixabayAPI::class.java)
    }
}