package com.softserveacademy.core.presentation.design_system.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import com.softserveacademy.core.presentation.design_system.R
import androidx.compose.ui.graphics.Shape

@Composable
fun TravelImageHandler(
    image: Any?,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    shouldClip: Boolean = true
) {
    AsyncImage(
        model = image,
        contentDescription = "image",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .then(
                if (shouldClip) Modifier.clip(shape = shape) else Modifier
            ),
        error = painterResource(R.drawable.placeholder),
        placeholder = painterResource(R.drawable.placeholder)
    )
}