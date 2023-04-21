package mind.map.neuronalnetworks.repository

import mind.map.neuronalnetworks.model.Topic
import org.slf4j.LoggerFactory
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.time.Duration

@Repository
class GraphLookupRepositoryImpl(
    private val reactiveMongoTemplate: ReactiveMongoTemplate
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun getTopicsWithSubtopics(parentTopicId: String): Flux<Topic> {
        val aggregation = Aggregation.newAggregation(
            Aggregation.match(Criteria.where("id").`is`(parentTopicId)),
            Aggregation.graphLookup("topics")
                .startWith("\$id")
                .connectFrom("id")
                .connectTo("parent_id")
                .`as`("all_topics")
        )

        return reactiveMongoTemplate.aggregate(aggregation, "topics", Topic::class.java)
            .flatMapIterable { it.allTopics }
            .delayElements(Duration.ofMillis(500))
            .doOnNext { topic -> log.info("Topic found: {}", topic) }
    }
}
