package br.com.devaria.devameet.devameetkotlin.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import javax.persistence.*

@Entity
data class MeetObject(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id:Long = 0,
    val name: String = "",
    val x: Int = 0,
    val y: Int = 0,
    val zIndex: Int = 0,
    val orientation: String = "",

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "meetId")
    val meet: Meet? = null,
)