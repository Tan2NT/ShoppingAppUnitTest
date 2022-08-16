package com.tan.hoangngoc.shoppingapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class ImageResult(
    val comments: Int?,
    val downloads: Int?,
    val favorites: Int?,
    val id: Int?,
    val imageHeight: Int?,
    val imageWidth: Int?,
    val imageSize: Int?,
    val largeImageURL: String?,
    val likes: Int?,
    val previewHeight: Int?,
    val previewWidth: Int?,
    val tags: String?,
    val type: String?,
    val user: String?,
    @SerializedName("user_id")
    val userId: Int?,
    val userImageURL: String?,
    val views: Int?,
    val webFormatHeight: Int?,
    val webFormatWidth: Int?,
)
