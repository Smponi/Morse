package com.mouse.mouse.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mouse.mouse.core.domain.model.MorseMode
import com.mouse.mouse.ui.components.*
import com.mouse.mouse.ui.theme.Layout
import com.mouse.mouse.ui.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToHistory: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: HomeViewModel = viewModel { HomeViewModel() }
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Morse",
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onNavigateToHistory) {
                        Text("🕐")
                    }
                },
                actions = {
                    TextButton(onClick = onNavigateToSettings) {
                        Text("⚙️")
                    }
                }
            )
        },
        bottomBar = {
            PlaybackPanel(
                playbackState = state.playbackState,
                enabledChannels = state.enabledChannels,
                onPlayClick = { viewModel.onPlayClick() },
                onChannelToggle = { viewModel.toggleChannel(it) },
                isPlayEnabled = state.isPlayButtonEnabled
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = Layout.screenPaddingHorizontal)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(Spacing.lg)
        ) {
            Spacer(modifier = Modifier.height(Spacing.sm))

            // Mode Selector
            ModeSegmentedControl(
                selectedMode = state.mode,
                onModeSelected = { viewModel.switchMode(it) }
            )

            // Input Card
            when (state.mode) {
                MorseMode.TEXT_TO_MORSE -> {
                    TextInputCard(
                        text = state.inputText,
                        onTextChange = { viewModel.updateInputText(it) }
                    )
                }
                MorseMode.MORSE_TO_TEXT -> {
                    MorseInputCard(
                        onMorseTap = { viewModel.onMorseTap(it) },
                        onDeleteLast = { viewModel.deleteMorseInput() },
                        onClear = { viewModel.clearMorseInput() },
                        currentMorseSequence = state.inputText
                    )
                }
            }

            // Output Card
            OutputCard(
                mode = state.mode,
                outputText = if (state.mode == MorseMode.TEXT_TO_MORSE) {
                    state.outputMorse
                } else {
                    state.outputText
                },
                onCopy = {
                    // TODO: Copy to clipboard
                },
                onShare = {
                    // TODO: Share functionality
                }
            )

            Spacer(modifier = Modifier.height(Spacing.xl))
        }
    }
}
