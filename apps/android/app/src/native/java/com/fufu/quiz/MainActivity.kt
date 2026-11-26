package com.fufu.quiz

import android.os.Bundle
import android.content.ComponentCallbacks2
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.fufu.quiz.document.DocumentRecognitionManager
import com.fufu.quiz.state.QuizRepository
import com.fufu.quiz.ui.app.FufuAppShell
import com.fufu.quiz.ui.components.clearMathRenderCaches
import com.fufu.quiz.ui.theme.FufuColors
import com.fufu.quiz.ui.theme.FufuMotion
import com.fufu.quiz.ui.theme.FUFUQuizTheme
import com.fufu.quiz.util.LauncherIconSwitcher
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        QuizRepository.init(applicationContext)
        DocumentRecognitionManager.init(applicationContext)
        com.fufu.quiz.sync.webdav.WebDavAutoBackup.schedule(applicationContext)
        LauncherIconSwitcher.applyFufuMode(applicationContext, QuizRepository.fufuModeEnabled)
        applyStatusBarTheme(QuizRepository.darkThemeEnabled)
        setContent {
            val darkTheme = QuizRepository.darkThemeEnabled
            SideEffect {
                applyStatusBarTheme(darkTheme)
            }
            FUFUQuizTheme(darkTheme = darkTheme) {
                FufuStartupGate {
                    FufuAppShell()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        clearMathRenderCaches()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level >= ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW) clearMathRenderCaches()
    }

    private fun applyStatusBarTheme(darkTheme: Boolean) {
        window.statusBarColor = if (darkTheme) {
            Color(0xFF0E1627).toArgb()
        } else {
            Color(0xFFF7F9FF).toArgb()
        }
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = !darkTheme
    }
}

@Composable
private fun FufuStartupGate(
    content: @Composable () -> Unit
) {
    val shouldShowStartupSplash = remember { QuizRepository.fufuModeEnabled && QuizRepository.startupSplashEnabled }
    var showSplash by remember { mutableStateOf(shouldShowStartupSplash) }
    var splashProgress by remember { mutableFloatStateOf(if (shouldShowStartupSplash) 0f else 1f) }

    LaunchedEffect(shouldShowStartupSplash) {
        if (shouldShowStartupSplash) {
            splashProgress = 1f
            delay(FufuMotion.SplashHoldMillis.toLong())
            showSplash = false
        } else {
            splashProgress = 1f
            showSplash = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        content()
        AnimatedVisibility(
            visible = showSplash,
            exit = fadeOut(animationSpec = tween(durationMillis = FufuMotion.SplashFadeMillis))
        ) {
            FufuSplashArtwork(progress = splashProgress)
        }
    }
}

@Composable
private fun FufuSplashArtwork(progress: Float) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FufuColors.BgApp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_screen_study),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillWidth,
            alignment = Alignment.TopCenter
        )
        FufuSplashNativeOverlay(progress = progress)
    }
}

@Composable
private fun FufuSplashNativeOverlay(progress: Float) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val progressBarWidth = maxWidth * 0.54f
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(
                    top = maxHeight * 0.712f,
                    start = 34.dp,
                    end = 34.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Fufu",
                    color = Color(0xFF1F5EA8),
                    fontSize = 41.sp,
                    lineHeight = 47.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Quiz",
                    color = Color(0xFFFF78AD),
                    fontSize = 41.sp,
                    lineHeight = 47.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text = "让自律成为习惯，让成长看得见",
                color = Color(0xFF7C9ED6),
                fontSize = 17.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
            Spacer(Modifier.height(30.dp))
            FufuSplashProgressBar(
                progress = progress,
                modifier = Modifier
                    .width(progressBarWidth)
                    .height(18.dp)
            )
            Spacer(Modifier.height(24.dp))
            Text(
                text = "正在加载中…",
                color = Color(0xFF7C9ED6),
                fontSize = 17.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun FufuSplashProgressBar(
    progress: Float,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(999.dp)
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = FufuMotion.SplashHoldMillis),
        label = "splashProgress"
    )

    Box(
        modifier = modifier
            .clip(shape)
            .background(Color.White.copy(alpha = 0.46f))
            .border(1.dp, Color(0xFFC8D8F4).copy(alpha = 0.62f), shape)
    ) {
        if (animatedProgress > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animatedProgress)
                    .clip(shape)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFFFF6EA7),
                                Color(0xFFFFA9CF)
                            )
                        )
                    )
            ) {
                Text(
                    text = "✦",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 10.dp)
                )
            }
        }
    }
}
