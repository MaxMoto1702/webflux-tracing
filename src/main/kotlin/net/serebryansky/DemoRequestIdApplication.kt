package net.serebryansky

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DemoRequestIdApplication

fun main(args: Array<String>) {
	runApplication<DemoRequestIdApplication>(*args)
}
