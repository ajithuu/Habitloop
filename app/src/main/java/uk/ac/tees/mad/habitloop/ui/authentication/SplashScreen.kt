package uk.ac.tees.mad.rd.ui.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import uk.ac.tees.mad.habitloop.R
import uk.ac.tees.mad.habitloop.authentication.viewmodel.AuthViewmodel
import uk.ac.tees.mad.habitloop.ui.theme.iconFamily


@Composable
fun SplashScreen(
    authViewmodel: AuthViewmodel,
    navController: NavHostController
){

    val isLoggedIn by authViewmodel.isLoggedIn.collectAsState()

    LaunchedEffect(Unit) {
        delay(2000L)

        if(isLoggedIn){
            navController.navigate("home_graph")
        }else{
            navController.navigate("auth_graph")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xFF9EB1F4)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Spacer(modifier = Modifier.weight(1f))
        Image(
            modifier = Modifier
                .size(200.dp),
            painter = painterResource(R.drawable.habitloop),
            contentDescription = "Main App Icon"
        )
        Text(
            text = "Habit Loop",
            fontSize = 28.sp,
            fontFamily = iconFamily,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}