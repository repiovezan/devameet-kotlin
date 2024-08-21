package br.com.devaria.devameet.devameetkotlin.dtos.room

import br.com.devaria.devameet.devameetkotlin.dtos.meet.MeetObjectRequestDto
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class GetRoomResponseDto(
    val name: String,
    val color: String,
    val link: String,
    val objects: List<MeetObjectRequestDto>
)