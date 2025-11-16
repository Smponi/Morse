package com.mouse.mouse.presentation.home

import androidx.lifecycle.ViewModel
import com.mouse.mouse.core.domain.model.MorseMode
import com.mouse.mouse.core.domain.model.OutputChannel
import com.mouse.mouse.core.domain.model.PlaybackState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class HomeState(
    val mode: MorseMode = MorseMode.TEXT_TO_MORSE,
    val inputText: String = "",
    val outputText: String = "",
    val outputMorse: String = "",
    val enabledChannels: Set<OutputChannel> = setOf(OutputChannel.VISUAL),
    val playbackState: PlaybackState = PlaybackState.IDLE,
    val isPlayButtonEnabled: Boolean = false
)

class HomeViewModel : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    fun switchMode(mode: MorseMode) {
        _state.value = _state.value.copy(
            mode = mode,
            inputText = "",
            outputText = "",
            outputMorse = ""
        )
    }

    fun updateInputText(text: String) {
        _state.value = _state.value.copy(inputText = text)
        
        // TODO: Call business layer to convert text to morse
        // For now, just enable play button if text is not empty
        _state.value = _state.value.copy(isPlayButtonEnabled = text.isNotEmpty())
    }

    fun updateMorseInput(morse: String) {
        // TODO: Call business layer to validate and convert morse to text
    }

    fun toggleChannel(channel: OutputChannel) {
        val currentChannels = _state.value.enabledChannels.toMutableSet()
        if (currentChannels.contains(channel)) {
            currentChannels.remove(channel)
        } else {
            currentChannels.add(channel)
        }
        _state.value = _state.value.copy(enabledChannels = currentChannels)
    }

    fun onPlayClick() {
        if (_state.value.playbackState == PlaybackState.IDLE) {
            startPlayback()
        } else {
            stopPlayback()
        }
    }

    private fun startPlayback() {
        // TODO: Call business layer to start playback with enabled channels
        _state.value = _state.value.copy(playbackState = PlaybackState.PLAYING)
    }

    private fun stopPlayback() {
        // TODO: Call business layer to stop playback
        _state.value = _state.value.copy(playbackState = PlaybackState.IDLE)
    }

    fun onMorseTap(pressDurationMs: Long) {
        // TODO: Call business layer to interpret tap as dot/dash
        // TODO: Update outputText with decoded morse
    }

    fun deleteMorseInput() {
        // TODO: Delete last morse character
    }

    fun clearMorseInput() {
        _state.value = _state.value.copy(
            inputText = "",
            outputText = "",
            outputMorse = ""
        )
    }
}
