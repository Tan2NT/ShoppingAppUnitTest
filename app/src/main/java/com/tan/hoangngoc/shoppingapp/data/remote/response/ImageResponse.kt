package com.tan.hoangngoc.shoppingapp.data.remote.response

data class ImageResponse(
    val hits: List<ImageResult>?,
    val total: Int?,
    val totalHits: Int?
)
