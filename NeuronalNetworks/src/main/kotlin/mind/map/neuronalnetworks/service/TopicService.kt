package mind.map.neuronalnetworks.service

import mind.map.neuronalnetworks.model.CreateTopicInput
import mind.map.neuronalnetworks.model.TopicDTO
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
interface TopicService {
    fun createTopic(createTopicInput: CreateTopicInput): Flux<TopicDTO>

    fun updateTopic(topicId: String, topicDTO: TopicDTO): Mono<TopicDTO>

    fun getBranch(topicId: String): Flux<TopicDTO>

    fun getSubjectTopics(subjectId: String): Flux<TopicDTO>

    fun getTopics(): Flux<TopicDTO>

    fun deleteTopic(topicId: String, newParentId: String?): Mono<String>

    fun deleteTopicBranch(topicId: String): Mono<String>
}
