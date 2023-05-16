package mind.map.neuronalnetworks.controller

import mind.map.neuronalnetworks.model.CreateTopicInput
import mind.map.neuronalnetworks.model.TopicDTO
import mind.map.neuronalnetworks.service.TopicService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("api/topic")
class TopicController(
    private val topicService: TopicService
) {

    @GetMapping("tree/{subjectId}")
    fun getSubjectTopics(@PathVariable subjectId: String): Flux<TopicDTO> {
        return topicService.getSubjectTopics(subjectId)
    }

    @PostMapping()
    fun createTopic(@RequestBody createTopicInput: CreateTopicInput): Flux<TopicDTO> {
        return topicService.createTopic(createTopicInput)
    }

    @PutMapping("{topicId}")
    fun updateTopic(@PathVariable topicId: String, @RequestBody topicDTO: TopicDTO): Mono<TopicDTO> {
        return topicService.updateTopic(topicId, topicDTO)
    }

    @DeleteMapping("{topicId}")
    fun deleteTopicBranch(
        @PathVariable topicId: String,
        @RequestParam deleteBranch: Boolean,
        @RequestParam newParentId: String?
    ): Mono<String> {
        if (deleteBranch) {
            return topicService.deleteTopicBranch(topicId)
        }

        return topicService.deleteTopic(topicId, newParentId)
    }

    @GetMapping()
    fun getTopics(): Flux<TopicDTO> {
        return topicService.getTopics()
    }

    @GetMapping("/branch/{topicId}")
    fun getBranch(@PathVariable topicId: String): Flux<TopicDTO> {
        return topicService.getBranch(topicId)
    }
}
