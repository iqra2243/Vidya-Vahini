package com.example.vidyavahini

data class RouteInfo(
    val name: String,
    val stops: List<String>
)

object RouteData {
    val routes = listOf(
        RouteInfo("College Route A", listOf("Village", "Bridge", "Temple", "Market", "College")),
        RouteInfo("School Route B", listOf("Town", "Bus Stand", "Circle", "School")),
        RouteInfo("College Route C", listOf("Highway", "Main Stop", "Bridge", "College"))
    )

    fun getRoute(index: Int): RouteInfo {
        return routes.getOrElse(index) { routes[0] }
    }

    fun etaForStop(routeIndex: Int, stop: String): String {
        val route = getRoute(routeIndex)
        val index = route.stops.indexOfFirst { it.equals(stop, ignoreCase = true) }

        if (index == -1) return "10 mins"
        if (index == route.stops.lastIndex) return "Arrived"

        val remainingStops = route.stops.lastIndex - index
        return "${remainingStops * 4} mins"
    }

    fun routeLine(routeIndex: Int, currentStop: String): String {
        val route = getRoute(routeIndex)

        return route.stops.joinToString("  ━━━  ") { stop ->
            if (stop.equals(currentStop, ignoreCase = true)) {
                "📍 ${stop.uppercase()}"
            } else {
                stop
            }
        }
    }
}