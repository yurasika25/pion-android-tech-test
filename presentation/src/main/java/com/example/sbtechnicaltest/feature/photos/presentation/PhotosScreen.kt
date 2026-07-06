package com.example.sbtechnicaltest.feature.photos.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.sbtechnicaltest.feature.photos.model.PhotoItem
import com.example.sbtechnicaltest.presentation.R
import com.example.sbtechnicaltest.design.SBTechnicalTestTheme
import com.example.sbtechnicaltest.design.StudentBeansAccent
import com.example.sbtechnicaltest.design.StudentBeansBackground
import com.example.sbtechnicaltest.design.StudentBeansPrimaryText
import com.example.sbtechnicaltest.design.StudentBeansSecondaryText
import com.example.sbtechnicaltest.design.StudentBeansSurface
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

/**
 * Hilt-aware route that collects [PhotosViewModel] state and forwards UI actions.
 */
@Composable
fun PhotosRoute(
    onBackClick: () -> Unit,
    viewModel: PhotosViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PhotosScreen(
        uiState = uiState,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onRetryClicked = viewModel::onRetryClicked,
        onLoadMore = viewModel::onLoadMoreRequested,
        onBackClick = onBackClick,
    )
}

/**
 * Stateless Photos UI with local search, infinite-scroll loading, and retryable error states.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotosScreen(
    uiState: PhotosUiState,
    onSearchQueryChanged: (String) -> Unit,
    onRetryClicked: () -> Unit,
    onLoadMore: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = StudentBeansBackground,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.photos_title),
                        color = StudentBeansPrimaryText,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    val interactionSource = remember { MutableInteractionSource() }
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null,
                                onClick = onBackClick,
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(
                                R.string.photos_back_content_description,
                            ),
                            tint = StudentBeansSecondaryText,
                            modifier = Modifier.size(28.dp),
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = StudentBeansBackground,
                    scrolledContainerColor = StudentBeansBackground,
                ),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 14.dp),
        ) {
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = onSearchQueryChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 14.dp)
                    .height(56.dp),
                placeholder = {
                    Text(
                        text = stringResource(R.string.photos_search_placeholder),
                        color = StudentBeansSecondaryText,
                    )
                },
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = StudentBeansPrimaryText,
                    fontSize = 16.sp,
                ),
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = StudentBeansSurface,
                    unfocusedContainerColor = StudentBeansSurface,
                    focusedBorderColor = StudentBeansAccent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = StudentBeansAccent,
                ),
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                val errorMessage = uiState.errorMessage
                when {
                    uiState.isLoading -> LoadingContent()
                    errorMessage != null -> ErrorContent(
                        message = errorMessage,
                        onRetryClicked = onRetryClicked,
                    )
                    uiState.photos.isEmpty() -> EmptyContent()
                    else -> PhotosList(
                        photos = uiState.photos,
                        isLoadingMore = uiState.isLoadingMore,
                        loadMoreErrorMessage = uiState.loadMoreErrorMessage,
                        hasMorePages = uiState.hasMorePages,
                        onLoadMore = onLoadMore,
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = StudentBeansAccent)
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetryClicked: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = message,
            color = StudentBeansSecondaryText,
            style = MaterialTheme.typography.bodyLarge,
        )
        Button(
            onClick = onRetryClicked,
            modifier = Modifier.padding(top = 16.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = StudentBeansAccent,
                contentColor = Color.White,
            ),
        ) {
            Text(stringResource(R.string.photos_retry_button))
        }
    }
}

@Composable
private fun EmptyContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.photos_empty_message),
            color = StudentBeansSecondaryText,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun PhotosList(
    photos: List<PhotoItem>,
    isLoadingMore: Boolean,
    loadMoreErrorMessage: String?,
    hasMorePages: Boolean,
    onLoadMore: () -> Unit,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(
        listState,
        photos.size,
        isLoadingMore,
        loadMoreErrorMessage,
        hasMorePages,
    ) {
        snapshotFlow {
            val lastVisibleItemIndex =
                listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            val itemCount = listState.layoutInfo.totalItemsCount
            itemCount > 0 &&
                lastVisibleItemIndex >= itemCount - LOAD_MORE_THRESHOLD &&
                !isLoadingMore &&
                loadMoreErrorMessage == null &&
                hasMorePages
        }
            .distinctUntilChanged()
            .filter { shouldLoadMore -> shouldLoadMore }
            .collect {
                onLoadMore()
            }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 24.dp),
    ) {
        items(
            items = photos,
            key = PhotoItem::id,
        ) { photo ->
            PhotoCard(photo = photo)
        }

        if (isLoadingMore) {
            item(key = "load-more-progress") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(28.dp),
                        color = StudentBeansAccent,
                    )
                }
            }
        } else if (loadMoreErrorMessage != null) {
            item(key = "load-more-error") {
                LoadMoreErrorContent(
                    message = loadMoreErrorMessage,
                    onRetryClicked = onLoadMore,
                )
            }
        }
    }
}

@Composable
private fun LoadMoreErrorContent(
    message: String,
    onRetryClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = message,
            color = StudentBeansSecondaryText,
            style = MaterialTheme.typography.bodyMedium,
        )
        Button(
            onClick = onRetryClicked,
            modifier = Modifier.padding(top = 8.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = StudentBeansAccent,
                contentColor = Color.White,
            ),
        ) {
            Text(stringResource(R.string.photos_retry_button))
        }
    }
}

@Composable
private fun PhotoCard(
    photo: PhotoItem,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .height(104.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = photo.thumbnailUrl,
            contentDescription = stringResource(
                R.string.photos_image_content_description,
                photo.title,
            ),
            modifier = Modifier
                .size(104.dp)
                .background(Color.White),
            contentScale = ContentScale.Fit,
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(StudentBeansSurface)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            Text(
                text = photo.title,
                color = StudentBeansPrimaryText,
                fontSize = 17.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 25.sp,
            )
        }
    }
}

// Preview states exercise the stateless UI without Hilt, navigation, or network access.
@Preview(
    name = "Photos - Loading",
    showBackground = true,
    showSystemUi = true,
)
@Composable
private fun PhotosScreenLoadingPreview() {
    PhotosScreenPreviewContent(uiState = PhotosUiState(isLoading = true))
}

@Preview(
    name = "Photos - Success",
    showBackground = true,
    showSystemUi = true,
)
@Composable
private fun PhotosScreenSuccessPreview() {
    PhotosScreenPreviewContent(
        uiState = PhotosUiState(
            isLoading = false,
            photos = listOf(
                PhotoItem(1, "Essence Mascara Lash Princess", ""),
                PhotoItem(2, "Eyeshadow Palette with Mirror", ""),
                PhotoItem(3, "Red Lipstick", ""),
            ),
        ),
    )
}

@Preview(
    name = "Photos - Error",
    showBackground = true,
    showSystemUi = true,
)
@Composable
private fun PhotosScreenErrorPreview() {
    PhotosScreenPreviewContent(
        uiState = PhotosUiState(
            isLoading = false,
            errorMessage = stringResource(R.string.photos_load_error),
        ),
    )
}

@Preview(
    name = "Photos - Empty",
    showBackground = true,
    showSystemUi = true,
)
@Composable
private fun PhotosScreenEmptyPreview() {
    PhotosScreenPreviewContent(
        uiState = PhotosUiState(isLoading = false),
    )
}

@Composable
private fun PhotosScreenPreviewContent(
    uiState: PhotosUiState,
) {
    SBTechnicalTestTheme {
        PhotosScreen(
            uiState = uiState,
            onSearchQueryChanged = {},
            onRetryClicked = {},
            onLoadMore = {},
            onBackClick = {},
        )
    }
}

private const val LOAD_MORE_THRESHOLD = 3
