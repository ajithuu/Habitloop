package uk.ac.tees.mad.habitloop.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import uk.ac.tees.mad.habitloop.authentication.viewmodel.AuthViewmodel
import uk.ac.tees.mad.habitloop.ui.mainapp.AddNewHabitScreen
import uk.ac.tees.mad.habitloop.ui.mainapp.EditProfileScreen
import uk.ac.tees.mad.habitloop.ui.mainapp.HomeScreen
import uk.ac.tees.mad.habitloop.ui.mainapp.ProfileScreen
import uk.ac.tees.mad.rd.ui.authentication.LogInScreen
import uk.ac.tees.mad.rd.ui.authentication.SignUpScreen
import uk.ac.tees.mad.rd.ui.authentication.SplashScreen


@Composable
fun CentralNavigation(
    navController: NavHostController,
    authViewmodel: AuthViewmodel
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
                    authViewmodel
                )
            }

            composable("profile_screen"){
                ProfileScreen(authViewmodel, navController)
            }

            composable("edit_profile_screen"){
                EditProfileScreen(authViewmodel, navController)
            }

            composable("add_new_habit") {
                AddNewHabitScreen()
            }

        }

    }

}