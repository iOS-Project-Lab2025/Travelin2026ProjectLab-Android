package com.softserveacademy.core.presentation.design_system.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

/**
 * A horizontal pager component that displays a gallery of images with a page indicator.
 *
 * This component is used to show a sequence of images, typically for a hotel gallery,
 * and includes a series of dots at the bottom to indicate the current page.
 *
 * @param images A list of image URLs or resource paths to be displayed.
 * @param modifier The modifier to be applied to the carousel container.
 */
@Composable
fun TravelGalleryCarousel(
    images: List<String>,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(
        pageCount = { images.size }
    )

    Box(
        modifier = modifier
    ) {

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->

            AsyncImage(
                model = images[page],
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop,
                onSuccess = {
                    android.util.Log.d("Gallery", "Loaded")
                },
                onError = {
                    android.util.Log.e(
                        "Gallery",
                        "Error loading image",
                        it.result.throwable
                    )
                }
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            repeat(images.size) { index ->

                Box(
                    modifier = Modifier
                        .size(
                            if (pagerState.currentPage == index)
                                10.dp
                            else
                                8.dp
                        )
                        .clip(CircleShape)
                        .background(
                            if (pagerState.currentPage == index)
                                Color.White
                            else
                                Color.White.copy(alpha = 0.5f)
                        )
                )
            }
        }
    }
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
private fun TravelGalleryCarouselPreview() {

    TravelGalleryCarousel(
        images = previewImages,
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    )
}