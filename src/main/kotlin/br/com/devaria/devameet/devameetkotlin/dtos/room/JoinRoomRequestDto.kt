package br.com.devaria.devameet.devameetkotlin.dtos.room

data class JoinRoomRequestDto(
    val userId: Long = 0,
    val link: String = "",
    var clientId:String = ""
)
