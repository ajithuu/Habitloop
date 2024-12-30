package uk.ac.tees.mad.habitloop.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.google.gson.Gson
import uk.ac.tees.mad.habitloop.authentication.viewmodel.AuthViewmodel
import uk.ac.tees.mad.habitloop.mainapp.model.HabitInfo
import uk.ac.tees.mad.habitloop.mainapp.viewmodel.HabitViewmodel
import uk.ac.tees.mad.habitloop.ui.mainapp.AddNewHabitScreen
import uk.ac.tees.mad.habitloop.ui.mainapp.EditProfileScreen
import uk.ac.tees.mad.habitloop.ui.mainapp.HabitDetailsScreen
import uk.ac.tees.mad.habitloop.ui.mainapp.HomeScreen
import uk.ac.tees.mad.habitloop.ui.mainapp.ProfileScreen
import uk.ac.tees.mad.rd.ui.authentication.LogInScreen
import uk.ac.tees.mad.rd.ui.authentication.SignUpScreen
import uk.ac.tees.mad.rd.ui.authentication.SplashScreen


@Composable
fun CentralNavigation(
    navController: NavHostController,
    authViewmodel: AuthViewmodel,
    habitViewmodel: HabitViewmodel
){
    NavHost(
        navController = navController,
        startDestination = "splash_graph"
    ){

        navigation(
            startDestination = "splash_screen",
            route = "splash_graph"
        ){
            composable("splash_screen") {
                SplashScreen(
                    authViewmodel,
                    navController
                )
            }
        }

        navigation(
            startDestination = "login_screen",
            route = "auth_graph"
        ){
            composable("login_screen") {
                LogInScreen(
                    authViewmodel,
                    navController
                )
            }

            composable("signup_screen") {
                SignUpScreen(
                    authViewmodel,
                    navController
                )
            }
        }

        navigation(
            startDestination = "home_screen",
            route = "home_graph"
        ){
            composable("home_screen") {
                HomeScreen(
                    navController,
                    authViewmodel,
                    habitViewmodel
                )
            }

            composable("profile_screen"){
                ProfileScreen(authViewmodel, navController)
            }

            composable("edit_profile_screen"){
                EditProfileScreen(authViewmodel, navController)
            }

            composable("add_new_habit") {
                AddNewHabitScreen(habitViewmodel, navController)
            }

            composable(
                "habit_details_screen/{habits}",
                arguments = listOf(navArgument("habits"){type = NavType.StringType})
            ) {backStack->
                val habitJson = backStack.arguments?.getString("habits")
                val habitInfo = Gson().fromJson(habitJson, HabitInfo::class.java)
                HabitDetailsScreen(habit = habitInfo)
            }

        }

    }

}