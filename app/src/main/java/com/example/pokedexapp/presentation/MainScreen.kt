package com.example.pokedexapp.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pokedexapp.R
import com.example.pokedexapp.presentation.navigation.BottomNavItem
import com.example.pokedexapp.presentation.navigation.NavGraph

@Composable
fun MainScreen(
    languageCode: String = ""
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarScreens = listOf(
        BottomNavItem.Home,
        BottomNavItem.TeamBuilder,
        BottomNavItem.Favorites,
        BottomNavItem.Settings
    )

    val currentRoute = currentDestination?.route

    MainContent(
        currentRoute = currentRoute,
        bottomBarScreens = bottomBarScreens,
        onNavigate = { route ->
            navController.navigate(route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                key(languageCode) {
                    NavGraph(navController = navController)
                }
            }
        }
    )
}

@Composable
fun MainContent(
    currentRoute: String?,
    bottomBarScreens: List<BottomNavItem>,
    onNavigate: (String) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val showBottomBar = bottomBarScreens.any { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                NavigationBar {
                    bottomBarScreens.forEach { item ->
                        val isSelected = currentRoute == item.route
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = stringResource(item.titleRes)) },
                            label = { Text(stringResource(item.titleRes)) },
                            selected = isSelected,
                            onClick = { onNavigate(item.route) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}

@Preview(showBackground = true)
@Composable
fun MainContentPreview() {
    com.example.pokedexapp.presentation.theme.PokeDexAppTheme {
        MainContent(
            currentRoute = BottomNavItem.Home.route,
            bottomBarScreens = listOf(
                BottomNavItem.Home,
                BottomNavItem.TeamBuilder,
                BottomNavItem.Favorites,
                BottomNavItem.Settings
            ),
            onNavigate = {},
            content = { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Text(stringResource(R.string.main_content_area))
                }
            }
        )
    }
}
