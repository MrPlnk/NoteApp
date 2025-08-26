package ru.krivenchukartem.noteapp.ui.destinations.note

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.OpenDocument
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import ru.krivenchukartem.noteapp.R
import ru.krivenchukartem.noteapp.domain.model.Attachment
import ru.krivenchukartem.noteapp.ui.composable.ErrorMessage
import ru.krivenchukartem.noteapp.ui.composable.NoteTopAppBar
import ru.krivenchukartem.noteapp.ui.form.AttachmentForm
import java.io.FileNotFoundException


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailsScreen(
    viewModel: NoteDetailsViewModel,
    navigateBack: () -> Unit,
    navigateToNoteAttachmentScreen: (Long, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState = viewModel.uiState
    val context = LocalContext.current
    val s = uiState as? NoteDetailsUIState.Success

    Scaffold(
        modifier = modifier,
        topBar = {
            NoteTopAppBar(
                title = "",
                canNavigateBack = true,
                navigateBack = {
                    viewModel.saveNote()
                    navigateBack()
                },
                actionsButtons = {
                    if (s != null) {
                        IconButton(onClick = { viewModel.changePinState() }) {
                            Icon(
                                imageVector = if (s.noteFullForm.noteForm.isPinned) Icons.Default.Star else Icons.TwoTone.Star,
                                contentDescription = if (s.noteFullForm.noteForm.isPinned) stringResource(R.string.button_pin)
                                                        else stringResource(R.string.button_unpin)
                            )
                        }
                        InsertAttachmentsButton(
                            onPicked = viewModel::addAttachment
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {

                    IconButton(onClick = {
                        viewModel.deleteNote()
                        navigateBack()
                    }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "")
                    }
                    IconButton(onClick = {
                        if (s != null) {
                            val intent =
                                viewModel.buildShareText(context.getString(R.string.empty_note))

                            context.startActivity(
                                Intent.createChooser(
                                    intent,
                                    context.getString(R.string.label_share_note)
                                )
                            )
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = stringResource(R.string.button_share_note))
                    }
                    if (s != null){
                        Text(
                            text = viewModel.getLastDateUpdate(stringResource(R.string.label_last_update)),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            viewModel.saveNote()
                            navigateBack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = stringResource(R.string.button_save)
                        )
                    }
                },
            )
        }
    ) { innerPadding ->
        when (uiState) {
            is NoteDetailsUIState.Error -> ErrorMessage(uiState.message)
            NoteDetailsUIState.Loading -> Text(stringResource(R.string.label_wait))
            is NoteDetailsUIState.Success -> NoteDetailsBody(
                localState = uiState,
                onTitleChanged = viewModel::changeTitle,
                onBodyChanged = viewModel::changeBody,
                onDeleteAttachment = viewModel::delAttachment,
                navigateToNoteAttachmentScreen = navigateToNoteAttachmentScreen,
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(dimensionResource(R.dimen.padding_small))
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteDetailsBody(
    localState: NoteDetailsUIState.Success,
    navigateToNoteAttachmentScreen: (Long, Int) -> Unit,
    onTitleChanged: (String) -> Unit,
    onBodyChanged: (String) -> Unit,
    onDeleteAttachment: (Long) -> Unit,
    modifier: Modifier = Modifier,
){
    val scrollState = rememberScrollState(0)
    val note = localState.noteFullForm.noteForm
    val attachments = localState.noteFullForm.attachmentsForm

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .imePadding()
            .navigationBarsPadding()
    ) {
        AttachmentsList(
            noteId = note.id,
            attachments = attachments,
            onDeleteAttachment = onDeleteAttachment,
            navigateToNoteAttachmentScreen = navigateToNoteAttachmentScreen
        )

        NoteTitleField(
            text = note.title,
            onTextChange = onTitleChanged,
            placeholder = stringResource(R.string.label_title)
        )

        NoteBodyFieldAutoScroll(
            text = note.body,
            onTextChange = onBodyChanged,
            placeholder = stringResource(R.string.label_text)
        )

    }
}

@Composable
fun AttachmentsList(
    noteId: Long,
    attachments: List<AttachmentForm>,
    onDeleteAttachment: (Long) -> Unit,
    navigateToNoteAttachmentScreen: (Long, Int) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 12.dp)
    ) {
        items(attachments.size) { idx ->
            val att = attachments[idx]
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(
                        enabled = noteId != 0L,
                        onClick = {
                            navigateToNoteAttachmentScreen(noteId, idx)
                        }
                    )
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(attachments[idx].uri)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
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
                                contentDescription = "Недоступно",
                                modifier = Modifier.align(Alignment.Center)
                            )
                            IconButton(
                                onClick = { onDeleteAttachment(attachments[idx].id) },
                                modifier = Modifier.align(Alignment.TopEnd)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Удалить")
                            }
                        }
                    },
                    modifier = Modifier.matchParentSize()
                )
                if (noteId == 0L) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.28f))
                    )
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Вложение доступно после сохранения",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(28.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                                shape = RoundedCornerShape(999.dp)
                            )
                            .padding(6.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun NoteTitleField(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = ""
){
    TextField(
        value = text,
        onValueChange = {onTextChange(it)},
        modifier = modifier
            .fillMaxWidth(),
        placeholder = {
            if (text.isEmpty()) {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        },
        textStyle = MaterialTheme.typography.titleLarge.copy(
            color = MaterialTheme.colorScheme.onSurface
        ),
        singleLine = false,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.primary
        ),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Next,
            autoCorrectEnabled = true
        )
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteBodyFieldAutoScroll(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = ""
) {
    val bringIntoView = remember { BringIntoViewRequester() }
    val scope = rememberCoroutineScope()
    var layout by remember { mutableStateOf<TextLayoutResult?>(null) }

    var value by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(text))
    }

    LaunchedEffect(text) {
        if (text != value.text) value = value.copy(text = text)
    }

    Box(
        modifier = modifier
            .padding(dimensionResource(R.dimen.padding_medium))
            .bringIntoViewRequester(bringIntoView)
            .imePadding()
    ) {
        if (value.text.isEmpty()) {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }

        BasicTextField(
            value = value,
            onValueChange = { new ->
                value = new
                if (new.text != text) onTextChange(new.text)
            },
            onTextLayout = { layout = it },
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                autoCorrectEnabled = true
            ),
            maxLines = Int.MAX_VALUE
        )
    }

    LaunchedEffect(value.selection, value.text, layout) {
        val lp = layout ?: return@LaunchedEffect
        val max = lp.layoutInput.text.length
        val offset = value.selection.end.coerceIn(0, max)
        if (max == 0) return@LaunchedEffect
        val rect = lp.getCursorRect(offset).inflate(16f)
        scope.launch { bringIntoView.bringIntoView(rect) }
    }
}

@Composable
fun InsertAttachmentsButton(
    onPicked: (List<Uri>) -> Unit,
    maxItems: Int = 10,
){
    val context = LocalContext.current

    val photosLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia(maxItems)
    ) { uris ->
        if (uris.isNotEmpty()) {
            // Сохраняем права доступа для всех URI
            uris.forEach { uri ->
                try {
                    context.contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (e: Exception) {
                    // Логируем ошибку, но продолжаем обработку
                }
            }
            onPicked(uris)
        }
    }

    val openDocLauncher = rememberLauncherForActivityResult(
        OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            try {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (e: Exception) {
                // Обработка ошибок
            }
            onPicked(listOf(it))
        }
    }

    IconButton(
        onClick = {
            if (PickVisualMedia.isPhotoPickerAvailable(context)) {
                photosLauncher.launch(
                    PickVisualMediaRequest(PickVisualMedia.ImageOnly)
                )
            } else {
                openDocLauncher.launch(arrayOf("image/*"))
            }
        }
    ) {
        Icon(
            painter = painterResource(R.drawable.add_box_24px),
            contentDescription = stringResource(R.string.button_add_attachment)
        )
    }
}
