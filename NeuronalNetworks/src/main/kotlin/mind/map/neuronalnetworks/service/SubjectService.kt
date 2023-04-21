package mind.map.neuronalnetworks.service

import mind.map.neuronalnetworks.model.SubjectDTO
import mind.map.neuronalnetworks.model.SubjectSearchDTO
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
interface SubjectService {

    fun getAllSubjects(): Flux<SubjectDTO>

    fun getSubjectById(id: String): Mono<SubjectDTO>

    fun getSubjectsByUserId(userId: String): Flux<SubjectDTO>

    fun getSubjectsByTag(tag: String): Flux<SubjectDTO>

    fun getSubjectsByTags(tags: List<String>): Flux<SubjectDTO>

    fun getPaginatedSubjectsBySearch(search: String?, page: Int, size: Int): Flux<SubjectSearchDTO>

    fun createSubject(subjectDTO: SubjectDTO): Mono<SubjectDTO>

    fun updateSubject(subjectDTO: SubjectDTO): Mono<SubjectDTO>
}
