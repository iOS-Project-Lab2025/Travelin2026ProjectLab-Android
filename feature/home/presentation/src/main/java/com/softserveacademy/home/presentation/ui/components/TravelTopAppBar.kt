package com.softserveacademy.home.presentation.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.home.presentation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelTopAppBar(navSelected: () -> Unit) {
    TopAppBar(
        title = { Text("Travel") },
        navigationIcon = {
            Icon(
                painter = painterResource(R.drawable.travel_ic_home),
                contentDescription = null,
                modifier = Modifier.clickable {
                    navSelected()
                })
        },
        actions = {
            Icon(
                painter = painterResource(R.drawable.travel_ic_home),
                contentDescription = null
            )
            Icon(
                painter = painterResource(R.drawable.travel_ic_home),
                contentDescription = null
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.DarkGray,
            titleContentColor = Color.Cyan
        )
    )
}

@Composable
fun TravelFab(modifier: Modifier = Modifier) {
    FloatingActionButton(onClick = {
    }) {
        Icon(painter = painterResource(R.drawable.ic_add), contentDescription = null)
    }
}

@Composable
fun     TravelNavigationBar(
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
                    painter = painterResource(R.drawable.travel_ic_home),
                    contentDescription = null
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.home_ic_label)/*, color =
             MaterialTheme.colorScheme.primaryContainer*/
                )
            })
        NavigationBarItem(
            selected = selectedTab == 1,
            onClick = { onTabClick(1) },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.travel_ic_ticket),
                    contentDescription = null
                )
            }, label = {
                Text(
                    text = stringResource(R.string.orders_ic_label)/*, color =
             MaterialTheme.colorScheme.primaryContainer*/
                )
            })
        NavigationBarItem(
            selected = selectedTab == 2,
            onClick = { onTabClick(2) },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.travel_ic_percentage),
                    contentDescription = null
                )
            }, label = {
                Text(
                    text = stringResource(R.string.deals_ic_label)/*, color =
             MaterialTheme.colorScheme.primaryContainer*/
                )
            })
        NavigationBarItem(
            selected = selectedTab == 3,
            onClick = { onTabClick(3) },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.travel_ic_person),
                    contentDescription = null
                )
            }, label = {
                Text(
                    text = stringResource(R.string.accounts_ic_label)/*, color =
             MaterialTheme.colorScheme.primaryContainer*/
                )
            })
    }
}

@Composable
fun TravelModalDrawer(drawerState: DrawerState, content: @Composable () -> Unit) {

    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet() {
                Text(text = "ejemplo 1")
                Text(text = "ejemplo 2")
                Text(text = "ejemplo 3")
            }
        }
    ) {
        content()
    }
}

@Composable
fun TravelTextField(modifier: Modifier = Modifier) {
    var state = rememberTextFieldState(initialText = "")
    TextField(
        state = state, modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Row {
                Icon(painter = painterResource(R.drawable.ic_search2), contentDescription = null)
                Text(text = "Where to go?")
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
            unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        shape = RoundedCornerShape(10.dp)
    )
}

@Composable
fun TravelBackground(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            drawOval(
                color = Color(0xFF4DA8B5),
                topLeft = Offset(x = -width * 0.1f, y = -height * 0.5f),
                size = Size(width * 1.2f, height * 0.69f)
            )
            drawOval(
                color = Color(0xFF007A8C),
                topLeft = Offset(x = -width * 0.1f, y = -height * 0.5f),
                size = Size(width * 1.2f, height * 0.615f)
            )
            drawOval(
                color = Color(0xFF005F6B),
                topLeft = Offset(x = -width * 0.1f, y = -height * 0.5f),
                size = Size(width * 1.2f, height * 0.55f)
            )

        }

        // Tu contenido va aquí encima
    }
}

@Composable
fun TravelCard(modifier: Modifier = Modifier) {
    Card(
        //modifier= Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(R.drawable.tickethome), contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(166.dp),
            //contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun TravelIconsCard() {
    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(color=MaterialTheme.colorScheme.secondary.copy(alpha = 0.3F)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.travel_ic_plane), contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
            Text(text = stringResource(R.string.flights_label), color = MaterialTheme.colorScheme.secondary)

        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.3F)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.travel_ic_hotel), contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
            Text(text = stringResource(R.string.hotels_label), color = MaterialTheme.colorScheme.secondary)

        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.3F)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.travel_ic_train), contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
            Text(text = stringResource(R.string.train_label), color = MaterialTheme.colorScheme.secondary)

        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.3F)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.travel_ic_ferry), contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
            Text(text = stringResource(R.string.ferry_label), color = MaterialTheme.colorScheme.secondary)

        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.3F)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.travel_ic_bus), contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
            Text(text = stringResource(R.string.bus_label), color = MaterialTheme.colorScheme.secondary)

        }
    }
}

@Preview
@Composable
private fun TravelNavigationBarPreview() {
    Travelin2026ProjectLabTheme() {
        TravelNavigationBar()
    }

}

@Preview
@Composable
private fun TravelTextFieldPreview() {
    Travelin2026ProjectLabTheme() {
        TravelTextField()
    }
}

@Preview
@Composable
private fun TravelBackgroundPreview() {
    Travelin2026ProjectLabTheme() {
        TravelBackground()
    }

}

@Preview
@Composable
private fun TravelIconsCardPreview() {
    Travelin2026ProjectLabTheme() {
        TravelIconsCard()
    }

}