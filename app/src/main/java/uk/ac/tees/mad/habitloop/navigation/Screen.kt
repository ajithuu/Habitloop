package uk.ac.tees.mad.habitloop.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Home : Screen("home")
    object HabitDetail : Screen("habit/{habitId}") {
        fun createRoute(habitId: String) = "habit/$habitId"
    }
    object Profile : Screen("profile")
}
