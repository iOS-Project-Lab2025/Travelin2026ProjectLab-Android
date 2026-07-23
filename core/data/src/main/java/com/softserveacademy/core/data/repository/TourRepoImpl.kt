package com.softserveacademy.core.data.repository

import com.softserveacademy.core.domain.model.Tour
import com.softserveacademy.core.domain.model.TourCategory
import com.softserveacademy.core.domain.repository.TourRepo
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

/**
 * Test implementation of the [TourRepo] interface that provides mock data.
 */
@Singleton
class TourRepoImpl @Inject constructor() : TourRepo {

    private val _tours = MutableStateFlow(listOf(
        Tour(
            id = "tour_1",
            title = "Mount Bromo Sunrise Tour",
            description = "Experience the breathtaking sunrise over the volcanic landscape of Mount Bromo.",
            location = "East Java, Indonesia",
            imageUrl = "https://picsum.photos/id/1015/800/600",
            duration = 1.days,
            price = 150.0,
            rating = 4.9f,
            category = TourCategory.ADVENTURE
        ),
        Tour(
            id = "tour_2",
            title = "Bali Cultural Dance & Dinner",
            description = "Enjoy a traditional Kecak fire dance followed by a delicious Balinese dinner.",
            location = "Ubud, Bali",
            imageUrl = "https://picsum.photos/id/1016/800/600",
            duration = 4.hours,
            price = 45.0,
            rating = 4.7f,
            category = TourCategory.CULTURE
        ),
        Tour(
            id = "tour_3",
            title = "Komodo Island Expedition",
            description = "Visit the home of the world's largest lizards and snorkel in crystal clear waters.",
            location = "Komodo National Park, Indonesia",
            imageUrl = "https://picsum.photos/id/1018/800/600",
            duration = 3.days,
            price = 450.0,
            rating = 4.8f,
            category = TourCategory.NATURE
        ),
        Tour(
            id = "tour_4",
            title = "Tokyo City Lights Night Tour",
            description = "Explore the vibrant nightlife and neon lights of Tokyo's most iconic districts.",
            location = "Tokyo, Japan",
            imageUrl = "https://picsum.photos/id/1019/800/600",
            duration = 5.hours,
            price = 75.0,
            rating = 4.6f,
            category = TourCategory.CITY
        ),
        Tour(
            id = "tour_5",
            title = "Tuscany Vineyard Wine Tasting",
            description = "Savor the finest wines and local produce in the beautiful rolling hills of Tuscany.",
            location = "Tuscany, Italy",
            imageUrl = "https://picsum.photos/id/1020/800/600",
            duration = 6.hours,
            price = 120.0,
            rating = 4.9f,
            category = TourCategory.GASTRONOMY
        )
    ))

    override suspend fun getTours(): List<Tour> {
        return _tours.value
    }

    override suspend fun getTourById(id: String): Tour {
        return _tours.value.find { it.id == id } ?: throw NoSuchElementException("Tour with id $id not found")
    }
}
