package com.example.myapplication.ui.screens.smartphoneList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.myapplication.data.entity.SmartphoneDataResponse
import com.example.myapplication.ui.entity.RequestState
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(viewModel: MainScreenViewModel = koinViewModel(), onItemClick: (String) -> Unit) {
    val requestState by viewModel.requestState.collectAsState()
    val smartphones by viewModel.smartphones.collectAsState()

    if (requestState is RequestState.Processing) {
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

    if (requestState is RequestState.Success<*>) {
        val lastIndex = smartphones.lastIndex

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(
                smartphones,
                key = { index, _ ->
                    index
                }
            ) {index, smartphone  ->
                if (smartphones.isNotEmpty() && lastIndex == index) {
                    viewModel.fetchSmartphones()
                }

                SmartphoneItem(
                    smartphone,
                    onFavoriteClick = {
                        viewModel.toggleFavorite(smartphone.code)
                    },
                    onItemClick = onItemClick
                )
            }
        }
    }

    if (requestState is RequestState.Error) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = (requestState as RequestState.Error).message,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun SmartphoneItem(
    updatedItemState: SmartphoneDataResponse.Smartphone,
    onFavoriteClick: (Int) -> Unit,
    onItemClick: (String) -> Unit
) {

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onItemClick(updatedItemState.code) },
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(updatedItemState.photos[0])
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = updatedItemState.title, style = MaterialTheme.typography.titleMedium)
            }

            IconButton(onClick = { onFavoriteClick(updatedItemState.id) }) {
                Icon(
                    imageVector = if (updatedItemState.inFavorites) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (updatedItemState.inFavorites) "Remove from favorites" else "Add to favorites"
                )
            }
        }
    }
}
