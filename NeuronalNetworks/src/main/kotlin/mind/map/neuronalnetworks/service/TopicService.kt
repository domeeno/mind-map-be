package mind.map.neuronalnetworks.service

import mind.map.neuronalnetworks.model.TopicDTO
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
interface TopicService {
    fun createSubtopic(rootTopicId: String, subjectId: String, dto: TopicDTO): Mono<TopicDTO>
}
