package br.com.devaria.devameet.devameetkotlin.exceptions

import org.springframework.http.HttpStatus

class BadRequestException(val messages: MutableList<String>) : Exception(messages[0]){
    val status = HttpStatus.BAD_REQUEST
}