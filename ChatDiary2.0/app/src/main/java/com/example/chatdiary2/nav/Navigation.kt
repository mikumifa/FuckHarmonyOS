package com.example.chatdiary2.nav

import androidx.navigation.NavController
import com.example.chatdiary2.nav.Destination.Diary
import com.example.chatdiary2.nav.Destination.Login
import com.example.chatdiary2.nav.Destination.Register


object Destination {
    const val Login = "login"
    const val Register = "register"
    const val Diary = "diary"
}

class Action(navController: NavController) {


    val toLogin: () -> Unit = { navController.navigate(Login) }
    val toRegister: () -> Unit = { navController.navigate(Register) }
    val toDiary: () -> Unit = { navController.navigate(Diary) }
}