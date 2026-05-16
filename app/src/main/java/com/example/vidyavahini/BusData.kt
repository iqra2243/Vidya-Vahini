package com.example.vidyavahini

data class BusInfo(
    val busNumber: String,
    val driverName: String,
    val driverPhone: String
)

object BusData {

    val buses = listOf(
        BusInfo("KA-05-1234", "Ramesh", "9876543210"),
        BusInfo("KA-05-5678", "Suresh", "9123456780"),
        BusInfo("KA-05-9012", "Mahesh", "9988776655")

    )
}