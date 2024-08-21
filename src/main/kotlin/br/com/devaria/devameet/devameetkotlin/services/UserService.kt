package br.com.devaria.devameet.devameetkotlin.services

import br.com.devaria.devameet.devameetkotlin.dtos.RegisterRequestDto
import br.com.devaria.devameet.devameetkotlin.dtos.UserUpdateRequestDto
import br.com.devaria.devameet.devameetkotlin.entities.User
import br.com.devaria.devameet.devameetkotlin.exceptions.BadRequestException
import br.com.devaria.devameet.devameetkotlin.repositories.UserRepository
import br.com.devaria.devameet.devameetkotlin.utils.encrypt
import br.com.devaria.devameet.devameetkotlin.utils.isEmailValid
import br.com.devaria.devameet.devameetkotlin.utils.isPasswordValid
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import kotlin.jvm.Throws

@Service
class UserService (
    @Value("\${devameet.secrets.aes-secret}")
    private val secret: String,
    private val userRepository: UserRepository
){
    private val log = LoggerFactory.getLogger(UserService::class.java)

    fun getUser(userId: Long) : User? {
        val optional =  userRepository.findById(userId)
        if(optional != null && optional.isPresent){
            return optional.get()
        }
        return null
    }

    @Throws(BadRequestException::class)
    fun create(dto: RegisterRequestDto){
        log.info("create - start")

        val messages = mutableListOf<String>()

        if(dto.email.isNullOrBlank() || dto.email.isNullOrEmpty() ||
            dto.name.isNullOrBlank() || dto.name.isNullOrEmpty() ||
            dto.password.isNullOrBlank() || dto.password.isNullOrEmpty()){
            messages.add("Favor preencher todos os campos obrigatorios")
            throw BadRequestException(messages)
        }

        if(!isEmailValid(dto.email)){
            messages.add("Email inválido")
        }

        if(!isPasswordValid(dto.password)){
            messages.add("Senha inválida, senha deve conter ao menos 1 maiúscula, 1 minúscula, 1 número, 1 caracter especial e 1 letra e ter no mínimo 8 caracteres")
        }

        if(dto.name.length < 2){
            messages.add("Nome inválido")
        }

        val existingUser = userRepository.findByEmail(dto.email)
        if(existingUser != null && existingUser.id > 0){
            messages.add("Já existe usuário com o email informado")
        }

        if(messages.size > 0){
            throw BadRequestException(messages)
        }

        val user = User(
            name = dto.name,
            email = dto.email,
            password = encrypt(dto.password, secret),
            avatar = dto.avatar
        )

        userRepository.save(user)
        log.info("create - success")
    }

    @Throws(BadRequestException::class)
    fun update(user: User, dto: UserUpdateRequestDto){
        log.info("update - start")

        val messages = mutableListOf<String>()

        if(!dto.name.isNullOrBlank() && dto.name.length < 2){
            messages.add("Nome inválido")
        }else if(!dto.name.isNullOrEmpty() && !dto.name.isNullOrBlank()) {
            user.name = dto.name
        }

        if(!dto.avatar.isNullOrBlank() && dto.avatar.length < 5){
            messages.add("Avatar inválido")
        }else if(!dto.avatar.isNullOrEmpty() && !dto.avatar.isNullOrBlank()) {
            user.avatar = dto.avatar
        }

        if(messages.size > 0){
            throw BadRequestException(messages)
        }

        userRepository.save(user)
        log.info("update - finish success")
    }
}