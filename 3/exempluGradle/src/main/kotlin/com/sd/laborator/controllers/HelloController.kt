package com.sd.laborator.controllers

import com.sd.laborator.services.HelloService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class HelloController {

    val service: HelloService =
        HelloService()

    @RequestMapping(value = ["/helloworld"], method = [RequestMethod.GET])
    @ResponseBody
    fun hello() = service.getHello()
}