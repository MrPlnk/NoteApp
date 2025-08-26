package ru.krivenchukartem.noteapp.ui.destinations.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import ru.krivenchukartem.noteapp.ui.composable.NoteTopAppBar
import ru.krivenchukartem.noteapp.R
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import ru.krivenchukartem.noteapp.ui.composable.ErrorMessage


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToNoteDetails: (Long) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    val queryUiState by viewModel.searchState.collectAsState()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(x = (-8).dp),
                        shape = RoundedCornerShape(100.dp),
                        value = queryUiState.query,
                        onValueChange = {viewModel.changeQuery(it)},
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            imeAction = ImeAction.Done,
                            autoCorrectEnabled = true
                        ),
                        singleLine = true,
                        placeholder = {
                            if (queryUiState.query.isEmpty()) {
                                Text("Искать в заметках")
                            }
                        },
                    )
                },
                navigationIcon = {},
                actions = {},
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {navigateToNoteDetails(0L)}
            ){
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.button_add)
                )
            }
        }


    ) { contentPadding ->
        when (val state = homeUiState){
            is HomeUiState.Success -> HomeBody(
                homeLocalState = state.local,
                navigateToNoteDetails = navigateToNoteDetails,
                modifier = Modifier
                    .padding(contentPadding)
                    .padding(dimensionResource(R.dimen.padding_small))
                    .fillMaxWidth()
            )
            is HomeUiState.Error -> ErrorMessage(state.message)
            HomeUiState.Loading -> Text(stringResource(R.string.label_wait))
        }
    }
}

@Composable
fun HomeBody(
    homeLocalState: HomeLocalState,
    navigateToNoteDetails: (Long) -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (homeLocalState.notes.isEmpty()){
            Text(stringResource(R.string.label_its_empty))
        }
        else{
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
            ) {
                val firstUnpinnedIndex = homeLocalState.firstUnpinnedIndex

                if (firstUnpinnedIndex == -1){
                    item {
                        Text(
                            text = stringResource(R.string.label_pinned_notes)
                        )
                    }
                    items(homeLocalState.notes.size){ idx->
                        val note = homeLocalState.notes[idx]

                        NoteWidget(
                            note,
                            navigateToNoteDetails = navigateToNoteDetails,
                        )
                    }

                }
                else if (firstUnpinnedIndex == 0){
                    items(homeLocalState.notes.size){ idx->
                        val note = homeLocalState.notes[idx]
                        NoteWidget(
                            note,
                            navigateToNoteDetails = navigateToNoteDetails,
                        )
                    }
                }
                else{
                    item {
                        Text(
                            text = stringResource(R.string.label_pinned_notes)
                        )
                    }
                    items(firstUnpinnedIndex) { idx ->
                    val note = homeLocalState.notes[idx]
                        NoteWidget(
                            note,
                            navigateToNoteDetails = navigateToNoteDetails,
                        )
                    }
                    item {
                        Text(
                            text = stringResource(R.string.label_unpinned_notes)
                        )
                    }
                    items(homeLocalState.notes.size - firstUnpinnedIndex){ unmovedIdx ->
                        val idx = unmovedIdx + firstUnpinnedIndex
                        val note = homeLocalState.notes[idx]
                        NoteWidget(
                            note,
                            navigateToNoteDetails = navigateToNoteDetails,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NoteWidget(
    note: NoteState,
    navigateToNoteDetails: (Long) -> Unit,
    modifier: Modifier = Modifier,
){
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .sizeIn(maxHeight = 200.dp)
            .clickable(onClick = {navigateToNoteDetails(note.id)}),
    ) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
        ) {
            if (note.title.isNotEmpty()) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            if (note.body.isNotEmpty()) {
                Text(
                    text = note.body,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}