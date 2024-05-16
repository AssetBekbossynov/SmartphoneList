package com.example.myapplication.ui.screens.detailsScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.myapplication.data.entity.SmartphoneDetailDataResponse
import com.example.myapplication.ui.entity.RequestState
import com.example.myapplication.ui.screens.smartphoneList.MainScreenViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SmartphoneDetailScreen(
    viewModel: MainScreenViewModel = koinViewModel(),
    smartphoneId: String
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.loadSmartphoneDetail(smartphoneId)
    }

    val state by viewModel.smartphoneDetailState.collectAsState()

    if (state is RequestState.Processing) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Fetching data...",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }

    if (state is RequestState.Success<*>) {
        val smartphone = (state as RequestState.Success<*>).data
        if (smartphone is SmartphoneDetailDataResponse.SmartphoneDetails) {
            Column(
                Modifier.padding(16.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(smartphone.photos[0])
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                )
                Text(text = smartphone.title)
                val propertiesText = smartphone.mainProperties.joinToString(separator = "\n") { "${it.propName}: ${it.propValue}" }
                Text(text = propertiesText)
                IconButton(onClick = { viewModel.toggleFavorite(smartphone.code) }) {
                    Icon(
                        imageVector = if (smartphone.inFavorites) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (smartphone.inFavorites) "Remove from favorites" else "Add to favorites"
                    )
                }
            }
        }
    }

    if (state is RequestState.Error) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = (state as RequestState.Error).message,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}