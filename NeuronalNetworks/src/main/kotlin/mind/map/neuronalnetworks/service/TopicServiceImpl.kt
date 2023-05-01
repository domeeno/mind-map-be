package mind.map.neuronalnetworks.service

import mind.map.neuronalnetworks.model.TopicDTO
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class TopicServiceImpl() : TopicService {

    override fun createSubtopic(rootTopicId: String, subjectId: String, dto: TopicDTO): Mono<TopicDTO> {
        TODO("Not yet implemented")
    }
}
