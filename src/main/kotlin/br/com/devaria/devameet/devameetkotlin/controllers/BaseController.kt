package br.com.devaria.devameet.devameetkotlin.controllers

import br.com.devaria.devameet.devameetkotlin.dtos.DefaultResponseMsgDto
import br.com.devaria.devameet.devameetkotlin.entities.User
import br.com.devaria.devameet.devameetkotlin.services.UserService
import br.com.devaria.devameet.devameetkotlin.utils.JWTUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.lang.IllegalArgumentException

open class BaseController(c:String) {

    protected val log = LoggerFactory.getLogger(c)

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var jwtUtils: JWTUtils

    fun formatErrorResponse(statusCode: HttpStatus, message: Array<String>): ResponseEntity<Any>
        = ResponseEntity(DefaultResponseMsgDto(statusCode.value(), message, statusCode.toString()), statusCode)

    fun readToken(authorization: String): User {
        val token = authorization.substring(7)
        val userIdString = jwtUtils.getUserId(token)

        if (userIdString == null || userIdString.isNullOrEmpty()) {
            throw IllegalArgumentException("Você não tem acesso a este recurso")
        }

        return userService.getUser(userIdString.toLong())
            ?: throw IllegalArgumentException("Você não tem acesso a este recurso")
    }
}