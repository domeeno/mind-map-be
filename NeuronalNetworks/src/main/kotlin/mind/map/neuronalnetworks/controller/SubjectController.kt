package mind.map.neuronalnetworks.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("api/subject")
class SubjectController {

    @GetMapping("test")
    fun test(): Flux<String> {
        // returns 10 numbers with a small delay
        return Flux.range(1, 10).delayElements(java.time.Duration.ofMillis(500)).map { it.toString() }
    }
}
