package br.com.devaria.devameet.devameetkotlin.services

import br.com.devaria.devameet.devameetkotlin.dtos.LoginRequestDto
import br.com.devaria.devameet.devameetkotlin.dtos.LoginResponseDto
import br.com.devaria.devameet.devameetkotlin.exceptions.BadRequestException
import br.com.devaria.devameet.devameetkotlin.repositories.UserRepository
import br.com.devaria.devameet.devameetkotlin.utils.JWTUtils
import br.com.devaria.devameet.devameetkotlin.utils.decrypt
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import kotlin.jvm.Throws

@Service
class LoginService(
    @Value("\${devameet.secrets.aes-secret}")
    private val secret: String,
    private val userRepository: UserRepository) {

    private val log = LoggerFactory.getLogger(LoginService::class.java)

    @Autowired
    private lateinit var jwtUtils: JWTUtils

    @Throws(BadRequestException::class)
    fun login(dto:LoginRequestDto) : LoginResponseDto{
        log.info("login - start")
        val messages = mutableListOf<String>()

        if(dto.login.isNullOrBlank() || dto.login.isNullOrEmpty() ||
            dto.password.isNullOrBlank() || dto.password.isNullOrEmpty()){
            messages.add("Favor preencher os campos")
            throw BadRequestException(messages)
        }

        val existingUser = userRepository.findByEmail(dto.login)
        if(existingUser == null){
            messages.add("Usuário e senha não encontrado")
            throw BadRequestException(messages)
        }

        val passwordDecrypted = decrypt(existingUser.password, secret)
        if(passwordDecrypted != dto.password){
            messages.add("Usuário e senha não encontrado")
            throw BadRequestException(messages)
        }

        val token = jwtUtils.generateToken(existingUser.id.toString())

        val response = LoginResponseDto(token, existingUser.name, existingUser.email)

        log.info("login - finish success")
        return response
    }
}