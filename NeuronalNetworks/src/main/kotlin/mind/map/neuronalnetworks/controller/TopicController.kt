package mind.map.neuronalnetworks.controller

import mind.map.neuronalnetworks.model.TopicDTO
import mind.map.neuronalnetworks.service.TopicService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("api/topic")
class TopicController(
    private val topicService: TopicService
) {

    @GetMapping("test")
    fun test(): String {
        return "test"
    }

    @PostMapping("/root/{rootTopicId}")
    fun createSubtopic(@PathVariable rootTopicId: String, @PathVariable subjectId: String, @RequestBody dto: TopicDTO): Mono<TopicDTO> {
        return topicService.createSubtopic(rootTopicId, subjectId, dto)
    }
}
