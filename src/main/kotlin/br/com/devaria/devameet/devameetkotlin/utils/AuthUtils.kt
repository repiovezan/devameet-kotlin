package br.com.devaria.devameet.devameetkotlin.utils

val EMAIL_REGEX = "^[\\w+.]+@\\w+\\.\\w{2,}(?:\\.\\w{2})?\$";
var PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";

fun isEmailValid(email: String) : Boolean{
    return EMAIL_REGEX.toRegex().matches(email)
}

fun isPasswordValid(email: String) : Boolean{
    return PASSWORD_REGEX.toRegex().matches(email)
}