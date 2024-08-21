package br.com.devaria.devameet.devameetkotlin.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import javax.persistence.*

@Entity
data class Meet(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id:Long = 0,
    var name: String = "",
    val link: String = "",
    var color: String = "",

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    val user: User? = null,

    @JsonBackReference
    @OneToMany(mappedBy = "meet")
    val objects: List<MeetObject> = emptyList(),

    @JsonBackReference
    @OneToMany(mappedBy = "meetPosition")
    val positions : List<Position> = emptyList()
)