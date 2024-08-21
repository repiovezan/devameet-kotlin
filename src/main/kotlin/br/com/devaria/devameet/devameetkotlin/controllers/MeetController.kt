package br.com.devaria.devameet.devameetkotlin.controllers

import br.com.devaria.devameet.devameetkotlin.dtos.UserUpdateRequestDto
import br.com.devaria.devameet.devameetkotlin.dtos.meet.MeetCreateRequestDto
import br.com.devaria.devameet.devameetkotlin.dtos.meet.MeetUpdateRequestDto
import br.com.devaria.devameet.devameetkotlin.exceptions.BadRequestException
import br.com.devaria.devameet.devameetkotlin.services.MeetService
import br.com.devaria.devameet.devameetkotlin.services.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/meet")
class MeetController(private val meetService: MeetService) : BaseController(MeetController::class.java.toString()) {

    @GetMapping
    fun getUserMeets(@RequestHeader("Authorization") authorization:String) : ResponseEntity<Any>{
        return try{
            val user = readToken(authorization)
            val meets = meetService.getUserMeets(user)
            ResponseEntity(meets, HttpStatus.OK)
        } catch (e: Exception){
            formatErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, mutableListOf("Ocorreu erro ao obter reuniões do usuario").toTypedArray())
        }
    }

    @GetMapping("/objects/{id}")
    fun getMeetObjects(
        @RequestHeader("Authorization") authorization:String,
        @PathVariable id: Long
    ) : ResponseEntity<Any>{
        return try{
            val user = readToken(authorization)
            val objects = meetService.getMeetObjects(id)
            ResponseEntity(objects, HttpStatus.OK)
        } catch (e: Exception){
            formatErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, mutableListOf("Ocorreu erro ao obter objetos da reunião do usuario").toTypedArray())
        }
    }

    @PostMapping
    fun saveMeet(
        @RequestHeader("Authorization") authorization:String,
        @RequestBody dto: MeetCreateRequestDto) : ResponseEntity<Any>{
        return try{
            val user = readToken(authorization)
            meetService.create(user, dto)
            ResponseEntity(HttpStatus.OK)
        } catch (bre: BadRequestException){
            formatErrorResponse(bre.status, bre.messages.toTypedArray())
        } catch (e: Exception){
            formatErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, mutableListOf("Ocorreu erro ao criar reunião do usuario").toTypedArray())
        }
    }

    @DeleteMapping("/{id}")
    fun deleteMeet(
        @RequestHeader("Authorization") authorization:String,
        @PathVariable id: Long
    ) : ResponseEntity<Any>{
        return try{
            val user = readToken(authorization)
            meetService.delete(user, id)
            ResponseEntity(HttpStatus.OK)
        } catch (e: Exception){
            formatErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, mutableListOf("Ocorreu erro ao deletar reunião do usuario").toTypedArray())
        }
    }

    @PutMapping("/{id}")
    fun updateMeet(
        @RequestHeader("Authorization") authorization:String,
        @PathVariable id: Long,
        @RequestBody dto: MeetUpdateRequestDto) : ResponseEntity<Any>{
        return try{
            val user = readToken(authorization)
            meetService.update(user, id, dto)
            ResponseEntity(HttpStatus.OK)
        } catch (bre: BadRequestException){
            formatErrorResponse(bre.status, bre.messages.toTypedArray())
        } catch (e: Exception){
            formatErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, mutableListOf("Ocorreu erro ao atualizar reunião do usuario").toTypedArray())
        }
    }
}