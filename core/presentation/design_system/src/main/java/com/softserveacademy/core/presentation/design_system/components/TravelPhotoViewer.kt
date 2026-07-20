package com.softserveacademy.core.presentation.design_system.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.softserveacademy.core.presentation.design_system.theme.AngleLeftIcon
import com.softserveacademy.core.presentation.design_system.theme.AngleRightIcon
import com.softserveacademy.core.presentation.design_system.theme.ArrowLeftIcon
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import kotlinx.coroutines.launch

/**
 * A full-screen photo viewer component inspired by Airbnb.
 *
 * @param images List of image URLs to display.
 * @param initialIndex The index of the image to show first.
 */
@Composable
fun TravelPhotoViewer(
    images: List<String>,
    initialIndex: Int = 0
) {
    val pagerState = rememberPagerState(initialPage = initialIndex) { images.size }
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    // Sync thumbnail strip scroll with pager
    LaunchedEffect(pagerState.currentPage) {
        listState.animateScrollToItem(pagerState.currentPage)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            AsyncImage(
                model = images[page],
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit,
                placeholder = painterResource(com.softserveacademy.core.presentation.design_system.R.drawable.test_hotel),
                error = painterResource(com.softserveacademy.core.presentation.design_system.R.drawable.test_hotel)
            )
        }



        // Navigation Arrows (visible if more than 1 image)
        if (images.size > 1) {
            // Left Arrow
            if (pagerState.currentPage > 0) {
                TravelIconButton(
                    icon = AngleLeftIcon,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    },
                    modifier = Modifier
                        .padding(start = TravelinDimens.PaddingSmall)
                        .align(Alignment.CenterStart),
                    backgroundColor = Color.White.copy(alpha = 0.3f),
                    iconColor = Color.White
                )
            }

            // Right Arrow
            if (pagerState.currentPage < images.size - 1) {
                TravelIconButton(
                    icon = AngleRightIcon,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    },
                    modifier = Modifier
                        .padding(end = TravelinDimens.PaddingSmall)
                        .align(Alignment.CenterEnd),
                    backgroundColor = Color.White.copy(alpha = 0.3f),
                    iconColor = Color.White
                )
            }
        }

        // Bottom Controls: Position Indicator and Thumbnail Strip
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.6f))
                .padding(vertical = TravelinDimens.PaddingMedium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceSmall)
        ) {
            // Position Indicator (e.g., 3 / 35)
            Text(
                text = "${pagerState.currentPage + 1} / ${images.size}",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            // Thumbnail Strip
            LazyRow(
                state = listState,
                contentPadding = PaddingValues(horizontal = TravelinDimens.PaddingMedium),
                horizontalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceExtraSmall),
                modifier = Modifier.height(60.dp)
            ) {
                itemsIndexed(images) { index, imageUrl ->
                    val isSelected = index == pagerState.currentPage
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(
                                width = 2.dp,
                                color = if (isSelected) Color.White else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            }
                    ) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(com.softserveacademy.core.presentation.design_system.R.drawable.test_hotel),
                            error = painterResource(com.softserveacademy.core.presentation.design_system.R.drawable.test_hotel),
                            alpha = if (isSelected) 1f else 0.6f
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun TravelPhotoViewerPreview() {
    val sampleImages = listOf(
        "https://picsum.photos/id/10/800/600",
        "https://picsum.photos/id/11/800/600",
        "https://picsum.photos/id/12/800/600",
        "https://picsum.photos/id/13/800/600",
        "https://picsum.photos/id/14/800/600"
    )
    Travelin2026ProjectLabTheme {
        TravelPhotoViewer(
            images = sampleImages,
            initialIndex = 2
        )
    }
}
