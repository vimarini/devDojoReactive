package academy.devdojo.webflux.controller

import academy.devdojo.webflux.domain.Anime
import academy.devdojo.webflux.repository.AnimeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("animes")
class AnimeController {

    @Autowired
    lateinit var animeRepository : AnimeRepository

    @GetMapping
    fun getAll() : Flux<Anime> {
        return animeRepository.findAll()
    }

}