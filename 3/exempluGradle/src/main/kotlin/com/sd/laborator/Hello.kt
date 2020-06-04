package com.sd.laborator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Hello {
    companion object {
        @JvmStatic
        fun main(args: Array<String>){
            runApplication<Hello>(*args)
        }
    }
}