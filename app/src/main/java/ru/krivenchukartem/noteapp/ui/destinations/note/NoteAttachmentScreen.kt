package ru.krivenchukartem.noteapp.ui.destinations.note

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.flow.distinctUntilChanged
import ru.krivenchukartem.noteapp.R
import ru.krivenchukartem.noteapp.ui.composable.ErrorScreen
import ru.krivenchukartem.noteapp.ui.composable.NoteTopAppBar
import ru.krivenchukartem.noteapp.ui.form.AttachmentForm

@Composable
fun NoteAttachmentScreen(
    viewModel: NoteAttachmentViewModel,
    navigateBack: () -> Unit,
) {
    when(val uiState = viewModel.uiState){
        is AttachmentsUiState.Error -> ErrorScreen(
            navigateBack = navigateBack,
            errorMessage = uiState.message
        )
        AttachmentsUiState.Loading -> Text(stringResource(R.string.label_wait))
        is AttachmentsUiState.Success -> NoteAttachmentScaffold(
            navigateBack = navigateBack,
            updateIdx = viewModel::indexUpdate,
            localState = uiState.localState,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteAttachmentScaffold(
    navigateBack: () -> Unit,
    updateIdx: (Int) -> Unit,
    localState: LocalUiState,
    modifier: Modifier = Modifier,
){
    val currentIdx = localState.currentIdx
    val size = localState.attachments.size

    Scaffold(
        modifier = modifier,
        topBar = {
            NoteTopAppBar(
                title = "${currentIdx + 1} ${stringResource(R.string.label_of)} $size",
                canNavigateBack = true,
                navigateBack = navigateBack,
                actionsButtons = {}
            )
        }
    ) { innerPadding ->
        NoteAttachmentBody(
            attachments = localState.attachments,
            currentIdx = currentIdx,
            updateIdx = updateIdx,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}

@Composable
fun NoteAttachmentBody(
    attachments: List<AttachmentForm>,
    updateIdx: (Int) -> Unit,
    currentIdx: Int,
    modifier: Modifier = Modifier,

){
    val pagerState = rememberPagerState(
        pageCount = {attachments.size},
        initialPage = currentIdx
    )

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .collect { page ->
                updateIdx(page)
            }
    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier
    ) { page ->
        val att = attachments[page]
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(att.uri)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    loading = {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.Center)
                        )
                    },
                    error = {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(color = MaterialTheme.colorScheme.surfaceContainer),
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.broken_image_24px),
                                contentDescription = stringResource(R.string.label_not_available),
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    },
                )
            }
        }

    }

}