package mind.map.neuronalnetworks.service

import mind.map.neuronalnetworks.model.TopicDTO
import mind.map.neuronalnetworks.model.toTopic
import mind.map.neuronalnetworks.model.toTopicDTO
import mind.map.neuronalnetworks.repository.GraphLookupRepositoryImpl
import mind.map.neuronalnetworks.repository.TopicRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class TopicServiceImpl(
    private val graphLookupRepository: GraphLookupRepositoryImpl,
    private val topicRepository: TopicRepository
) : TopicService {

    override fun getTopicTree(rootTopicId: String): Flux<TopicDTO> {
        return graphLookupRepository.getTopicsWithSubtopics(rootTopicId).map { it.toTopicDTO() }
    }

    override fun createSubtopic(rootTopicId: String, dto: TopicDTO): Mono<TopicDTO> {
        return topicRepository.save(dto.toTopic(rootTopicId)).map { it.toTopicDTO() }
    }
}
