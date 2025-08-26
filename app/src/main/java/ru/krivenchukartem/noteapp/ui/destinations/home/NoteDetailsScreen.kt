package ru.krivenchukartem.noteapp.ui.destinations.home

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import kotlinx.coroutines.launch
import ru.krivenchukartem.noteapp.R
import ru.krivenchukartem.noteapp.domain.model.Note
import ru.krivenchukartem.noteapp.ui.composable.ErrorMessage
import ru.krivenchukartem.noteapp.ui.composable.NoteTopAppBar


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailsScreen(
    viewModel: NoteDetailsViewModel,
    navigateBack: () -> Unit,
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
    onTitleChanged: (String) -> Unit,
    onBodyChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
){
    val scrollState = rememberScrollState(0)
    val note = localState.noteFullForm.noteForm

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .imePadding()
            .navigationBarsPadding()
    ) {

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
