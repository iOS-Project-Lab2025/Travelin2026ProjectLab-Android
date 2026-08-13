package com.softserveacademy.core.presentation.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.presentation.design_system.R as DesignSystemR
import com.softserveacademy.core.presentation.ui.R

@Composable
fun TravelNavigationBar(
    selectedTab: Int = 0,
    onTabClick: (Int) -> Unit = {}
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier.shadow(
            elevation = 12.dp,
            clip = false
        ),
        windowInsets = WindowInsets(0, 0, 0, 0)
    ) {
        NavigationBarItem(
            selected = selectedTab == 0,
            onClick = { onTabClick(0) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primaryContainer,
                unselectedIconColor = MaterialTheme.colorScheme.secondaryContainer,
                selectedTextColor = MaterialTheme.colorScheme.primaryContainer,
                unselectedTextColor = MaterialTheme.colorScheme.secondaryContainer,
                indicatorColor = Color.Transparent
            ),
            icon = {
                Icon(
                    painter = painterResource(DesignSystemR.drawable.travel_ic_home),
                    contentDescription = null
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.home_ic_label)
                )
            })
        NavigationBarItem(
            selected = selectedTab == 1,
            onClick = { onTabClick(1) },
            icon = {
                Icon(
                    painter = painterResource(DesignSystemR.drawable.travel_ic_ticket),
                    contentDescription = null
                )
            }, label = {
                Text(
                    text = stringResource(R.string.orders_ic_label)
                )
            })
        NavigationBarItem(
            selected = selectedTab == 2,
            onClick = { onTabClick(2) },
            icon = {
                Icon(
                    painter = painterResource(DesignSystemR.drawable.travel_ic_percentage),
                    contentDescription = null
                )
            }, label = {
                Text(
                    text = stringResource(R.string.deals_ic_label)
                )
            })
        NavigationBarItem(
            selected = selectedTab == 3,
            onClick = { onTabClick(3) },
            icon = {
                Icon(
                    painter = painterResource(DesignSystemR.drawable.travel_ic_person),
                    contentDescription = null
                )
            }, label = {
                Text(
                    text = stringResource(R.string.accounts_ic_label)
                )
            })
    }
}
