package com.softserveacademy.core.presentation.design_system.components.util.reusable_icons

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import com.softserveacademy.core.presentation.design_system.R
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.core.presentation.design_system.theme.Yellow50

/**
 * A reusable star icon component that supports partial filling.
 *
 * This component is used to display a single star that can be filled by a specific percentage,
 * allowing for detailed rating visualizations.
 *
 * @param starSize The size of the star icon. Defaults to [TravelinDimens.IconSizeExtraSmall].
 * @param starColor The tint color of the filled portion of the star. Defaults to [Yellow50].
 * @param iconFill A float between 0.0 and 1.0 representing the fill fraction of the star.
 */
@Composable
fun TravelStar(
    starSize : Dp = TravelinDimens.IconSizeExtraSmall,
    starColor : Color =  Yellow50,
    iconFill : Float = 1f
){
    Icon(
        painter = painterResource(id = R.drawable.star),
        contentDescription = "Star icon",
        tint = starColor,
        modifier = Modifier
            .size(starSize)
            .clip(FractionalWidthShape(iconFill))
    )
}


/**
 * A custom shape that clips a rectangle to a specific fraction of its width.
 */
private class FractionalWidthShape(private val fraction: Float) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Rectangle(
            Rect(
                left = 0f,
                top = 0f,
                right = size.width * fraction,
                bottom = size.height
            )
        )
    }
}