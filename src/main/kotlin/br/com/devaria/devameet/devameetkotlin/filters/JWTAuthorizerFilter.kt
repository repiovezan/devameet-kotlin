package br.com.devaria.devameet.devameetkotlin.filters

import br.com.devaria.devameet.devameetkotlin.repositories.UserRepository
import br.com.devaria.devameet.devameetkotlin.utils.JWTUtils
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTAuthorizerFilter(
    authenticationManager: AuthenticationManager,
    val jwtUtils: JWTUtils,
    val userRepository: UserRepository) : BasicAuthenticationFilter(authenticationManager) {

    val authorization = "Authorization"
    val bearer = "Bearer"

    override fun doFilterInternal(request: HttpServletRequest,
                                  response: HttpServletResponse,
                                  chain: FilterChain) {
        val authorizationHeader = request.getHeader(authorization)

        if(authorizationHeader !=null && authorizationHeader.startsWith(bearer)){
            val autorizado = getAuthentication(authorizationHeader)
            SecurityContextHolder.getContext().authentication = autorizado
        }

        chain.doFilter(request, response)
    }

    private fun getAuthentication(authorizationHeader: String): UsernamePasswordAuthenticationToken {
        val token = authorizationHeader.substring(7)
        if(jwtUtils.isTokenValid(token)){
            val idString = jwtUtils.getUserId(token)
            if(!idString.isNullOrEmpty() && !idString.isNullOrBlank()){
                val usuario = userRepository.findByIdOrNull(idString.toLong())
                    ?: throw UsernameNotFoundException("Token informado não é válido")

                val usuarioImpl = UserDetailImpl(usuario)
                return UsernamePasswordAuthenticationToken(usuarioImpl, null, usuarioImpl.authorities)
            }
        }

        throw UsernameNotFoundException("Token informado não é válido")
    }
}