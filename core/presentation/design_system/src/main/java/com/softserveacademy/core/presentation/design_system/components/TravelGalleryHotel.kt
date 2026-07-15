package com.softserveacademy.core.presentation.design_system.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

/**
 * A screen component that displays a collection of images in a staggered grid layout.
 *
 * This component uses [LazyVerticalStaggeredGrid] to display images with varying heights,
 * creating a dynamic and visually appealing gallery.
 *
 * @param images A list of image URLs to be displayed in the grid.
 */
@Composable
fun HotelGalleryScreen(
    images: List<String>,
) {

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        itemsIndexed(images) { index, image ->

            GalleryItem(
                imageUrl = image,
                index = index
            )
        }
    }
}

/**
 * An individual item within the hotel gallery grid.
 *
 * Each item calculates its height based on its index to contribute to the staggered effect.
 *
 * @param imageUrl The URL of the image to display.
 * @param index The position of the item in the list, used to determine its display height.
 */
@Composable
fun GalleryItem(
    imageUrl: String,
    index: Int
) {

    val height = when (index % 4) {
        0 -> 240.dp
        1 -> 180.dp
        2 -> 280.dp
        else -> 200.dp
    }

    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(12.dp)),
        contentScale = ContentScale.Crop,
        onSuccess = {
            android.util.Log.d("GalleryHotel", "Loaded")
        },
        onError = {
            android.util.Log.e(
                "GalleryHotel",
                "Error loading image",
                it.result.throwable
            )
        }
    )
}

private val previewImages = listOf(
    //R.drawable.test_place,
    //R.drawable.test_place,
    //R.drawable.test_place,
    //R.drawable.test_place
    "https://picsum.photos/200/300",
    "https://picsum.photos/200",
    "https://picsum.photos/id/1020/800/600",
    "https://developer.android.com/images/brand/Android_Robot.png"
)

@Preview(showBackground = true)
@Composable
private fun HotelGalleryScreenPreview() {

    HotelGalleryScreen(
        images = previewImages
    )
}