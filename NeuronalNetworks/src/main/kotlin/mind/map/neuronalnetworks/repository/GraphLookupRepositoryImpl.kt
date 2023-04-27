package mind.map.neuronalnetworks.repository

import mind.map.neuronalnetworks.model.Topic
import org.slf4j.LoggerFactory
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.GraphLookupOperation
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.time.Duration

@Repository
class GraphLookupRepositoryImpl(
    private val reactiveMongoTemplate: ReactiveMongoTemplate
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun getTopicsWithSubtopics(rooTopicId: String): Flux<Topic> {
        val criteria = Criteria("_id").`is`(rooTopicId)

        val matchStage = Aggregation.match(criteria)

        val operation = GraphLookupOperation.builder()
            .from("topics")
            .startWith("\$_id")
            .connectFrom("_id")
            .connectTo("parent_id")
            .maxDepth(16)
            .`as`("all_topics")

        val aggregation = Aggregation.newAggregation(matchStage, operation)
        return reactiveMongoTemplate.aggregate(aggregation, "topics", Topic::class.java).elementAt(0)
            .flatMapIterable { listOf(it) + it.allTopics }
    }
}
