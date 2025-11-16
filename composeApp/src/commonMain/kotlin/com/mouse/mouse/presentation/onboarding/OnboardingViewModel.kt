package com.mouse.mouse.presentation.onboarding

import androidx.lifecycle.ViewModel
import com.mouse.mouse.core.domain.model.OutputChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class OnboardingState(
    val currentPage: Int = 0,
    val selectedChannels: Set<OutputChannel> = setOf(OutputChannel.VISUAL)
)

class OnboardingViewModel : ViewModel() {
    private val _state = MutableStateFlow(OnboardingState())
    val state: StateFlow<OnboardingState> = _state.asStateFlow()

    fun nextPage() {
        _state.value = _state.value.copy(currentPage = _state.value.currentPage + 1)
    }

    fun toggleChannel(channel: OutputChannel) {
        val currentChannels = _state.value.selectedChannels.toMutableSet()
        if (currentChannels.contains(channel)) {
            currentChannels.remove(channel)
        } else {
            currentChannels.add(channel)
        }
        _state.value = _state.value.copy(selectedChannels = currentChannels)
    }

    fun completeOnboarding() {
        // TODO: Save onboarding completed flag to preferences
        // TODO: Save selected channels to preferences
    }
}
