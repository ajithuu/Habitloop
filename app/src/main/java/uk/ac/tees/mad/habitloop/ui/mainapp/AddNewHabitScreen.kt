package uk.ac.tees.mad.habitloop.ui.mainapp

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import uk.ac.tees.mad.habitloop.mainapp.model.FirebaseResponse
import uk.ac.tees.mad.habitloop.mainapp.model.HabitInfo
import uk.ac.tees.mad.habitloop.mainapp.viewmodel.HabitViewmodel
import uk.ac.tees.mad.habitloop.ui.theme.metamorphousFamily
import uk.ac.tees.mad.habitloop.ui.theme.poppinsFamily


@Composable
fun AddNewHabitScreen(
    habitViewmodel: HabitViewmodel,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val state = habitViewmodel.authState
    var name by remember { mutableStateOf("") }
    var goal by remember { mutableStateOf("") }
    var schedule by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(state) {
        if (state.value is FirebaseResponse.Success) {
//            navController.popBackStack()
        } else if (state.value is FirebaseResponse.Failure) {
            val message = (state.value as FirebaseResponse.Failure).message
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier.weight(2f))
        Text(
            modifier = modifier.fillMaxWidth(),
            text = "Add New Habit!!",
            fontSize = 25.sp,
            fontFamily = metamorphousFamily,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier.weight(1f))

        Text(
            modifier = modifier
                .fillMaxWidth(0.8f),
            text = "Name:",
            fontSize = 18.sp,
            fontFamily = poppinsFamily
        )
        OutlinedTextField(
            modifier = modifier.fillMaxWidth(0.85f),
            value = name,
            onValueChange = {
                name = it
            },
            shape = RoundedCornerShape(15.dp)
        )

        Spacer(modifier.weight(1f))

        Text(
            modifier = modifier
                .fillMaxWidth(0.8f),
            text = "Goal:",
            fontSize = 18.sp,
            fontFamily = poppinsFamily
        )
        OutlinedTextField(
            modifier = modifier.fillMaxWidth(0.85f),
            value = goal,
            onValueChange = {
                goal = it
            },
            shape = RoundedCornerShape(15.dp)
        )

        Spacer(modifier.weight(1f))

        Text(
            modifier = modifier
                .fillMaxWidth(0.8f),
            text = "Schedule:",
            fontSize = 18.sp,
            fontFamily = poppinsFamily
        )
        OutlinedTextField(
            modifier = modifier.fillMaxWidth(0.85f),
            value = schedule,
            onValueChange = {
                schedule = it
            },
            shape = RoundedCornerShape(15.dp)
        )

        Spacer(modifier.weight(1f))

        Button(
            modifier = modifier
                .fillMaxWidth(0.8f),
            border = BorderStroke(width = 1.dp, color = Color.Black),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF9EB1F4),
                contentColor = Color.Black
            ),
            elevation = ButtonDefaults.elevatedButtonElevation(10.dp),
            onClick = {
                val habit = HabitInfo(
                    name = name,
                    goal = goal,
                    schedule = schedule
                )
                habitViewmodel.addNewHabit(habit = habit)
            }
        ) {
            Text(
                text = "Create Habit!!",
                fontSize = 18.sp,
                fontFamily = poppinsFamily
            )
        }

        Spacer(modifier.weight(10f))
    }
}