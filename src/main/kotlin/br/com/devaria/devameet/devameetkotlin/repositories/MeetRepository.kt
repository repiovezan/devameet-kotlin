package br.com.devaria.devameet.devameetkotlin.repositories

import br.com.devaria.devameet.devameetkotlin.entities.Meet
import br.com.devaria.devameet.devameetkotlin.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MeetRepository : JpaRepository<Meet, Long> {

    fun findAllByUserId(userId: Long) : List<Meet>
    fun findByLink(link:String): Meet?
}