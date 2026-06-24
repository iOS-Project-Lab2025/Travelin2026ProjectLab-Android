package com.softserveacademy.core.presentation.design_system.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.core.presentation.design_system.theme.shapes

@Composable
fun HotelImage(
    image: Int,
    imageWidth: androidx.compose.ui.unit.Dp = TravelinDimens.ImageSizeMedium,
    imageHeight: androidx.compose.ui.unit.Dp = TravelinDimens.ImageSizeMedium,
    shape: androidx.compose.ui.graphics.Shape = shapes.medium,
    shouldClip: Boolean = true
) {
    Image(
        painter = painterResource(image),
        contentDescription = "hotel image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .width(imageWidth)
            .height(imageHeight)
            .then(
                if (shouldClip) Modifier.clip(shape = shape) else Modifier
            )
    )
}