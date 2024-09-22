package xyz.katiedotson.deweydecimal

sealed class NavGraphItem(val route: String) {
    data object Search : NavGraphItem(route = "search")
    data object Add : NavGraphItem(route = "add")
}
