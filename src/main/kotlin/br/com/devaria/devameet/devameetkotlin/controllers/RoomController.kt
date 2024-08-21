package br.com.devaria.devameet.devameetkotlin.controllers

import br.com.devaria.devameet.devameetkotlin.exceptions.BadRequestException
import br.com.devaria.devameet.devameetkotlin.services.RoomService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/room")
class RoomController(private val roomService: RoomService) : BaseController(RoomController::class.java.toString()) {

    @GetMapping("/{link}")
    fun getRoom(@PathVariable link: String) : ResponseEntity<Any>{
        try{
            val result = roomService.getRoom(link)
            return ResponseEntity(result, HttpStatus.OK)
        }catch (be: BadRequestException){
            return formatErrorResponse(be.status, be.messages.toTypedArray())
        }catch (e: Exception){
            log.error("Ocorreu erro ao buscar sala", e)
            return formatErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                mutableListOf("Ocorreu erro ao buscar sala").toTypedArray())
        }
    }
}