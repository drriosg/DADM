package com.unal.edu.companieslist

data class Company(
    val id: Int,
    val name: String,
    val uri: String,
    val phone: Int,
    val email: String,
    val prodAndServ: String,
    val classification: String,
)
