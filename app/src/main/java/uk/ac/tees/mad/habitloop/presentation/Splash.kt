package uk.ac.tees.mad.habitloop.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.delay
import uk.ac.tees.mad.habitloop.R

@Composable
fun Splash(onNavigateToLogin: () -> Unit) {
    LaunchedEffect(true) {
        delay(2500)
        onNavigateToLogin()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(painter = painterResource(id = R.drawable.hl), contentDescription = "habitloop")
    }
}