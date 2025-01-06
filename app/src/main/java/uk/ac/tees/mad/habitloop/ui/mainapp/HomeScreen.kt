package uk.ac.tees.mad.habitloop.ui.mainapp

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.gson.Gson
import uk.ac.tees.mad.habitloop.authentication.viewmodel.AuthViewmodel
import uk.ac.tees.mad.habitloop.mainapp.model.FirebaseResponse
import uk.ac.tees.mad.habitloop.mainapp.model.HabitInfo
import uk.ac.tees.mad.habitloop.mainapp.viewmodel.HabitViewmodel
import uk.ac.tees.mad.habitloop.ui.theme.metamorphousFamily
import uk.ac.tees.mad.habitloop.ui.theme.poppinsFamily


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    authViewmodel: AuthViewmodel,
    habitViewmodel: HabitViewmodel,
    modifier: Modifier = Modifier,
) {
    val state = habitViewmodel.authState
    val context = LocalContext.current
    val allHabits by habitViewmodel.allHabits.collectAsState()
    LaunchedEffect(state) {
        if (state.value is FirebaseResponse.Success) {
//            navController.popBackStack()
        } else if (state.value is FirebaseResponse.Failure) {
            val message = (state.value as FirebaseResponse.Failure).message
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
    LaunchedEffect(Unit) {
        habitViewmodel.getAllHabits()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Habit Loop",
                        fontSize = 25.sp,
                        fontFamily = metamorphousFamily
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            authViewmodel.fetchCurrentUser()
                            navController.navigate("profile_screen")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = "Profile Screen"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF9EB1F4)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = Color(0xFF94bbe9),
                shape = CircleShape,
                onClick = {
                    navController.navigate("add_new_habit")
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add new Habit"
                )
            }
        }
    ) { innerpadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerpadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            LazyColumn {
                Log.i("Home Screen", "The list $allHabits")
                items(allHabits) { habit ->
                    if (habit != null) {
                        HabitTile(habit, navController, habitViewmodel)
                    }
                }
            }
        }
    }
}


@Composable
fun HabitTile(
    habit: HabitInfo,
    navController: NavHostController,
    habitViewmodel: HabitViewmodel,
    modifier: Modifier = Modifier,
) {

    var done by remember { mutableStateOf(habit.completed) }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        onClick = {
            val habitJson = Uri.encode(Gson().toJson(habit))
            navController.navigate("habit_details_screen/$habitJson")
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Checkbox(
                checked = done,
                onCheckedChange = {
                    done = !done
                    habitViewmodel.toggleHabitCompletion(
                        habit = habit
                    )
                }
            )
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 5.dp),
                text = habit.name,
                fontSize = 18.sp,
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Bold
            )
        }
    }
}