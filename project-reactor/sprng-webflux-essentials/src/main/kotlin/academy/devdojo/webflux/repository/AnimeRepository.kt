package academy.devdojo.webflux.repository

import academy.devdojo.webflux.domain.Anime
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface AnimeRepository : ReactiveCrudRepository<Anime, Int>{

}