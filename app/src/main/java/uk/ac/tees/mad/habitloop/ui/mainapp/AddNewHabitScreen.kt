package uk.ac.tees.mad.habitloop.ui.mainapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.ac.tees.mad.habitloop.ui.theme.metamorphousFamily
import uk.ac.tees.mad.habitloop.ui.theme.poppinsFamily


@Composable
fun AddNewHabitScreen(
    modifier: Modifier = Modifier
) {

    var name by remember { mutableStateOf("") }
    var goal by remember { mutableStateOf("") }
    var schedule by remember { mutableStateOf("") }

    Column (
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
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

        Spacer(modifier.weight(10f))
    }
}