package br.com.devaria.devameet.devameetkotlin.controllers

import br.com.devaria.devameet.devameetkotlin.dtos.UserUpdateRequestDto
import br.com.devaria.devameet.devameetkotlin.exceptions.BadRequestException
import br.com.devaria.devameet.devameetkotlin.services.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(private val userService: UserService) : BaseController(UserController::class.java.toString()) {

    @GetMapping
    fun getUser(@RequestHeader("Authorization") authorization:String) : ResponseEntity<Any>{
        try{
            val result = readToken(authorization)
            return ResponseEntity(result, HttpStatus.OK)
        }
        catch (e: Exception){
            return formatErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, mutableListOf("Ocorreu erro ao obter dados do usuario").toTypedArray())
        }
    }

    @PutMapping
    fun getUser(
        @RequestHeader("Authorization") authorization:String,
        @RequestBody dto: UserUpdateRequestDto) : ResponseEntity<Any>{
        return try{
            val user = readToken(authorization)
            userService.update(user, dto)
            ResponseEntity(HttpStatus.OK)
        } catch (bre: BadRequestException){
            formatErrorResponse(bre.status, bre.messages.toTypedArray())
        } catch (e: Exception){
            formatErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, mutableListOf("Ocorreu erro ao obter dados do usuario").toTypedArray())
        }
    }
}