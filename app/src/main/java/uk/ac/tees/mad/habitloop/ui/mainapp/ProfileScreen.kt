package uk.ac.tees.mad.habitloop.ui.mainapp


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import uk.ac.tees.mad.habitloop.R
import uk.ac.tees.mad.habitloop.authentication.viewmodel.AuthViewmodel
import uk.ac.tees.mad.habitloop.ui.theme.poppinsFamily


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileScreen(
    authViewmodel: AuthViewmodel,
    navController: NavHostController
){
    val currentUser by authViewmodel.currentUser.collectAsState()

    LaunchedEffect(currentUser){
        authViewmodel.fetchCurrentUser()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    0.2f to Color(0xFFF7A6D0),
                    0.4f to Color(0xFF94bbe9),
                    1.0f to Color(0xFFeeaeca)
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        Spacer(modifier = Modifier.weight(4F))
        GlideImage(
            modifier = Modifier
                .height(300.dp)
                .aspectRatio(1f, matchHeightConstraintsFirst = true)
                .border(
                    1.dp,
                    Color.DarkGray,
                    shape = CircleShape
                )
                .padding(1.dp)
                .clip(CircleShape),
            model = currentUser?.profilePictureUrl,
            contentDescription = "",
            failure = placeholder(painter = painterResource(R.drawable.avatar))
        )

        Spacer(modifier = Modifier.weight(1F))

        Button(
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.DarkGray,
                containerColor = Color.Transparent
            ),
            border = BorderStroke(1.dp, Color.Black),
            onClick = {
                navController.navigate("edit_profile_screen")
            }
        ) {
            Text(
                text = "Edit Profile",
                fontFamily = poppinsFamily,
                fontSize = 15.sp
            )
        }

        Spacer(modifier = Modifier.weight(1F))

        Text(
            text = "Account Details",
            fontFamily = poppinsFamily,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            modifier = Modifier.fillMaxWidth(0.85f),
            text = "Name",
            fontFamily = poppinsFamily,
            fontSize = 18.sp
        )
        Card(
            modifier = Modifier.fillMaxWidth(0.88f),
            border = BorderStroke(1.dp, Color.Black),
            elevation = CardDefaults.elevatedCardElevation(10.dp)
        ){
            currentUser?.name?.let {name->
                Text(
                    modifier = Modifier
                        .padding(10.dp),
                    text = name,
                    fontFamily = poppinsFamily,
                    fontSize = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1F))

        Text(
            modifier = Modifier.fillMaxWidth(0.85f),
            text = "Email",
            fontFamily = poppinsFamily,
            fontSize = 18.sp
        )
        Card(
            modifier = Modifier.fillMaxWidth(0.88f),
            border = BorderStroke(1.dp, Color.Black),
            elevation = CardDefaults.elevatedCardElevation(10.dp)
        ) {
            currentUser?.email?.let { email ->
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = email,
                    fontFamily = poppinsFamily,
                    fontSize = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1F))

        Button(
            modifier = Modifier
                .fillMaxWidth(0.8f),
            elevation = ButtonDefaults.elevatedButtonElevation(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFC890E0),
                contentColor = Color.DarkGray
            ),
            onClick = {
                authViewmodel.logout()
                navController.navigate("auth_graph"){
                    popUpTo(navController.graph.startDestinationId){
                        inclusive=true
                    }
                }
            }
        ) {
            Text(
                text = "Logout",
                fontSize = 16.sp,
                fontFamily = poppinsFamily
            )
        }

        Spacer(modifier = Modifier.weight(10f))
    }
}