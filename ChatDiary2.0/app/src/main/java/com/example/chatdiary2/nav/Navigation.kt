package com.example.chatdiary2.nav

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.example.chatdiary2.nav.Destination.Diary
import com.example.chatdiary2.nav.Destination.Login
import com.example.chatdiary2.nav.Destination.Register


object Destination {
    const val About = "about"
    const val Articles = "articles"
    const val Login = "login"
    const val Register = "register"
    const val Diary = "diary"
}

class Action(val navController: NavController) {
    val clearStack = {
        navController.popBackStack(navController.graph.startDestinationId, true)

    }
    val toLogin: (builder: NavOptionsBuilder.() -> Unit) -> Unit =
        { navController.navigate(Login, it) }
    val toRegister: (builder: NavOptionsBuilder.() -> Unit) -> Unit =
        { navController.navigate(Register, it) }
    val toDiary: (builder: NavOptionsBuilder.() -> Unit) -> Unit =
        { navController.navigate(Diary, it) }
}