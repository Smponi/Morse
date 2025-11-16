package com.mouse.mouse.presentation.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mouse.mouse.core.domain.model.OutputChannel
import androidx.compose.ui.unit.dp
import com.mouse.mouse.ui.theme.Layout
import com.mouse.mouse.ui.theme.Radius
import com.mouse.mouse.ui.theme.Spacing
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    onOnboardingComplete: () -> Unit,
    viewModel: OnboardingViewModel = viewModel { OnboardingViewModel() }
) {
    val state by viewModel.state.collectAsState()
    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            OnboardingBottomBar(
                currentPage = pagerState.currentPage,
                totalPages = 3,
                onNextClick = {
                    if (pagerState.currentPage < 2) {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    } else {
                        viewModel.completeOnboarding()
                        onOnboardingComplete()
                    }
                },
                onSkipClick = {
                    viewModel.completeOnboarding()
                    onOnboardingComplete()
                }
            )
        }
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) { page ->
            when (page) {
                0 -> OnboardingPage1()
                1 -> OnboardingPage2()
                2 -> OnboardingPage3(
                    selectedChannels = state.selectedChannels,
                    onChannelToggle = { viewModel.toggleChannel(it) }
                )
            }
        }
    }
}

@Composable
fun OnboardingPage1() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Layout.screenPaddingHorizontal),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Morse als Rhythmus.",
            style = MaterialTheme.typography.displayLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Spacing.xl))

        Text(
            text = "Übersetze Text ↔ Morse und erlebe ihn als Licht, Vibration, Sound oder visuell.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun OnboardingPage2() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Layout.screenPaddingHorizontal),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "HI",
            style = MaterialTheme.typography.displayLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Spacing.xl))

        Text(
            text = ".... ..",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(Spacing.xl))

        MorseTimelinePreview()

        Spacer(modifier = Modifier.height(Spacing.xl))

        Text(
            text = "Morse-Code ist ein Rhythmus aus kurzen und langen Signalen",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun MorseTimelinePreview() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(Spacing.xl)
            .padding(horizontal = Spacing.xxl),
        horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
    ) {
        // H: ....
        repeat(4) {
            Box(
                modifier = Modifier
                    .width(Spacing.md)
                    .fillMaxHeight()
                    .background(
                        MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.small
                    )
            )
        }

        Spacer(modifier = Modifier.width(Spacing.lg))

        // I: ..
        repeat(2) {
            Box(
                modifier = Modifier
                    .width(Spacing.md)
                    .fillMaxHeight()
                    .background(
                        MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.small
                    )
            )
        }
    }
}

@Composable
fun OnboardingPage3(
    selectedChannels: Set<OutputChannel>,
    onChannelToggle: (OutputChannel) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Layout.screenPaddingHorizontal),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Wähle deine Ausgabekanäle",
            style = MaterialTheme.typography.displayLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Spacing.xl))

        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.md)
        ) {
            ChannelCheckbox(
                channel = OutputChannel.FLASH,
                label = "Blitz",
                isSelected = selectedChannels.contains(OutputChannel.FLASH),
                onToggle = { onChannelToggle(OutputChannel.FLASH) }
            )

            ChannelCheckbox(
                channel = OutputChannel.VIBRATION,
                label = "Vibration",
                isSelected = selectedChannels.contains(OutputChannel.VIBRATION),
                onToggle = { onChannelToggle(OutputChannel.VIBRATION) }
            )

            ChannelCheckbox(
                channel = OutputChannel.SOUND,
                label = "Sound",
                isSelected = selectedChannels.contains(OutputChannel.SOUND),
                onToggle = { onChannelToggle(OutputChannel.SOUND) }
            )

            ChannelCheckbox(
                channel = OutputChannel.VISUAL,
                label = "Visuell",
                isSelected = selectedChannels.contains(OutputChannel.VISUAL),
                onToggle = { onChannelToggle(OutputChannel.VISUAL) }
            )
        }

        Spacer(modifier = Modifier.height(Spacing.xxl))

        Text(
            text = "Flash kann hell und schnell blinken. Du kannst es jederzeit deaktivieren.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ChannelCheckbox(
    channel: OutputChannel,
    label: String,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isSelected,
            onCheckedChange = { onToggle() }
        )
        Spacer(modifier = Modifier.width(Spacing.sm))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun OnboardingBottomBar(
    currentPage: Int,
    totalPages: Int,
    onNextClick: () -> Unit,
    onSkipClick: () -> Unit
) {
    Surface(
        tonalElevation = MaterialTheme.typography.bodySmall.lineHeight.value.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Layout.screenPaddingHorizontal)
                .padding(vertical = Spacing.lg),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (currentPage < totalPages - 1) {
                TextButton(onClick = onSkipClick) {
                    Text("Überspringen")
                }
            } else {
                Spacer(modifier = Modifier.width(1.dp))
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
            ) {
                repeat(totalPages) { index ->
                    Box(
                        modifier = Modifier
                            .size(Spacing.sm)
                            .background(
                                color = if (index == currentPage) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.surfaceVariant
                                },
                                shape = MaterialTheme.shapes.small
                            )
                    )
                }
            }

            Button(onClick = onNextClick) {
                Text(
                    text = if (currentPage < totalPages - 1) "Weiter" else "Starten"
                )
            }
        }
    }
}
