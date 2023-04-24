package mind.map.neuronalnetworks.repository

import mind.map.neuronalnetworks.model.Subject
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface SubjectRepository : ReactiveMongoRepository<Subject, String> {
    fun findAllByUserId(userId: String): Flux<Subject>
    fun findAllByTags(tag: String): Flux<Subject>
    fun findAllByTags(tags: List<String>): Flux<Subject>

    @Query("{'subjectName': { \$regex: ?0, \$options: 'i' }}")
    fun searchByName(search: String, pageable: Pageable): Flux<Subject>

    // return all sorted by created timestamp ascending
    fun findAllByOrderByCreateTimestampDesc(): Flux<Subject>
}
