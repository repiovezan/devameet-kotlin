package br.com.devaria.devameet.devameetkotlin.controllers

import br.com.devaria.devameet.devameetkotlin.dtos.RegisterRequestDto
import br.com.devaria.devameet.devameetkotlin.exceptions.BadRequestException
import br.com.devaria.devameet.devameetkotlin.services.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth/register")
class RegisterController(val userService: UserService) : BaseController(RegisterController::class.java.toString()) {

    @PostMapping
    fun doCreate(@RequestBody dto: RegisterRequestDto) : ResponseEntity<Any> {
        var messages = mutableListOf<String>()
        try{
            userService.create(dto)
            return ResponseEntity(HttpStatus.CREATED)
        }catch (e: BadRequestException){
            return formatErrorResponse(e.status, e.messages.toTypedArray())
        }catch (e: Exception){
            log.error("Ocorreu erro ao efetuar o cadastro", e)
            messages.add("Ocorreu erro ao efetuar o cadastro")
            return formatErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, messages.toTypedArray())
        }
    }
}