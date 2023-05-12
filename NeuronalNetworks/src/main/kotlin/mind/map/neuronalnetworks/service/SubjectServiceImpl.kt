package mind.map.neuronalnetworks.service

import mind.map.neuronalnetworks.model.CreateSubjectDTO
import mind.map.neuronalnetworks.model.SubjectDTO
import mind.map.neuronalnetworks.model.SubjectSearchDTO
import mind.map.neuronalnetworks.model.Topic
import mind.map.neuronalnetworks.model.TopicType
import mind.map.neuronalnetworks.model.toSubject
import mind.map.neuronalnetworks.model.toSubjectDTO
import mind.map.neuronalnetworks.model.toSubjectSearchDTO
import mind.map.neuronalnetworks.repository.SubjectRepository
import mind.map.neuronalnetworks.repository.TopicRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Service
class SubjectServiceImpl(
    private val subjectRepository: SubjectRepository,
    private val topicRepository: TopicRepository
) : SubjectService {

    override fun getAllSubjects(): Flux<SubjectDTO> {
        return subjectRepository.findAllByOrderByCreateTimestampAsc()
            .delayElements(java.time.Duration.ofMillis(500))
            .map { it.toSubjectDTO(null) }
    }

    override fun getSubjectById(id: String): Mono<SubjectDTO> {
        return subjectRepository.findById(id).map { it.toSubjectDTO(null) }
    }

    override fun getSubjectsByUserId(userId: String): Flux<SubjectDTO> {
        return subjectRepository.findAllByUserId(userId).map { it.toSubjectDTO(null) }
    }

    override fun getSubjectsByTag(tag: String): Flux<SubjectDTO> {
        return subjectRepository.findAllByTags(tag).map { it.toSubjectDTO(null) }
    }

    override fun getSubjectsByTags(tags: List<String>): Flux<SubjectDTO> {
        return subjectRepository.findAllByTags(tags).map { it.toSubjectDTO(null) }
    }

    override fun getPaginatedSubjectsBySearch(search: String?, page: Int, size: Int): Flux<SubjectSearchDTO> {
        val pageable: Pageable = PageRequest.of(page, size)
        return subjectRepository.searchByName(search ?: "", pageable).map { it.toSubjectSearchDTO() }
    }

    override fun createSubject(subjectDTO: CreateSubjectDTO): Mono<SubjectDTO> {
        return subjectRepository.save(subjectDTO.toSubject())
            .flatMap { savedSubject ->
                topicRepository.save(
                    Topic(
                        topicName = subjectDTO.subjectName,
                        tags = subjectDTO.tags,
                        userId = subjectDTO.userId,
                        type = TopicType.ROOT,
                        subjectId = savedSubject.id
                    )
                ).map {
                    savedSubject.toSubjectDTO(it.id)
                }
            }
    }

    override fun updateSubject(subjectDTO: SubjectDTO): Mono<SubjectDTO> {
        return subjectRepository.findById(subjectDTO.id)
            .map {
                it.copy(
                    subjectName = subjectDTO.subjectName,
                    description = subjectDTO.description,
                    tags = subjectDTO.tags,
                    updateTimestamp = LocalDateTime.now()
                )
            }
            .flatMap { subjectRepository.save(it) }
            .map { it.toSubjectDTO(null) }
    }
}
