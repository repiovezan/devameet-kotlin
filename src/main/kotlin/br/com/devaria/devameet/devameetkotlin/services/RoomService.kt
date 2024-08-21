package br.com.devaria.devameet.devameetkotlin.services

import br.com.devaria.devameet.devameetkotlin.dtos.meet.MeetObjectRequestDto
import br.com.devaria.devameet.devameetkotlin.dtos.room.GetRoomResponseDto
import br.com.devaria.devameet.devameetkotlin.dtos.room.UpdatePositionRequestDto
import br.com.devaria.devameet.devameetkotlin.dtos.room.UpdateUserSocketResponseDto
import br.com.devaria.devameet.devameetkotlin.dtos.room.UpdateUsersSocketResponseDto
import br.com.devaria.devameet.devameetkotlin.entities.Position
import br.com.devaria.devameet.devameetkotlin.entities.User
import br.com.devaria.devameet.devameetkotlin.exceptions.BadRequestException
import br.com.devaria.devameet.devameetkotlin.repositories.MeetObjectRepository
import br.com.devaria.devameet.devameetkotlin.repositories.MeetRepository
import br.com.devaria.devameet.devameetkotlin.repositories.PositionRepository
import br.com.devaria.devameet.devameetkotlin.repositories.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import kotlin.jvm.Throws

@Service
class RoomService(
    private val userRepository: UserRepository,
    private val meetRepository: MeetRepository,
    private val meetObjectRepository: MeetObjectRepository,
    private val positionRepository: PositionRepository
) {
    private val log = LoggerFactory.getLogger(RoomService::class.java)

    @Throws(BadRequestException::class)
    fun getRoom(link: String) : GetRoomResponseDto{
        log.info("getRoom - start")

        val meet = meetRepository.findByLink(link)
        if(meet == null || meet.id <= 0){
            throw BadRequestException(mutableListOf("Reunião não encontrada"))
        }

        val meetObjects = meetObjectRepository.findAllByMeetId(meet.id)
        val objects = meetObjects.map { MeetObjectRequestDto(
            name = it.name,
            x = it.x,
            y = it.y,
            orientation = it.orientation,
            zIndex =  it.zIndex
        )}

        val result = GetRoomResponseDto(
            name = meet.name,
            color = meet.color,
            link = meet.link,
            objects
        )

        log.info("getRoom - success")
        return result
    }

    fun listPosition(link: String) : List<UpdateUserSocketResponseDto>{
        val meet = meetRepository.findByLink(link) ?: throw BadRequestException(mutableListOf("Reunião não encontrada"))

        return positionRepository.findAllByMeet(meet.id).map {
            UpdateUserSocketResponseDto(
                clientId = it.clientId,
                user = it.userPosition!!.id,
                name = it.name,
                avatar = it.avatar,
                x = it.x,
                y = it.y,
                orientation = it.orientation,
                muted = it.muted,
            )
        }
    }

    fun deletePositionByClientId(clientId: String){
        return positionRepository.deleteAllByClientId(clientId)
    }

    fun updateUserMuted(dto: UpdatePositionRequestDto){
        val meet = meetRepository.findByLink(dto.link) ?: throw BadRequestException(mutableListOf("Reunião não encontrada"))
        val position = positionRepository.findByClientId(dto.clientId) ?: throw BadRequestException(mutableListOf("Posição não encontrada"))

        position.muted = dto.muted
        positionRepository.save(position)
    }

    fun updateUserPosition(dto: UpdatePositionRequestDto) : Position{
        val user = userRepository.getReferenceById(dto.userId.toLong())
        val meet = meetRepository.findByLink(dto.link) ?: throw BadRequestException(mutableListOf("Reunião não encontrada"))

        val usersInRoom = positionRepository.findAllByMeet(meet.id)

        var loggedUserInRoom = usersInRoom.find { it.userPosition?.id == user.id || it.clientId == dto.clientId}

        if(loggedUserInRoom == null){
            if(usersInRoom != null && usersInRoom.size > 20){
                throw BadRequestException(mutableListOf("Reunião excedeu a quantidade de participante"))
            }

            loggedUserInRoom = Position(
                clientId = dto.clientId,
                userPosition = user,
                meetPosition = meet,
                avatar = user.avatar,
                name = user.name,
                x = 2,
                y = 2,
                orientation = "down"
            )
        }else{
            if(dto.x > 0 && dto.x < 8){
                loggedUserInRoom.x = dto.x
            }
            if(dto.y > 0 && dto.y < 8){
                loggedUserInRoom.y = dto.y
            }

            if(!dto.orientation.isNullOrEmpty() && !dto.orientation.isNullOrBlank()){
                loggedUserInRoom.orientation = dto.orientation
            }
        }

        positionRepository.save(loggedUserInRoom)
        return loggedUserInRoom
    }

    fun findByClientId(clientId: String): Position? {
        return positionRepository.findByClientId(clientId)
    }
}