package ru.krivenchukartem.noteapp.ui.destinations.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToNoteDetails: (Long) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            NoteTopAppBar(
                title = stringResource(R.string.home_screen_title),
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
            is HomeUiState.Error -> {}
            HomeUiState.Loading -> {}
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
                items(homeLocalState.notes.size){ idx->
                    NoteWidget(
                        homeLocalState.notes[idx],
                        navigateToNoteDetails = navigateToNoteDetails,
                    )
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
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = note.body,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}