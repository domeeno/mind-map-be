package mind.map.neuronalnetworks.service

import mind.map.neuronalnetworks.model.TopicDTO
import mind.map.neuronalnetworks.model.toTopicDTO
import mind.map.neuronalnetworks.repository.TopicRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class TopicServiceImpl(
    private val topicRepository: TopicRepository
) : TopicService {

    override fun createSubtopic(rootTopicId: String, subjectId: String, dto: TopicDTO): Mono<TopicDTO> {
        TODO("Not yet implemented")
    }

    override fun getSubjectTopics(subjectId: String): Flux<TopicDTO> {
        return topicRepository.findAllBySubjectIdOrderByCreateTimestampDesc(subjectId).map {
            it.toTopicDTO()
        }
    }

    override fun getTopics(): Flux<TopicDTO> {
        return topicRepository.findAll().map { it.toTopicDTO() }
    }
}
