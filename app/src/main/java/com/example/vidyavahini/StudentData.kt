package com.example.vidyavahini

data class Student(
    val name: String,
    val contact: String
)

object StudentData {

    val students = listOf(
        Student("Aisha", "9876543210"),
        Student("Rahul", "9123456780"),
        Student("Priya", "9988776655"),
        Student("Arjun", "9090909090"),
        Student("Sneha", "8888888888")
    )
}