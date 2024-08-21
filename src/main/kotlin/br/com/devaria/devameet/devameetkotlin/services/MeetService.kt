package br.com.devaria.devameet.devameetkotlin.services

import br.com.devaria.devameet.devameetkotlin.dtos.LoginRequestDto
import br.com.devaria.devameet.devameetkotlin.dtos.LoginResponseDto
import br.com.devaria.devameet.devameetkotlin.dtos.meet.MeetCreateRequestDto
import br.com.devaria.devameet.devameetkotlin.dtos.meet.MeetObjectRequestDto
import br.com.devaria.devameet.devameetkotlin.dtos.meet.MeetUpdateRequestDto
import br.com.devaria.devameet.devameetkotlin.entities.Meet
import br.com.devaria.devameet.devameetkotlin.entities.MeetObject
import br.com.devaria.devameet.devameetkotlin.entities.User
import br.com.devaria.devameet.devameetkotlin.exceptions.BadRequestException
import br.com.devaria.devameet.devameetkotlin.repositories.MeetObjectRepository
import br.com.devaria.devameet.devameetkotlin.repositories.MeetRepository
import br.com.devaria.devameet.devameetkotlin.repositories.UserRepository
import br.com.devaria.devameet.devameetkotlin.utils.JWTUtils
import br.com.devaria.devameet.devameetkotlin.utils.decrypt
import br.com.devaria.devameet.devameetkotlin.utils.generateLink
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import kotlin.jvm.Throws

@Service
class MeetService(
   private val meetRepository: MeetRepository,
    private val meetObjectRepository: MeetObjectRepository) {

    private val log = LoggerFactory.getLogger(MeetService::class.java)

    fun getUserMeets(user:User) : List<Meet>{
        return meetRepository.findAllByUserId(user.id)
    }

    fun getMeetObjects(meetId: Long) : List<MeetObject>{
        return meetObjectRepository.findAllByMeetId(meetId)
    }
    @Throws(BadRequestException::class)
    fun delete(user: User, meetId: Long){
        val meet = meetRepository.getReferenceById(meetId)
        if(meet != null && meet.user?.id == user.id){
            meetObjectRepository.deleteAllByMeetId(meetId)
            return meetRepository.delete(meet)
        }
        throw BadRequestException(mutableListOf("Meet não encontrado"))
    }

    @Throws(BadRequestException::class)
    fun create(user: User, dto: MeetCreateRequestDto){
        log.info("create - start")
        val messages = mutableListOf<String>()

        if(dto.name.isNullOrBlank() || dto.name.isNullOrEmpty() ||
            dto.color.isNullOrBlank() || dto.color.isNullOrEmpty()){
            messages.add("Favor preencher os campos")
            throw BadRequestException(messages)
        }

        if(dto.name.length < 2){
            messages.add("Nome inválido")
        }

        if(dto.color.length < 3){
            messages.add("Cor inválida")
        }

        if(messages.size > 0){
            throw BadRequestException(messages)
        }

        val meet = Meet(
            name = dto.name,
            color = dto.color,
            user = user,
            link = generateLink()
        )

        meetRepository.save(meet)
        log.info("create - finish success")
    }

    @Throws(BadRequestException::class)
    fun update(user: User, meetId: Long, dto: MeetUpdateRequestDto){
        log.info("update - start")
        val messages = mutableListOf<String>()

        val meet = meetRepository.getReferenceById(meetId)
        if(meet == null || meet.user?.id != user.id){
            messages.add("Reunião não encontrada")
            throw BadRequestException(messages)
        }

        if(!dto.name.isNullOrBlank() && dto.name.length < 2){
            messages.add("Nome inválido")
        }else if(!dto.name.isNullOrBlank()){
            meet.name = dto.name
        }

        if(!dto.color.isNullOrBlank() && dto.color.length < 3){
            messages.add("Cor inválida")
        }else if(!dto.color.isNullOrBlank()){
            meet.color = dto.color
        }

        var obj : MeetObjectRequestDto
        for (i in 0 until dto.objects.size){
            obj = dto.objects[i]

            if(obj.name.isNullOrBlank() || obj.name.length < 2){
                messages.add("Nome do objeto inválido na posição: $i")
            }

            if(obj.x < 0 || obj.x > 8){
                messages.add("Eixo x inválido na posição: $i")
            }

            if(obj.y < 0 || obj.y > 8){
                messages.add("Eixo y inválido na posição: $i")
            }
            if(obj.zIndex < 0){
                messages.add("Zindex inválido na posição: $i")
            }
        }

        if(messages.size > 0){
            throw BadRequestException(messages)
        }

        meetRepository.save(meet)
        meetObjectRepository.deleteAllByMeetId(meet.id)

        var meetObj: MeetObject
        dto.objects.forEach {
            meetObj = MeetObject(
                meet = meet,
                name = it.name,
                x = it.x,
                y = it.y,
                zIndex = it.zIndex,
                orientation = it.orientation
            )

            meetObjectRepository.save(meetObj)
        }

        log.info("update - finish success")
    }
}