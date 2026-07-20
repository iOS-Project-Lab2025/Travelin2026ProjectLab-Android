package com.softserveacademy.core.presentation.design_system.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import com.softserveacademy.core.presentation.design_system.R
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens

@Composable
fun TravelImageHandler(
    image: Any?,
    imageWidth: androidx.compose.ui.unit.Dp = TravelinDimens.ImageSizeMedium,
    imageHeight: androidx.compose.ui.unit.Dp = TravelinDimens.ImageSizeMedium,
    shape: androidx.compose.ui.graphics.Shape = MaterialTheme.shapes.medium,
    shouldClip: Boolean = true
) {
    AsyncImage(
        model = image,
        contentDescription = "hotel image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .width(imageWidth)
            .height(imageHeight)
            .then(
                if (shouldClip) Modifier.clip(shape = shape) else Modifier
            ),
        error = painterResource(R.drawable.test_hotel)
    )
}