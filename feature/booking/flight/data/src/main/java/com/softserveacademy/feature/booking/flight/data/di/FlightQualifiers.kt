package com.softserveacademy.feature.booking.flight.data.di

import javax.inject.Qualifier

/**
 * Qualifier to identify the real remote API data source.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RemoteApi

/**
 * Qualifier to identify the local mock data source.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MockApi