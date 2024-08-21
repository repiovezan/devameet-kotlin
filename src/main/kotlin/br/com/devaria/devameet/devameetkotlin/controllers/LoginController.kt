package br.com.devaria.devameet.devameetkotlin.controllers

import br.com.devaria.devameet.devameetkotlin.dtos.LoginRequestDto
import br.com.devaria.devameet.devameetkotlin.exceptions.BadRequestException
import br.com.devaria.devameet.devameetkotlin.services.LoginService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth/login")
class LoginController(private val loginService: LoginService) : BaseController(LoginController::class.java.toString()) {

    @PostMapping
    fun doLogin(@RequestBody dto: LoginRequestDto) : ResponseEntity<Any> {
        try{
            val result = loginService.login(dto)
            return ResponseEntity(result, HttpStatus.OK)
        }
        catch (bre: BadRequestException){
            return formatErrorResponse(bre.status, bre.messages.toTypedArray())
        }
        catch (e: Exception){
            return formatErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, mutableListOf("Ocorreu erro ao efetuaro login").toTypedArray())
        }
    }
}