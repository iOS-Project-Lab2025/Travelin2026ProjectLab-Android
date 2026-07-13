package com.softserveacademy.home.presentation.ui.screens


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softserveacademy.core.presentation.design_system.components.TravelGalleryCarousel
import com.softserveacademy.core.presentation.design_system.components.TravelRatingBar
import com.softserveacademy.core.presentation.design_system.theme.BlueDark90_Alpha50
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.home.presentation.model.HotelDetailsUi
import com.softserveacademy.home.presentation.model.IncludedItemUi
import com.softserveacademy.home.presentation.ui.components.TravelBookingBar
import com.softserveacademy.home.presentation.ui.components.TravelHotelDetailsTopIcons
import com.softserveacademy.home.presentation.ui.components.TravelIncludedItem
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.softserveacademy.home.presentation.R.string
import com.softserveacademy.core.domain.model.IncludedItem
import com.softserveacademy.home.presentation.state.HotelDetailState
import com.softserveacademy.home.presentation.viewmodel.HotelDetailsViewModel

/**
 * Stateful wrapper for the [TravelHotelDetailScreen].
 *
 * This composable handles the connection between the UI and the [HotelDetailsViewModel].
 * It collects the state from the ViewModel and displays the appropriate UI (Loading, Error,
 * or Data).
 *
 * @param onBackClick Action to perform when the back button is clicked.
 * @param modifier The modifier to be applied to the layout.
 * @param viewModel The ViewModel that provides the hotel detail data.
 */
@Composable
fun HotelDetailState(
    onBackClick: () -> Unit,
    onSeeAllPhotosClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HotelDetailsViewModel = hiltViewModel(),
){
    val hotelDetailState by viewModel.hotelDetailState.collectAsState()

    LaunchedEffect(Unit){
        viewModel.getHotelDetail(1)
    }

    when(hotelDetailState){
        is HotelDetailState.Data -> {

            val hotelInformation = (hotelDetailState as HotelDetailState.Data).hotelDetail

            val hotelDetailsUi = HotelDetailsUi(
                id = hotelInformation.id,
                minimumPrice = hotelInformation.minimumPrice,
                imageList = hotelInformation.imageList,
                name = hotelInformation.name,
                numberOfReviews = hotelInformation.numberOfReviews,
                numberOfImages = hotelInformation.numberOfImages,
                rating = hotelInformation.rating,
                description = hotelInformation.description,
                includedItems = hotelInformation.includedItems.map { it.toUi() }
            )

            TravelHotelDetailScreen(
                hotelInformation = hotelDetailsUi,
                onBackClick = onBackClick,
                onSeeAllPhotosClick = onSeeAllPhotosClick,
                modifier = modifier
            )
        }
        is HotelDetailState.Error -> {
            Text(text = "Error: ${(hotelDetailState as HotelDetailState.Error).message}")
        }
        is HotelDetailState.IsLoading -> {
            CircularProgressIndicator()
        }
    }
}

/**
 * Maps a domain [IncludedItem] to its corresponding [IncludedItemUi] model.
 *
 * @return The UI representation of the included item, including its title and icon.
 */
@Composable
private fun IncludedItem.toUi(): IncludedItemUi = when (this) {
    IncludedItem.BuffetBreakfast -> IncludedItemUi.BuffetBreakfast
    IncludedItem.FreeWifi -> IncludedItemUi.FreeWifi
    IncludedItem.FitnessCenter -> IncludedItemUi.FitnessCenter
    IncludedItem.Pool -> IncludedItemUi.Pool
    IncludedItem.CleaningServices -> IncludedItemUi.CleaningServices
    IncludedItem.SelfParking -> IncludedItemUi.SelfParking
    IncludedItem.RoomService -> IncludedItemUi.RoomService
    IncludedItem.AcUnit -> IncludedItemUi.AcUnit
}

/**
 * A detail screen for a hotel or travel destination.
 * Displays information such as image carousel, description, what's included, and a gallery.
 *
 * @param hotelInformation The data model containing all the information to be displayed.
 * @param modifier The modifier to be applied to the screen.
 * @param onBackClick The action to perform when the back button is clicked.
 * @param onShareClick The action to perform when the share button is clicked.
 * @param onFavoriteClick The action to perform when the favorite button is clicked.
 * @param onBookClick The action to perform when the "Book Now" button is clicked.
 */
@Composable
fun TravelHotelDetailScreen(
    hotelInformation: HotelDetailsUi,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onSeeAllPhotosClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onBookClick: () -> Unit = {}
) {
    Scaffold(
        bottomBar = {
            Surface(shadowElevation = 8.dp) {
                TravelBookingBar(
                    price = hotelInformation.minimumPrice,
                    onBookClick = onBookClick
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            item {
                HotelHeaderImage(
                    imageList = hotelInformation.imageList,
                    name = hotelInformation.name,
                    rating = hotelInformation.rating,
                    limitedReviews = hotelInformation.limitedReviews,
                    limitedImages = hotelInformation.limitedImages,
                    onBackClick = onBackClick,
                    onShareClick = onShareClick,
                    onFavoriteClick = onFavoriteClick
                )
            }
            item {
                HotelDescriptionSection(
                    description = hotelInformation.description
                )
            }
            item {
                IncludingSection(includedItems = hotelInformation.includedItems)
            }
            item {
                GalleryPreviewSection(
                    imageList = hotelInformation.imageList,
                    numberOfImages = hotelInformation.numberOfImages,
                    onSeeAllPhotosClick = onSeeAllPhotosClick
                )
            }
        }
    }
}

/**
 * Displays the header section of the hotel detail screen, including the image carousel
 * and basic info.
 *
 * @param imageList List of image URLs to show in the carousel.
 * @param name The name of the hotel.
 * @param rating The hotel's user rating.
 * @param limitedReviews Formatted string representing the number of reviews.
 * @param limitedImages Formatted string representing the number of images.
 * @param onBackClick Action to perform when the back button is clicked.
 * @param onShareClick Action to perform when the share button is clicked.
 * @param onFavoriteClick Action to perform when the favorite button is clicked.
 */
@Composable
private fun HotelHeaderImage(
    imageList : List<String>,
    name : String,
    rating : Double,
    limitedReviews : String,
    limitedImages : String,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {

        TravelGalleryCarousel(
            images = imageList,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )

        TravelHotelDetailsTopIcons(onBackClick,onShareClick,onFavoriteClick)

        Column(
            verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceExtraSmall),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(TravelinDimens.PaddingMedium)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Row(verticalAlignment = Alignment.CenterVertically) {

                TravelRatingBar(rating = rating)

                Spacer(modifier = Modifier.width(TravelinDimens.SpaceSmall))
                Text(
                    text = limitedReviews,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        Surface(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(TravelinDimens.PaddingMedium),
            color = BlueDark90_Alpha50,
            shape = MaterialTheme.shapes.small
        ) {
            Text(
                text = limitedImages,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .padding(
                        horizontal = TravelinDimens.PaddingSmall,
                        vertical = TravelinDimens.PaddingExtraSmall
                    ),
                style = MaterialTheme.typography.bodySmall
            )
        }

    }
}

/**
 * Displays the description section of the hotel, with "Read more/less" functionality.
 *
 * @param description The full description text of the hotel.
 */
@Composable
private fun HotelDescriptionSection(
    description: String
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(TravelinDimens.PaddingLarge)
    ) {
        Text(
            text = stringResource(id = string.about_label),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onPrimaryFixed,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceSmall))

        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = if (isExpanded) Int.MAX_VALUE else 6,
        )
        Text(
            text =  if (isExpanded) stringResource(id = string.read_less_label)
                    else stringResource(id = string.read_more_label),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier
                .clickable { isExpanded = !isExpanded }
        )
        HorizontalDivider(
            modifier = Modifier
                .padding(
                    vertical = TravelinDimens.PaddingLarge
                )
        )
    }
}

/**
 * Displays the "What's included" section, showing amenities in a two-column grid.
 *
 * @param includedItems List of UI models representing the amenities.
 */
@Composable
private fun IncludingSection(
    includedItems: List<IncludedItemUi>
) {
    if (includedItems.isEmpty()) return
    
    Column(modifier = Modifier
            .padding(
                horizontal = TravelinDimens.PaddingLarge)
    ) {
        Text(
            text = stringResource(id = string.What_is_included_label),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onPrimaryFixed,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))

        includedItems.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceMedium)
            ) {
                rowItems.forEach { item ->
                    TravelIncludedItem(
                        item = item,
                        modifier = Modifier.weight(1f)
                    )
                }
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))
        }

        HorizontalDivider(
            modifier = Modifier
                .padding(
                    vertical = TravelinDimens.PaddingLarge
                )
        )
    }
}

/**
 * Displays a preview of the hotel gallery with 3 images and a button to view all photos.
 *
 * @param imageList List of image URLs to choose from.
 * @param numberOfImages Total number of images available.
 * @param onSeeAllPhotosClick Action to perform when the button is clicked.
 */
@Composable
private fun GalleryPreviewSection(
    imageList: List<String>,
    numberOfImages: Int,
    onSeeAllPhotosClick: () -> Unit
) {
    if (imageList.isEmpty()) return

    Column(
        modifier = Modifier
            .padding(horizontal = TravelinDimens.PaddingLarge)
            .padding(bottom = TravelinDimens.PaddingLarge)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceSmall)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceSmall)
            ) {
                if (imageList.isNotEmpty()) {
                    AsyncImage(
                        model = imageList[0],
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
                if (imageList.size > 1) {
                    AsyncImage(
                        model = imageList[1],
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            if (imageList.size > 2) {
                AsyncImage(
                    model = imageList[2],
                    contentDescription = null,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceLarge))

        OutlinedButton(
            onClick = onSeeAllPhotosClick,
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline
            )
        ) {
            Text(
                text = "See all +$numberOfImages photos",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TravelHotelDetailScreenPreview() {
    Travelin2026ProjectLabTheme(darkTheme = false) {
        TravelHotelDetailScreen(
            hotelInformation = HotelDetailsUi(
                id = 1,
                minimumPrice = 400,
                imageList = listOf(
                    "https://picsum.photos/200",
                    "https://picsum.photos/id/1020/800/600",
                    "https://images.unsplash.com/photo-1582719508461-905c673771fd",
                    "https://images.unsplash.com/photo-1582719508461-905c673771fd",
                    "https://images.unsplash.com/photo-1582719508461-905c673771fd",
                    "https://images.unsplash.com/photo-1582719508461-905c673771fd",
                    "https://images.unsplash.com/photo-1582719508461-905c673771fd"
                ),
                name = "Koh Rong Samloem",
                numberOfReviews = 30,
                rating = 3.6,
                numberOfImages = 200,
                description = LoremIpsum(words = 50).values.first(),
                includedItems = listOf(
                    IncludedItemUi.BuffetBreakfast,
                    IncludedItemUi.FreeWifi,
                    IncludedItemUi.Pool
                )
            )
        )
    }
}
