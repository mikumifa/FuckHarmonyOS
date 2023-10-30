package com.example.chatdiary2.nav

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.example.chatdiary2.nav.Destination.About
import com.example.chatdiary2.nav.Destination.Articles
import com.example.chatdiary2.nav.Destination.Diary
import com.example.chatdiary2.nav.Destination.Login
import com.example.chatdiary2.nav.Destination.Main
import com.example.chatdiary2.nav.Destination.Register


object Destination {
    const val Main = "Main"
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
    val toDiaryWithDate: (builder: NavOptionsBuilder.() -> Unit) -> Unit =
        { navController.navigate(Diary, it) }
    val toMain: (builder: NavOptionsBuilder.() -> Unit) -> Unit =
        { navController.navigate(Main, it) }
    val toArticles: (builder: NavOptionsBuilder.() -> Unit) -> Unit =
        { navController.navigate(Articles, it) }
    val toAbout: (builder: NavOptionsBuilder.() -> Unit) -> Unit =
        { navController.navigate(About, it) }
}