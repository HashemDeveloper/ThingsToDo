package com.example.thingstodo.ui.splash

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thingstodo.R
import com.example.thingstodo.ui.theme.LOGO_HEIGHT
import com.example.thingstodo.ui.theme.ThingsToDoTheme
import com.example.thingstodo.ui.theme.splashScreenBackground
import com.example.thingstodo.util.Constants
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navigateToTaskScreen: () -> Unit) {
    var startAnimation by remember {
        mutableStateOf(false)
    }
    val offSetState by animateDpAsState(
        targetValue = if (startAnimation) 0.dp else 100.dp,
        animationSpec = tween(
            durationMillis = 1000
        )
    )
    val alphaState by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000
        )
    )
    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(Constants.SPLASH_SCREEN_DELAY)
        navigateToTaskScreen()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.splashScreenBackground)
            .offset(y = offSetState)
            .alpha(alpha = alphaState),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.size(LOGO_HEIGHT),
            painter = painterResource(getLogo()),
            contentDescription = stringResource(id = R.string.logo_icon)
        )
    }
}
@Composable
fun getLogo(): Int {
    return if (isSystemInDarkTheme()) {
        R.drawable.ic_logo_dark
    } else {
        R.drawable.ic_logo_light
    }
}
@Composable
@Preview
private fun PreviewSplashScreen_LightTheme() {
    SplashScreen({})
}
@Composable
@Preview
private fun PreviewSplashScreen_DarkTheme() {
    ThingsToDoTheme(darkTheme = true) {
        SplashScreen({})
    }
}