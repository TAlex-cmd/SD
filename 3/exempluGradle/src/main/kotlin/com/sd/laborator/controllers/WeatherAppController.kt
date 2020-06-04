package com.sd.laborator.controllers

import com.sd.laborator.interfaces.HtmlPrettyPrinterInterface
import com.sd.laborator.interfaces.LocationSearchInterface
import com.sd.laborator.interfaces.WeatherForecastInterface
import com.sd.laborator.pojo.WeatherForecastData
import com.sd.laborator.services.HtmlPrettyPrinterService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
class WeatherAppController {
    @Autowired
    private lateinit var locationSearchService: LocationSearchInterface

    @Autowired
    private lateinit var weatherForecastService: WeatherForecastInterface

    @Autowired
    private lateinit var chainedService: HtmlPrettyPrinterInterface

    @RequestMapping("/getforecast/{location}", method = [RequestMethod.GET])
//    @ResponseBody
    fun getForecast(@PathVariable location: String, @RequestParam("pretty", required = false) pretty: String?): ResponseEntity<String> {
        // se incearca preluarea WOEID-ului locaţiei primite in URL
        val locationId = locationSearchService.getLocationId(location)
        // dacă locaţia nu a fost găsită, răspunsul va fi corespunzător
        if (locationId == -1) {
            return ResponseEntity.ok("Nu s-au putut gasi date meteo pentru cuvintele cheie\"$location\"!")
        }

        // aici e partea de orchestrare, care dirijeaza controlu' catre unu' din servicii in functie de ce se cere
        if(pretty == null){
            // pe baza ID-ului de locaţie, se interoghează al doilea serviciu care returnează datele meteo
            // încapsulate într-un obiect POJO
            val rawForecastData: WeatherForecastData = weatherForecastService.getForecastData(locationId)
            // fiind obiect POJO, funcţia toString() este suprascrisă pentru o afişare mai prietenoasă
            return ResponseEntity.ok(rawForecastData.toString())
        }else{
            // aici se face partea de chaining, in serviciul ala care face html frumos din orice obiect
            chainedService.setNextService(weatherForecastService)
            val headers = HttpHeaders()
            headers.contentType = MediaType.TEXT_HTML;
            return ResponseEntity.ok().headers(headers).body(chainedService.getPrettyHtml(locationId));
        }
    }
}