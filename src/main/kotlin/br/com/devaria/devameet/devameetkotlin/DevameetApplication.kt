package br.com.devaria.devameet.devameetkotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DevameetApplication

fun main(args: Array<String>) {
	runApplication<DevameetApplication>(*args)
}
