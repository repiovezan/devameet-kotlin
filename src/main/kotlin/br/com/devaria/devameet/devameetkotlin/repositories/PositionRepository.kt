package br.com.devaria.devameet.devameetkotlin.repositories

import br.com.devaria.devameet.devameetkotlin.entities.Position
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
interface PositionRepository : JpaRepository<Position, Long>{

    fun findByClientId(clientId:String): Position?

    @Modifying
    @Query("SELECT p FROM Position p WHERE p.meetPosition.id = :id")
    fun findAllByMeet(@Param("id") meetId:Long): List<Position>

    @Transactional
    @Modifying
    @Query("DELETE FROM Position p WHERE p.clientId = :id")
    fun deleteAllByClientId(@Param("id") clientId: String)
}