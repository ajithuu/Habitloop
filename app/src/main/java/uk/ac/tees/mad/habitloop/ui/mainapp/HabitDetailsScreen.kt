package uk.ac.tees.mad.habitloop.ui.mainapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.ac.tees.mad.habitloop.mainapp.model.HabitInfo
import uk.ac.tees.mad.habitloop.ui.theme.poppinsFamily

@Composable
fun HabitDetailsScreen(
    habit: HabitInfo,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    0.2f to Color(0xFFF7A6D0),
                    0.4f to Color(0xFF94bbe9),
                    1.0f to Color(0xFFeeaeca)
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp),
            border = BorderStroke(1.dp, Color.Black),
            onClick = {

            }
        ) {
            Text(
                modifier = modifier.fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                text = habit.name,
                fontSize = 18.sp,
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Bold
            )

            Text(
                modifier = modifier.fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                text = "Goal: ${habit.goal}",
                fontSize = 18.sp,
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Bold
            )

            Text(
                modifier = modifier.fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                text = "Schedule: ${habit.schedule}",
                fontSize = 18.sp,
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Bold
            )
        }

        Button(
            modifier = modifier
                .fillMaxWidth(0.8f),
            border = BorderStroke(1.dp, Color.Black),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF9EB1F4),
                contentColor = Color.Black
            ),
            onClick = {

            }
        ) {
            Text(
                text = "Set Reminder!!",
                fontSize = 18.sp,
                fontFamily = poppinsFamily
            )
        }

        Button(
            modifier = modifier
                .fillMaxWidth(0.8f),
            border = BorderStroke(1.dp, Color.Black),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF9EB1F4),
                contentColor = Color.Black
            ),
            onClick = {

            }
        ) {
            Text(
                text = "Delete Habit!!",
                fontSize = 18.sp,
                fontFamily = poppinsFamily
            )
        }
    }
}