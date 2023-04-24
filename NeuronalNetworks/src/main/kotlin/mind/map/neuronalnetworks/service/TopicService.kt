package mind.map.neuronalnetworks.service

import mind.map.neuronalnetworks.model.TopicDTO
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
interface TopicService {

    fun getTopicTree(rootTopicId: String): Flux<TopicDTO>

    fun createSubtopic(rootTopicId: String, dto: TopicDTO): Mono<TopicDTO>
}
