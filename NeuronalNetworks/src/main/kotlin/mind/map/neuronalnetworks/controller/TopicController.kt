package mind.map.neuronalnetworks.controller

import mind.map.neuronalnetworks.model.TopicDTO
import mind.map.neuronalnetworks.service.TopicService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("api/topic")
class TopicController(
    private val topicService: TopicService
) {

    @GetMapping("tree/{subjectId}")
    fun getSubjectTopics(@PathVariable subjectId: String): Flux<TopicDTO> {
        return topicService.getSubjectTopics(subjectId)
    }

    @GetMapping()
    fun getTopics(): Flux<TopicDTO> {
        return topicService.getTopics()
    }

}