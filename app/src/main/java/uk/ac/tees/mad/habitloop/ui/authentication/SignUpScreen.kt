package uk.ac.tees.mad.rd.ui.authentication

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import uk.ac.tees.mad.habitloop.R
import uk.ac.tees.mad.habitloop.authentication.model.AuthResponse
import uk.ac.tees.mad.habitloop.authentication.model.UserInfo
import uk.ac.tees.mad.habitloop.authentication.viewmodel.AuthViewmodel
import uk.ac.tees.mad.habitloop.ui.theme.metamorphousFamily
import uk.ac.tees.mad.habitloop.ui.theme.poppinsFamily


@Composable
fun SignUpScreen(
    authViewmodel: AuthViewmodel,
    navController: NavHostController
){

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var passwordVisi by remember { mutableStateOf(false) }
    val authState by authViewmodel.authState.collectAsState()

    when(authState){
        is AuthResponse.Success->{
            navController.navigate("home_graph"){
                popUpTo(navController.graph.startDestinationId){
                    inclusive = true
                }
                launchSingleTop = true
            }
        }

        is AuthResponse.Failure -> {
            Toast.makeText(LocalContext.current, "Can't Register: ${(authState as AuthResponse.Failure).message}", Toast.LENGTH_SHORT).show()
            password = ""
            passwordVisi = false
        }
        AuthResponse.Idle -> {

        }
        AuthResponse.Loading -> {

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
        Spacer(modifier = Modifier.weight(3f))

        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "Habit Loop",
            fontSize = 25.sp,
            fontFamily = metamorphousFamily,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Welcome! Create new account!",
            fontFamily = poppinsFamily,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.weight(3f))

        Text(
            modifier = Modifier
                .fillMaxWidth(0.8f),
            text = "Email",
            fontFamily = poppinsFamily,
            fontSize = 16.sp
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(0.85f),
            value = email,
            onValueChange = {
                email = it
            },
            shape = RoundedCornerShape(15.dp),
            placeholder = {
                Text(
                    text = "Enter the email",
                    fontFamily = poppinsFamily,
                    fontSize = 16.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "User email"
                )
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            modifier = Modifier
                .fillMaxWidth(0.8f),
            text = "Name",
            fontFamily = poppinsFamily,
            fontSize = 16.sp
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(0.85f),
            value = name,
            onValueChange = {
                name = it
            },
            shape = RoundedCornerShape(15.dp),
            placeholder = {
                Text(
                    text = "Enter the name",
                    fontFamily = poppinsFamily,
                    fontSize = 16.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "User name"
                )
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            modifier = Modifier
                .fillMaxWidth(0.8f),
            text = "Password",
            fontFamily = poppinsFamily,
            fontSize = 16.sp
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(0.85f),
            value = password,
            onValueChange = {
                password = it
            },
            shape = RoundedCornerShape(15.dp),
            placeholder = {
                Text(
                    text = "Enter the password",
                    fontFamily = poppinsFamily,
                    fontSize = 16.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Key,
                    contentDescription = "User Password"
                )
            },
            trailingIcon = {
                val showPassword = if (passwordVisi){
                    Icons.Default.Visibility
                }else{
                    Icons.Default.VisibilityOff
                }

                IconButton(
                    onClick = {
                        passwordVisi = !passwordVisi
                    }
                ) {
                    Icon(
                        imageVector = showPassword,
                        contentDescription = "Password Visibility"
                    )
                }
            },
            visualTransformation = if (passwordVisi) VisualTransformation.None else PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier
                .fillMaxWidth(0.8f),
            border = BorderStroke(1.dp, Color.Black),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Black
            ),
            onClick = {
                if (email.isNotEmpty() && name.isNotEmpty() && password.isNotEmpty()){
                    val newUser = UserInfo(
                        name = name,
                        email = email,
                        profilePictureUrl = ""
                    )
                    authViewmodel.RegisterUser(newUser, password)
                }
            },
            shape = RoundedCornerShape(15.dp)
        ) {
            Text(
                text = "Create User!!",
                fontSize = 17.sp,
                fontFamily = poppinsFamily
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier
                .fillMaxWidth(0.8f),
            border = BorderStroke(1.dp, Color.Black),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Black
            ),
            onClick = {
                navController.navigate("login_screen")
            },
            shape = RoundedCornerShape(15.dp)
        ) {
            Text(
                text = "Already have an account?",
                fontSize = 17.sp,
                fontFamily = poppinsFamily
            )
        }

        Spacer(modifier = Modifier.weight(8f))
    }
}