package mind.map.neuronalnetworks.service

import mind.map.neuronalnetworks.model.CreateTopicInput
import mind.map.neuronalnetworks.model.Topic
import mind.map.neuronalnetworks.model.TopicDTO
import mind.map.neuronalnetworks.model.TopicType
import mind.map.neuronalnetworks.model.toTopicDTO
import mind.map.neuronalnetworks.repository.GraphLookupRepository
import mind.map.neuronalnetworks.repository.TopicRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Service
class TopicServiceImpl(
    private val topicRepository: TopicRepository,
    private val graphLookupRepository: GraphLookupRepository
) : TopicService {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun createTopic(createTopicInput: CreateTopicInput): Flux<TopicDTO> {
        val topic = Topic(
            subjectId = createTopicInput.subjectId,
            parentIds = listOf(createTopicInput.parentId),
            topicName = createTopicInput.topicName
        )

        val newTopic = topicRepository.save(topic)

        val updateParentTopic = newTopic
            .flatMapMany {
                topicRepository.findById(createTopicInput.parentId)
                    .flatMap { parentTopic ->
                        parentTopic.childIds = parentTopic.childIds.plus(it.id)
                        topicRepository.save(parentTopic)
                    }
            }

        return newTopic.concatWith(updateParentTopic).map { it.toTopicDTO() }
    }

    override fun updateTopic(topicId: String, topicDTO: TopicDTO): Mono<TopicDTO> {
        return topicRepository.findById(topicId)
            .flatMap {
                it.topicName = topicDTO.topicName
                it.color = topicDTO.color
                it.weight = topicDTO.weight
                it.parentIds = topicDTO.parentIds
                it.childIds = topicDTO.childIds
                it.tags = topicDTO.tags
                it.documentId = topicDTO.documentId
                it.updateTimestamp = LocalDateTime.now()
                it.type = topicDTO.type
                topicRepository.save(it)
            }.map { it.toTopicDTO() }
    }

    override fun getSubjectTopics(subjectId: String): Flux<TopicDTO> {
        return topicRepository.findAllBySubjectIdOrderByCreateTimestampAsc(subjectId).map {
            it.toTopicDTO()
        }
    }

    override fun getBranch(topicId: String): Flux<TopicDTO> {
        return graphLookupRepository.getBranch(topicId)
            .flatMapIterable {
                it.childTopics
            }
            .map { it.toTopicDTO() }
            .onErrorResume {
                println(it)
                Flux.empty()
            }
    }

    override fun getTopics(): Flux<TopicDTO> {
        return topicRepository.findAll().map { it.toTopicDTO() }
    }

    override fun deleteTopic(topicId: String, newParentId: String?): Mono<String> {
        return if (!newParentId.isNullOrBlank()) {
            deleteAndAssignToParent(topicId, newParentId)
        } else {
            deleteAndAssign(topicId)
        }
    }

    private fun deleteAndAssign(
        topicId: String
    ) = topicRepository.findById(topicId)
        .flatMap { dbTopic ->
            graphLookupRepository.findByArrayFieldContaining(dbTopic.id)
                .flatMap { parentTopic ->
                    parentTopic.childIds = parentTopic.childIds.filter { refId -> refId != dbTopic.id }
                    if (dbTopic.childIds.isNotEmpty()) {
                        parentTopic.childIds = parentTopic.childIds.plus(dbTopic.childIds)
                    }
                    topicRepository.save(parentTopic)
                }
                .flatMap {
                    topicRepository.findAllById(dbTopic.childIds)
                        .flatMap {
                            it.parentIds = it.parentIds.filter { refId -> refId != dbTopic.id }
                            it.parentIds = dbTopic.parentIds
                            topicRepository.save(it)
                        }
                }
                .then(Mono.just("Deleted"))
        }

    private fun deleteAndAssignToParent(
        topicId: String,
        newParentId: String
    ) = topicRepository.findById(topicId)
        .filter { it.type != TopicType.ROOT }
        .flatMap { dbTopic ->
            graphLookupRepository.getBranch(topicId)
                .filter { !it.childTopics.isNullOrEmpty() }
                .flatMap {
                    if (it.childTopics!!.any { childTopic -> childTopic.id == newParentId }) {
                        Mono.error<String>(Exception("Cannot move topic to its child"))
                    } else {
                        Mono.just(it)
                    }
                }.flatMap {
                    graphLookupRepository.findByArrayFieldContaining(dbTopic.id)
                        .flatMap {
                            it.childIds = it.childIds.filter { refId -> refId != dbTopic.id }
                            topicRepository.save(it)
                        }
                        .flatMap {
                            topicRepository.findById(newParentId)
                                .flatMap { newParentTopic ->
                                    newParentTopic.childIds =
                                        newParentTopic.childIds.filter { it != dbTopic.id }
                                    newParentTopic.childIds = newParentTopic.childIds.plus(dbTopic.childIds)
                                    topicRepository.save(newParentTopic)
                                }
                        }
                        .flatMap {
                            topicRepository.findAllById(dbTopic.childIds)
                                .flatMap { childTopic ->
                                    childTopic.parentIds = childTopic.parentIds.filter { it != dbTopic.id }
                                    childTopic.parentIds = childTopic.parentIds.plus(newParentId)
                                    topicRepository.save(childTopic)
                                }
                        }
                        .flatMap {
                            topicRepository.delete(dbTopic)
                        }
                }
                .then(Mono.just("Deleted"))
        }

    override fun deleteTopicBranch(topicId: String): Mono<String> {
        return topicRepository.findById(topicId)
            .filter { it.type != TopicType.ROOT }
            .flatMap { dbTopic ->
                graphLookupRepository.findByArrayFieldContaining(dbTopic.id)
                    .flatMap { topic ->
                        topic.childIds = topic.childIds.filter { it != dbTopic.id }
                        topicRepository.save(topic)
                    }
                    .flatMap {
                        graphLookupRepository.getBranch(dbTopic.id)
                            .flatMap {
                                if (it.childTopics.isNullOrEmpty()) {
                                    topicRepository.deleteById(dbTopic.id)
                                } else {
                                    topicRepository.deleteAll(it.childTopics!!)
                                        .then(topicRepository.deleteById(dbTopic.id))
                                }
                            }
                    }
                    .then(Mono.just("Deleted"))
                    .onErrorResume {
                        Mono.just("Error")
                    }
            }
    }
}
