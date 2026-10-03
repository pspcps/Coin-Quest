package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.random.Random

enum class ArcadeGameType {
    BLOCK_FALL,
    SPEED_MATH,
    PATTERN_BRAIN
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PuzzleArcadeScreen(
    profile: UserProfile,
    onBack: () -> Unit,
    onAwardCoins: (coins: Int, reason: String) -> Unit
) {
    var selectedGame by remember { mutableStateOf(ArcadeGameType.BLOCK_FALL) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = when (selectedGame) {
                                ArcadeGameType.BLOCK_FALL -> "🧱 Block Line Arcade"
                                ArcadeGameType.SPEED_MATH -> "⚡ Math Sprint"
                                ArcadeGameType.PATTERN_BRAIN -> "🧠 Logic Pattern"
                            },
                            fontWeight = FontWeight.Black,
                            fontSize = 19.sp,
                            color = BoldTextPrimary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("arcade_back_btn")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = BoldTextPrimary)
                    }
                },
                actions = {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = CoinGoldBg,
                        border = androidx.compose.foundation.BorderStroke(1.dp, CoinGoldLight),
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text("🪙", fontSize = 14.sp)
                            Text(
                                "${profile.unassignedCoins + profile.spendJarCoins + profile.saveJarCoins + profile.giveJarCoins + profile.safetyJarCoins + profile.bankCoins}",
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp,
                                color = CoinGoldDark
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BoldSurface,
                    titleContentColor = BoldTextPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BoldCanvas)
                .padding(padding)
        ) {
            // Mode selector tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf(
                    ArcadeGameType.BLOCK_FALL to "🧱 Block Line",
                    ArcadeGameType.SPEED_MATH to "⚡ Math Sprint",
                    ArcadeGameType.PATTERN_BRAIN to "🧠 Logic Brain"
                ).forEach { (type, title) ->
                    val isSelected = selectedGame == type
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedGame = type },
                        label = {
                            Text(
                                title,
                                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                                fontSize = 12.sp
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SaveBlueDark,
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            when (selectedGame) {
                ArcadeGameType.BLOCK_FALL -> {
                    BlockFallingGame(
                        onEarnCoins = { coins ->
                            onAwardCoins(coins, "Mastered Block Line Puzzle!")
                        }
                    )
                }
                ArcadeGameType.SPEED_MATH -> {
                    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                        SpeedMathGame(
                            childAge = profile.childAge,
                            onEarnCoins = { coins ->
                                onAwardCoins(coins, "Speed Math Champion!")
                            }
                        )
                    }
                }
                ArcadeGameType.PATTERN_BRAIN -> {
                    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                        PatternBrainGame(
                            onEarnCoins = { coins ->
                                onAwardCoins(coins, "Logic Pattern Wizard!")
                            }
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 1. FULL-SCREEN BLOCK LINE GAME WITH ROBUST GAME-OVER LOGIC
// -------------------------------------------------------------
data class TetroPiece(
    val shape: List<Pair<Int, Int>>,
    val colorIndex: Int,
    val color: Color
)

val TETRO_PIECES = listOf(
    // I - Cyan (4 horizontal blocks)
    TetroPiece(listOf(0 to 0, 1 to 0, 2 to 0, 3 to 0), 1, Color(0xFF06B6D4)),
    // O - Yellow (2x2 square)
    TetroPiece(listOf(0 to 0, 1 to 0, 0 to 1, 1 to 1), 2, Color(0xFFFACC15)),
    // T - Purple
    TetroPiece(listOf(1 to 0, 0 to 1, 1 to 1, 2 to 1), 3, Color(0xFFA855F7)),
    // S - Green
    TetroPiece(listOf(1 to 0, 2 to 0, 0 to 1, 1 to 1), 4, Color(0xFF22C55E)),
    // Z - Red
    TetroPiece(listOf(0 to 0, 1 to 0, 1 to 1, 2 to 1), 5, Color(0xFFEF4444)),
    // J - Blue
    TetroPiece(listOf(0 to 0, 0 to 1, 1 to 1, 2 to 1), 6, Color(0xFF3B82F6)),
    // L - Orange
    TetroPiece(listOf(2 to 0, 0 to 1, 1 to 1, 2 to 1), 7, Color(0xFFF97316))
)

fun getRandomTetro(): TetroPiece = TETRO_PIECES.random()

@Composable
fun BlockFallingGame(
    onEarnCoins: (Int) -> Unit
) {
    val rows = 20
    val cols = 10

    var board by remember { mutableStateOf(List(rows) { IntArray(cols) { 0 } }) }
    var currentPiece by remember { mutableStateOf(getRandomTetro()) }
    var nextPiece by remember { mutableStateOf(getRandomTetro()) }
    var pieceX by remember { mutableStateOf(3) }
    var pieceY by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var linesCleared by remember { mutableStateOf(0) }
    var isGameOver by remember { mutableStateOf(false) }
    var isRunning by remember { mutableStateOf(false) }
    var isPaused by remember { mutableStateOf(false) }
    var dropSpeedMs by remember { mutableStateOf(650L) }
    var earnedCoinsThisGame by remember { mutableStateOf(0) }
    var lastClearedCount by remember { mutableStateOf(0) }

    fun canFitInGrid(grid: List<IntArray>, checkX: Int, checkY: Int, shape: List<Pair<Int, Int>>): Boolean {
        for ((dx, dy) in shape) {
            val tx = checkX + dx
            val ty = checkY + dy
            if (tx < 0 || tx >= cols || ty >= rows) return false
            if (ty >= 0 && grid[ty][tx] != 0) return false
        }
        return true
    }

    fun canFit(checkX: Int, checkY: Int, shape: List<Pair<Int, Int>>): Boolean {
        return canFitInGrid(board, checkX, checkY, shape)
    }

    fun resetGame() {
        board = List(rows) { IntArray(cols) { 0 } }
        score = 0
        linesCleared = 0
        isGameOver = false
        earnedCoinsThisGame = 0
        lastClearedCount = 0
        val p1 = getRandomTetro()
        val p2 = getRandomTetro()
        currentPiece = p1
        nextPiece = p2
        pieceX = (cols - 4) / 2
        pieceY = 0
        dropSpeedMs = 650L
        isPaused = false
        isRunning = true
    }

    fun lockPieceAndNext() {
        val newBoard = board.map { it.clone() }.toMutableList()

        // Stamp current piece onto board
        for ((dx, dy) in currentPiece.shape) {
            val tx = pieceX + dx
            val ty = pieceY + dy
            if (ty in 0 until rows && tx in 0 until cols) {
                newBoard[ty][tx] = currentPiece.colorIndex
            }
        }

        // Check for full lines (clearing bottom to top)
        val remainingRows = newBoard.filterNot { row -> row.all { it != 0 } }.toMutableList()
        val cleared = rows - remainingRows.size

        while (remainingRows.size < rows) {
            remainingRows.add(0, IntArray(cols) { 0 })
        }

        // Synchronously update local board state
        board = remainingRows

        if (cleared > 0) {
            linesCleared += cleared
            lastClearedCount = cleared
            val pts = when (cleared) {
                1 -> 100
                2 -> 250
                3 -> 500
                4 -> 1000
                else -> cleared * 150
            }
            score += pts

            // Award financial coins for line clears!
            val coinsWon = when (cleared) {
                1 -> 2
                2 -> 5
                3 -> 9
                else -> 15
            }
            earnedCoinsThisGame += coinsWon
            onEarnCoins(coinsWon)

            // Speed up slightly as player clears lines
            dropSpeedMs = (650L - (linesCleared * 12L)).coerceAtLeast(180L)
        }

        // Spawn next piece
        val spawnX = (cols - 4) / 2
        val spawnY = 0
        val incoming = nextPiece
        nextPiece = getRandomTetro()

        // Game Over ONLY happens when the newly spawned piece cannot fit at the top of the new board!
        if (canFitInGrid(remainingRows, spawnX, spawnY, incoming.shape)) {
            currentPiece = incoming
            pieceX = spawnX
            pieceY = spawnY
        } else {
            // Reached the very top and no space left for new block
            isGameOver = true
            isRunning = false
            if (score >= 200) {
                onEarnCoins(3)
            }
        }
    }

    fun rotatePiece() {
        if (!isRunning || isGameOver || isPaused) return
        val currentShape = currentPiece.shape
        // Rotate 90 degrees clockwise
        val rotated = currentShape.map { (x, y) ->
            (3 - y) to x
        }

        // Normalize to 0-indexed top-left
        val minX = rotated.minOf { it.first }
        val minY = rotated.minOf { it.second }
        val normalized = rotated.map { (x, y) -> (x - minX) to (y - minY) }

        // Test wall kicks: center, shift left 1, shift right 1, shift left 2, shift right 2
        val kickOffsets = listOf(0, -1, 1, -2, 2)
        for (offset in kickOffsets) {
            if (canFit(pieceX + offset, pieceY, normalized)) {
                pieceX += offset
                currentPiece = currentPiece.copy(shape = normalized)
                return
            }
        }
    }

    fun hardDrop() {
        if (!isRunning || isGameOver || isPaused) return
        var dropY = pieceY
        while (canFit(pieceX, dropY + 1, currentPiece.shape)) {
            dropY += 1
        }
        pieceY = dropY
        lockPieceAndNext()
    }

    // Auto drop loop
    LaunchedEffect(isRunning, isGameOver, isPaused, pieceY, pieceX, dropSpeedMs) {
        if (isRunning && !isGameOver && !isPaused) {
            delay(dropSpeedMs)
            if (canFit(pieceX, pieceY + 1, currentPiece.shape)) {
                pieceY += 1
            } else {
                lockPieceAndNext()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Compact Full-Screen Game HUD
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = CoinGoldBg,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CoinGoldLight)
                ) {
                    Text(
                        "🏆 Score: $score",
                        fontWeight = FontWeight.Black,
                        color = CoinGoldDark,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Surface(
                    color = GardenGreenMint,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GardenGreenLight)
                ) {
                    Text(
                        "⚡ Lines: $linesCleared",
                        fontWeight = FontWeight.Black,
                        color = GardenGreenDark,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Next Piece Box
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text("NEXT:", fontSize = 11.sp, fontWeight = FontWeight.Black, color = BoldTextSecondary)
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF0F172A))
                        .border(1.dp, Color(0xFF334155), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(26.dp)) {
                        val cellSize = size.width / 4f
                        for ((dx, dy) in nextPiece.shape) {
                            drawRect(
                                color = nextPiece.color,
                                topLeft = Offset(dx * cellSize + 1f, dy * cellSize + 1f),
                                size = Size(cellSize - 2f, cellSize - 2f)
                            )
                        }
                    }
                }
            }
        }

        // Responsive Full Screen Game Canvas Box
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF0A0F1D))
                .border(2.dp, Color(0xFF1E293B), RoundedCornerShape(16.dp))
                .pointerInput(isRunning, isGameOver, isPaused) {
                    var totalDragX = 0f
                    var totalDragY = 0f
                    detectDragGestures(
                        onDragStart = {
                            totalDragX = 0f
                            totalDragY = 0f
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            if (!isRunning || isGameOver || isPaused) return@detectDragGestures
                            totalDragX += dragAmount.x
                            totalDragY += dragAmount.y

                            val stepThreshold = 36f
                            if (totalDragX > stepThreshold) {
                                if (canFit(pieceX + 1, pieceY, currentPiece.shape)) pieceX += 1
                                totalDragX = 0f
                            } else if (totalDragX < -stepThreshold) {
                                if (canFit(pieceX - 1, pieceY, currentPiece.shape)) pieceX -= 1
                                totalDragX = 0f
                            }

                            if (totalDragY > stepThreshold * 1.6f) {
                                if (canFit(pieceX, pieceY + 1, currentPiece.shape)) {
                                    pieceY += 1
                                }
                                totalDragY = 0f
                            }
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        if (isRunning && !isGameOver && !isPaused) {
                            rotatePiece()
                        }
                    }
            ) {
                val cellW = size.width / cols
                val cellH = size.height / rows

                // Draw subtle grid lines
                for (r in 0..rows) {
                    drawLine(
                        color = Color(0xFF1E293B).copy(alpha = 0.6f),
                        start = Offset(0f, r * cellH),
                        end = Offset(size.width, r * cellH),
                        strokeWidth = 0.8f
                    )
                }
                for (c in 0..cols) {
                    drawLine(
                        color = Color(0xFF1E293B).copy(alpha = 0.6f),
                        start = Offset(c * cellW, 0f),
                        end = Offset(c * cellW, size.height),
                        strokeWidth = 0.8f
                    )
                }

                // Draw settled locked blocks on board
                for (r in 0 until rows) {
                    for (c in 0 until cols) {
                        val colorIdx = board[r][c]
                        if (colorIdx > 0) {
                            val blockColor = TETRO_PIECES.firstOrNull { it.colorIndex == colorIdx }?.color ?: Color(0xFF38BDF8)
                            drawRoundRect(
                                color = blockColor,
                                topLeft = Offset(c * cellW + 1.2f, r * cellH + 1.2f),
                                size = Size(cellW - 2.4f, cellH - 2.4f),
                                cornerRadius = CornerRadius(3f, 3f)
                            )
                            // 3D bevel specular highlight
                            drawRoundRect(
                                color = Color.White.copy(alpha = 0.4f),
                                topLeft = Offset(c * cellW + 2f, r * cellH + 2f),
                                size = Size((cellW - 4f) * 0.45f, (cellH - 4f) * 0.45f),
                                cornerRadius = CornerRadius(2f, 2f)
                            )
                        }
                    }
                }

                // Draw ghost drop shadow (shows landing destination)
                if (isRunning && !isGameOver && !isPaused) {
                    var ghostY = pieceY
                    while (canFit(pieceX, ghostY + 1, currentPiece.shape)) {
                        ghostY += 1
                    }
                    if (ghostY > pieceY) {
                        for ((dx, dy) in currentPiece.shape) {
                            val gx = pieceX + dx
                            val gy = ghostY + dy
                            if (gy in 0 until rows && gx in 0 until cols) {
                                drawRoundRect(
                                    color = currentPiece.color.copy(alpha = 0.22f),
                                    topLeft = Offset(gx * cellW + 1.5f, gy * cellH + 1.5f),
                                    size = Size(cellW - 3f, cellH - 3f),
                                    cornerRadius = CornerRadius(3f, 3f)
                                )
                            }
                        }
                    }

                    // Draw active falling piece
                    for ((dx, dy) in currentPiece.shape) {
                        val px = pieceX + dx
                        val py = pieceY + dy
                        if (py in 0 until rows && px in 0 until cols) {
                            drawRoundRect(
                                color = currentPiece.color,
                                topLeft = Offset(px * cellW + 1.2f, py * cellH + 1.2f),
                                size = Size(cellW - 2.4f, cellH - 2.4f),
                                cornerRadius = CornerRadius(3f, 3f)
                            )
                            drawRoundRect(
                                color = Color.White.copy(alpha = 0.45f),
                                topLeft = Offset(px * cellW + 2f, py * cellH + 2f),
                                size = Size((cellW - 4f) * 0.45f, (cellH - 4f) * 0.45f),
                                cornerRadius = CornerRadius(2f, 2f)
                            )
                        }
                    }
                }
            }

            // Start Splash Overlay
            if (!isRunning && !isGameOver) {
                Surface(
                    color = Color(0xFF0F172A).copy(alpha = 0.92f),
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF334155)),
                    modifier = Modifier.padding(24.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(22.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("🧱 Block Line Arcade", fontWeight = FontWeight.Black, color = Color.White, fontSize = 20.sp)
                        Text(
                            "Fill full horizontal lines across the 10 columns to clear them and earn real coins! Game only ends when blocks stack to the very top ceiling.",
                            fontSize = 13.sp,
                            color = Color(0xFF94A3B8),
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )
                        Button(
                            onClick = { resetGame() },
                            colors = ButtonDefaults.buttonColors(containerColor = SaveBlueDark),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth().testTag("start_block_game_btn")
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Play")
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Start Game (Full Screen)", fontWeight = FontWeight.Black, fontSize = 15.sp)
                        }
                    }
                }
            }

            // Game Over Overlay
            if (isGameOver) {
                Surface(
                    color = Color(0xFF0F172A).copy(alpha = 0.94f),
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(2.dp, SpendOrangeDark),
                    modifier = Modifier.padding(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(22.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("💥 Tower Reached Ceiling!", color = SpendOrangeLight, fontWeight = FontWeight.Black, fontSize = 20.sp)
                        Text("No more space left at top row.", color = Color(0xFF94A3B8), fontSize = 13.sp)

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = Color(0xFF1E293B),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Lines: $linesCleared", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
                            }
                            Surface(
                                color = Color(0xFF1E293B),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Score: $score", color = CoinGoldLight, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
                            }
                        }

                        if (earnedCoinsThisGame > 0) {
                            Surface(
                                color = GardenGreenDark.copy(alpha = 0.3f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, GardenGreen),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("🪙 +$earnedCoinsThisGame Coins Awarded!", color = GardenGreenMint, fontWeight = FontWeight.Black, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp))
                            }
                        }

                        Button(
                            onClick = { resetGame() },
                            colors = ButtonDefaults.buttonColors(containerColor = GardenGreen),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth().testTag("replay_block_game_btn")
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = "Replay")
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Play Again 🔄", fontWeight = FontWeight.Black, fontSize = 15.sp)
                        }
                    }
                }
            }
        }

        // Tactile Arcade Bottom Controller D-Pad & Action Buttons
        if (isRunning && !isGameOver) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Move Left
                IconButton(
                    onClick = {
                        if (canFit(pieceX - 1, pieceY, currentPiece.shape)) pieceX -= 1
                    },
                    modifier = Modifier
                        .size(54.dp)
                        .background(Color(0xFF1E293B), CircleShape)
                        .border(1.dp, Color(0xFF475569), CircleShape)
                        .testTag("block_move_left_btn")
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Move Left", tint = Color.White, modifier = Modifier.size(26.dp))
                }

                // Rotate Clockwise
                IconButton(
                    onClick = { rotatePiece() },
                    modifier = Modifier
                        .size(58.dp)
                        .background(SaveBlueDark, CircleShape)
                        .border(1.5.dp, Color(0xFF60A5FA), CircleShape)
                        .testTag("block_rotate_btn")
                ) {
                    Icon(Icons.Default.RotateRight, contentDescription = "Rotate", tint = Color.White, modifier = Modifier.size(30.dp))
                }

                // Move Right
                IconButton(
                    onClick = {
                        if (canFit(pieceX + 1, pieceY, currentPiece.shape)) pieceX += 1
                    },
                    modifier = Modifier
                        .size(54.dp)
                        .background(Color(0xFF1E293B), CircleShape)
                        .border(1.dp, Color(0xFF475569), CircleShape)
                        .testTag("block_move_right_btn")
                ) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "Move Right", tint = Color.White, modifier = Modifier.size(26.dp))
                }

                // Soft Drop
                IconButton(
                    onClick = {
                        if (canFit(pieceX, pieceY + 1, currentPiece.shape)) {
                            pieceY += 1
                        } else {
                            lockPieceAndNext()
                        }
                    },
                    modifier = Modifier
                        .size(54.dp)
                        .background(Color(0xFF0F766E), CircleShape)
                        .border(1.dp, Color(0xFF2DD4BF), CircleShape)
                        .testTag("block_soft_drop_btn")
                ) {
                    Icon(Icons.Default.ArrowDownward, contentDescription = "Soft Drop", tint = Color.White, modifier = Modifier.size(26.dp))
                }

                // Hard Drop
                IconButton(
                    onClick = { hardDrop() },
                    modifier = Modifier
                        .size(54.dp)
                        .background(CoinGoldDark, CircleShape)
                        .border(1.5.dp, Color(0xFFFDE68A), CircleShape)
                        .testTag("block_hard_drop_btn")
                ) {
                    Icon(Icons.Default.KeyboardDoubleArrowDown, contentDescription = "Hard Drop", tint = Color.White, modifier = Modifier.size(28.dp))
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 2. SPEED MATH SPRINT
// -------------------------------------------------------------
@Composable
fun SpeedMathGame(
    childAge: Int,
    onEarnCoins: (Int) -> Unit
) {
    var question by remember { mutableStateOf(generateMathQuestion(childAge)) }
    var score by remember { mutableStateOf(0) }
    var streak by remember { mutableStateOf(0) }
    var feedbackMsg by remember { mutableStateOf<String?>(null) }

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = BoldSurface),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, BoldBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("⚡ Speed Math Sprint", fontWeight = FontWeight.Black, fontSize = 17.sp, color = BoldTextPrimary)
                Text("🔥 Streak: $streak | Score: $score", fontWeight = FontWeight.Black, color = SpendOrangeDark, fontSize = 14.sp)
            }

            Surface(
                shape = RoundedCornerShape(18.dp),
                color = BunnyPurpleLight,
                border = androidx.compose.foundation.BorderStroke(1.dp, BunnyPurple),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier.padding(28.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        question.text,
                        fontWeight = FontWeight.Black,
                        fontSize = 36.sp,
                        color = BunnyPurpleDark
                    )
                }
            }

            // Options
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                question.options.forEach { opt ->
                    Button(
                        onClick = {
                            if (opt == question.answer) {
                                score += 1
                                streak += 1
                                feedbackMsg = "✅ Correct! +1"
                                if (streak % 3 == 0) {
                                    onEarnCoins(3)
                                    feedbackMsg = "🌟 3 Streak! +3 Coins earned!"
                                }
                            } else {
                                streak = 0
                                feedbackMsg = "❌ Oops, answer was ${question.answer}"
                            }
                            question = generateMathQuestion(childAge)
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SaveBlueDark),
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                    ) {
                        Text("$opt", fontSize = 20.sp, fontWeight = FontWeight.Black)
                    }
                }
            }

            feedbackMsg?.let {
                Text(it, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = if (it.startsWith("✅") || it.startsWith("🌟")) GardenGreenDark else SpendOrangeDark)
            }
        }
    }
}

data class MathQuestion(val text: String, val answer: Int, val options: List<Int>)

fun generateMathQuestion(age: Int): MathQuestion {
    val a = when {
        age < 7 -> Random.nextInt(1, 9)
        age < 11 -> Random.nextInt(4, 18)
        else -> Random.nextInt(10, 45)
    }
    val b = when {
        age < 7 -> Random.nextInt(1, 9)
        age < 11 -> Random.nextInt(2, 10)
        else -> Random.nextInt(4, 20)
    }
    val op = if (age < 8) "+" else listOf("+", "-", "*").random()

    val ans = when (op) {
        "+" -> a + b
        "-" -> (a.coerceAtLeast(b)) - (a.coerceAtMost(b))
        else -> (a % 9 + 2) * (b % 9 + 2)
    }
    val text = if (op == "-" && a < b) "$b - $a = ?" else "$a $op $b = ?"

    val fake1 = ans + Random.nextInt(1, 4)
    val fake2 = (ans - Random.nextInt(1, 4)).coerceAtLeast(0)
    val options = listOf(ans, fake1, fake2).distinct().shuffled()
    return MathQuestion(text, ans, options)
}

// -------------------------------------------------------------
// 3. LOGIC PATTERN GAME
// -------------------------------------------------------------
@Composable
fun PatternBrainGame(
    onEarnCoins: (Int) -> Unit
) {
    val sequence = remember { listOf("🍎", "🍌", "🍎", "🍌", "🍎", "❓") }
    var solved by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = BoldSurface),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, BoldBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("🧠 Logic Pattern Completer", fontWeight = FontWeight.Black, fontSize = 17.sp, color = BoldTextPrimary)
            Text("Find the missing symbol in the sequence:", fontSize = 13.sp, color = BoldTextSecondary)

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                sequence.forEach { sym ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (sym == "❓") CoinGoldBg else BoldSurfaceVariant,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (sym == "❓") CoinGoldLight else BoldBorder),
                        modifier = Modifier.size(46.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(if (solved && sym == "❓") "🍌" else sym, fontSize = 22.sp)
                        }
                    }
                }
            }

            if (!solved) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    listOf("🍇", "🍌", "🍓", "🍎").forEach { choice ->
                        Button(
                            onClick = {
                                if (choice == "🍌") {
                                    solved = true
                                    onEarnCoins(3)
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BoldSurfaceVariant)
                        ) {
                            Text(choice, fontSize = 22.sp)
                        }
                    }
                }
            } else {
                Surface(
                    color = GardenGreenMint,
                    border = androidx.compose.foundation.BorderStroke(1.dp, GardenGreenLight),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "🎉 Brilliant! Pattern completed! +3 Coins awarded!",
                        color = GardenGreenDark,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(14.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
