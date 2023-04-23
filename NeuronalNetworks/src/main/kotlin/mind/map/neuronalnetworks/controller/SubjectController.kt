package mind.map.neuronalnetworks.controller

import mind.map.neuronalnetworks.model.CreateSubjectDTO
import mind.map.neuronalnetworks.model.SubjectDTO
import mind.map.neuronalnetworks.model.SubjectSearchDTO
import mind.map.neuronalnetworks.service.SubjectService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("api/subject")
class SubjectController(
    private val subjectService: SubjectService
) {

    @GetMapping("test")
    fun test(): Flux<String> {
        // returns 10 numbers with a small delay
        return Flux.range(1, 10).delayElements(java.time.Duration.ofMillis(500)).map { it.toString() }
    }

    @GetMapping("")
    fun getAllSubjects(): Flux<SubjectDTO> {
        return subjectService.getAllSubjects()
    }

    @GetMapping("search")
    fun getSubjects(
        @RequestParam page: Int = 1,
        @RequestParam size: Int = 6,
        @RequestParam search: String?
    ): Flux<SubjectSearchDTO> {
        return subjectService.getPaginatedSubjectsBySearch(search, page, size)
    }

    @GetMapping("{subjectId}")
    fun getSubject(@PathVariable subjectId: String): Mono<SubjectDTO> {
        return subjectService.getSubjectById(subjectId)
    }

    @PostMapping("")
    fun createSubject(@RequestBody subject: CreateSubjectDTO): Mono<SubjectDTO> {
        return subjectService.createSubject(subject)
    }
}
