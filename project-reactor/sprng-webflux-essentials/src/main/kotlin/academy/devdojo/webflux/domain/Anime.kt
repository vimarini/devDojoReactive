package academy.devdojo.webflux.domain

import org.jetbrains.annotations.NotNull
import org.springframework.data.annotation.Id
import javax.validation.constraints.NotEmpty

data class Anime(
    @field:Id
    val id: Int,
    @field:NotNull
    @field:NotEmpty(message = "Name can't be empty!")
    val name: String
)