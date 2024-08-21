package br.com.devaria.devameet.devameetkotlin.repositories

import br.com.devaria.devameet.devameetkotlin.entities.MeetObject
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
interface MeetObjectRepository : JpaRepository<MeetObject, Long> {

    fun findAllByMeetId(meetId: Long) : List<MeetObject>

    @Transactional
    @Modifying
    @Query("DELETE FROM MeetObject o where o.meet.id = :id")
    fun deleteAllByMeetId(@Param("id") meetId: Long)
}