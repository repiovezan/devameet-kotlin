package br.com.devaria.devameet.devameetkotlin.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany


@Entity
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val email: String = "",
    var name: String = "",

    @JsonIgnore
    var password: String = "",
    var avatar: String = "",

    @JsonBackReference
    @OneToMany(mappedBy = "user")
    val meets : List<Meet> = emptyList(),

    @JsonBackReference
    @OneToMany(mappedBy = "userPosition")
    val positions : List<Position> = emptyList()
)
