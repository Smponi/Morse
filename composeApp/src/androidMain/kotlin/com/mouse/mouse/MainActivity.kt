package com.mouse.mouse

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraManager
import android.os.*
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

// ---------------------------------------------------------
// DATA MODELS
// ---------------------------------------------------------

data class MorseRecord(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val morse: String,
    val timestamp: Long = System.currentTimeMillis(),
    var isFavorite: Boolean = false
)

enum class OutputMode {
    VIBRATION, LIGHT, SOUND
}

// ---------------------------------------------------------
// LOGIC / VIEWMODEL
// ---------------------------------------------------------

class MorseSuiteViewModel : ViewModel() {
    var inputText by mutableStateOf("")
    var morseOutput by mutableStateOf("")
    
    val activeModes = mutableStateListOf(OutputMode.VIBRATION, OutputMode.LIGHT)

    private val _history = mutableStateListOf<MorseRecord>()
    val history: List<MorseRecord> get() = _history

    var isPlaying by mutableStateOf(false)
    var isSignalActive by mutableStateOf(false)

    var isScanning by mutableStateOf(false)

    fun updateText(newText: String) {
        inputText = newText
        morseOutput = translateToMorse(newText)
    }

    fun toggleMode(mode: OutputMode) {
        if (activeModes.contains(mode)) {
            activeModes.remove(mode)
        } else {
            activeModes.add(mode)
        }
    }

    fun addToHistory() {
        if (inputText.isNotBlank()) {
            _history.add(0, MorseRecord(text = inputText, morse = morseOutput))
        }
    }

    fun toggleFavorite(recordId: String) {
        val index = _history.indexOfFirst { it.id == recordId }
        if (index != -1) {
            val record = _history[index]
            _history[index] = record.copy(isFavorite = !record.isFavorite)
        }
    }

    private fun translateToMorse(text: String): String {
        val charToMorse = mapOf(
            'A' to ".-", 'B' to "-...", 'C' to "-.-.", 'D' to "-..", 'E' to ".",
            'F' to "..-.", 'G' to "--.", 'H' to "....", 'I' to "..", 'J' to ".---",
            'K' to "-.-", 'L' to ".-..", 'M' to "--", 'N' to "-.", 'O' to "---",
            'P' to ".--.", 'Q' to "--.-", 'R' to ".-.", 'S' to "...", 'T' to "-",
            'U' to "..-", 'V' to "...-", 'W' to ".--", 'X' to "-..-", 'Y' to "-.--",
            'Z' to "--..", '0' to "-----", '1' to ".----", ' ' to "/"
        )
        return text.uppercase().map { charToMorse[it] ?: "" }.joinToString(" ")
    }

    suspend fun transmitSignal(context: Context) {
        if (isPlaying || morseOutput.isEmpty()) return
        
        addToHistory()
        
        isPlaying = true
        
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
        
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraId = try { cameraManager.cameraIdList.firstOrNull() } catch (e: Exception) { null }

        val timeUnit = 120L

        morseOutput.forEach { char ->
            if (!isPlaying) { signalOff(cameraManager, cameraId); return }

            when (char) {
                '.' -> playSignal(timeUnit, vibrator, cameraManager, cameraId)
                '-' -> playSignal(timeUnit * 3, vibrator, cameraManager, cameraId)
                ' ', '/' -> delay(timeUnit * 3)
            }
            delay(timeUnit) 
        }
        
        isPlaying = false
        signalOff(cameraManager, cameraId)
    }

    private suspend fun playSignal(
        duration: Long, 
        vibrator: Vibrator, 
        camMan: CameraManager, 
        camId: String?
    ) {
        isSignalActive = true
        
        if (activeModes.contains(OutputMode.VIBRATION)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(duration)
            }
        }

        if (activeModes.contains(OutputMode.LIGHT) && camId != null) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    camMan.setTorchMode(camId, true)
                }
            } catch (e: Exception) { }
        }

        delay(duration)
        
        signalOff(camMan, camId)
    }

    private fun signalOff(camMan: CameraManager, camId: String?) {
        isSignalActive = false
        if (activeModes.contains(OutputMode.LIGHT) && camId != null) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    camMan.setTorchMode(camId, false)
                }
            } catch (e: Exception) {}
        }
    }

    fun stopTransmission() {
        isPlaying = false
        isSignalActive = false
    }
}

// ---------------------------------------------------------
// UI LAYER
// ---------------------------------------------------------

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Color(0xFF80D8FF),
                    secondary = Color(0xFFFFD180),
                    background = Color(0xFF101418),
                    surface = Color(0xFF1C2229),
                    onSurface = Color(0xFFE0E0E0),
                    surfaceVariant = Color(0xFF2B343D)
                )
            ) {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: MorseSuiteViewModel = viewModel()) {
    var currentScreen by remember { mutableStateOf(Screen.Transmitter) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.background,
                tonalElevation = 8.dp
            ) {
                Screen.values().forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.label) },
                        selected = currentScreen == screen,
                        onClick = { currentScreen = screen },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            selectedIconColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            Crossfade(targetState = currentScreen, label = "ScreenNav") { screen ->
                when (screen) {
                    Screen.Transmitter -> TransmitterRoute(viewModel)
                    Screen.History -> HistoryRoute(viewModel, showFavoritesOnly = false)
                    Screen.Favorites -> HistoryRoute(viewModel, showFavoritesOnly = true)
                }
            }
        }
    }
}

enum class Screen(val label: String, val icon: ImageVector) {
    Transmitter("Transmit", Icons.Rounded.WifiTethering),
    History("History", Icons.Rounded.History),
    Favorites("Favorites", Icons.Rounded.Star)
}

// ---------------------------------------------------------
// SCREENS
// ---------------------------------------------------------

@Composable
fun TransmitterRoute(viewModel: MorseSuiteViewModel) {
    var showCameraScanner by remember { mutableStateOf(false) }

    if (showCameraScanner) {
        CameraScannerMockUI(
            onScanResult = { resultText ->
                viewModel.updateText(resultText)
                showCameraScanner = false
            },
            onClose = { showCameraScanner = false }
        )
    } else {
        TransmitterScreen(
            viewModel = viewModel,
            onOpenCamera = { showCameraScanner = true }
        )
    }
}

@Composable
fun TransmitterScreen(
    viewModel: MorseSuiteViewModel,
    onOpenCamera: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) Toast.makeText(context, "Flashlight Access Granted", Toast.LENGTH_SHORT).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        SignalVisualizer(isSignalActive = viewModel.isSignalActive)

        InputCard(
            text = viewModel.inputText,
            onTextChange = viewModel::updateText,
            onCameraClick = onOpenCamera
        )

        OutputCard(morseCode = viewModel.morseOutput)

        Spacer(modifier = Modifier.weight(1f))

        OutputSelector(
            modes = viewModel.activeModes,
            onToggle = { mode ->
                if (mode == OutputMode.LIGHT) {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) 
                        != PackageManager.PERMISSION_GRANTED) {
                        launcher.launch(Manifest.permission.CAMERA)
                    }
                }
                viewModel.toggleMode(mode) 
            }
        )

        PlayButton(
            isPlaying = viewModel.isPlaying,
            onPlay = { scope.launch { viewModel.transmitSignal(context) } },
            onStop = viewModel::stopTransmission,
            enabled = viewModel.morseOutput.isNotEmpty()
        )
    }
}

@Composable
fun HistoryRoute(viewModel: MorseSuiteViewModel, showFavoritesOnly: Boolean) {
    val items = if (showFavoritesOnly) {
        viewModel.history.filter { it.isFavorite }
    } else {
        viewModel.history
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = if (showFavoritesOnly) "FAVORITES" else "RECENT TRANSMISSIONS",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp, start = 8.dp)
        )

        if (items.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    "No records yet.",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(items, key = { it.id }) { record ->
                    HistoryItem(
                        record = record,
                        onFavoriteToggle = { viewModel.toggleFavorite(record.id) },
                        onLoad = { viewModel.updateText(record.text) }
                    )
                }
            }
        }
    }
}

// ---------------------------------------------------------
// COMPONENT LIBRARY
// ---------------------------------------------------------

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InputCard(text: String, onTextChange: (String) -> Unit, onCameraClick: () -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("TEXT", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                
                IconButton(onClick = onCameraClick) {
                    Icon(
                        Icons.Rounded.PhotoCamera,
                        contentDescription = "Scan",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            TextField(
                value = text,
                onValueChange = onTextChange,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                textStyle = MaterialTheme.typography.headlineSmall,
                placeholder = { Text("Type to translate...", color = Color.Gray.copy(0.5f)) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                })
            )
        }
    }
}

@Composable
fun OutputCard(morseCode: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.Black.copy(alpha = 0.3f))
            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f), RoundedCornerShape(24.dp))
            .padding(24.dp)
            .heightIn(min = 100.dp),
        contentAlignment = Alignment.Center
    ) {
        if (morseCode.isEmpty()) {
            Text(
                "Waiting for input",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray.copy(alpha = 0.5f)
            )
        } else {
            Text(
                text = morseCode,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                ),
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutputSelector(
    modes: List<OutputMode>,
    onToggle: (OutputMode) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("OUTPUT METHOD", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutputMode.values().forEach { mode ->
                val selected = modes.contains(mode)
                FilterChip(
                    selected = selected,
                    onClick = { onToggle(mode) },
                    label = { Text(mode.name) },
                    leadingIcon = {
                        Icon(
                            imageVector = when(mode) {
                                OutputMode.VIBRATION -> Icons.Rounded.Vibration
                                OutputMode.LIGHT -> Icons.Rounded.FlashOn
                                OutputMode.SOUND -> Icons.Rounded.VolumeUp
                            },
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                        selectedLabelColor = MaterialTheme.colorScheme.primary,
                        selectedLeadingIconColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }
    }
}

@Composable
fun PlayButton(isPlaying: Boolean, onPlay: () -> Unit, onStop: () -> Unit, enabled: Boolean) {
    val haptic = LocalHapticFeedback.current
    
    Button(
        onClick = {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            if (isPlaying) onStop() else onPlay()
        },
        enabled = enabled || isPlaying,
        modifier = Modifier.fillMaxWidth().height(64.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isPlaying) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Icon(if (isPlaying) Icons.Rounded.Stop else Icons.Rounded.PlayArrow, null)
        Spacer(Modifier.width(8.dp))
        Text(
            if (isPlaying) "STOP TRANSMISSION" else "TRANSMIT SIGNAL",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
fun HistoryItem(record: MorseRecord, onFavoriteToggle: () -> Unit, onLoad: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onLoad() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = record.text,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = record.morse,
                    style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                    color = MaterialTheme.colorScheme.secondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(record.timestamp)),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
            IconButton(onClick = onFavoriteToggle) {
                Icon(
                    imageVector = if (record.isFavorite) Icons.Rounded.Star else Icons.Outlined.StarBorder,
                    contentDescription = "Fav",
                    tint = if (record.isFavorite) MaterialTheme.colorScheme.secondary else Color.Gray
                )
            }
        }
    }
}

@Composable
fun SignalVisualizer(isSignalActive: Boolean) {
    val color by animateColorAsState(
        if (isSignalActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
        label = "color"
    )
    val scale by animateFloatAsState(if (isSignalActive) 1.15f else 1f, label = "scale")

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(100.dp)
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .scale(scale)
                .clip(CircleShape)
                .background(color)
                .border(4.dp, Color.White.copy(alpha = 0.1f), CircleShape)
        ) {
            Icon(
                Icons.Rounded.Bolt, 
                null, 
                tint = if (isSignalActive) Color.White else Color.White.copy(0.3f),
                modifier = Modifier.align(Alignment.Center).size(32.dp)
            )
        }
    }
}

// ---------------------------------------------------------
// CAMERA SCANNER MOCK UI
// ---------------------------------------------------------

@Composable
fun CameraScannerMockUI(onScanResult: (String) -> Unit, onClose: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "scan")
    val scanLineY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "scanline"
    )

    LaunchedEffect(Unit) {
        delay(2500)
        onScanResult("SOS HELP")
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        Box(modifier = Modifier.fillMaxSize().background(Color.DarkGray)) {
            Text(
                "CAMERA PREVIEW", 
                color = Color.White.copy(0.2f), 
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp).background(Color.Black.copy(0.5f), CircleShape),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onClose) {
                    Icon(Icons.Rounded.Close, null, tint = Color.White)
                }
                Text("SCANNING TEXT...", color = Color.White, style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.size(48.dp))
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(32.dp)
            ) {
                Box(Modifier.matchParentSize().border(2.dp, Color.White.copy(0.5f), RoundedCornerShape(12.dp)))
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .align(Alignment.TopCenter)
                        .offset(y = (400 * scanLineY).dp)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color.Transparent, Color(0xFF00E5FF), Color.Transparent)
                            )
                        )
                )
            }

            Text(
                "Align text or morse code within frame",
                color = Color.White.copy(0.7f),
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 32.dp)
            )
        }
    }
}
