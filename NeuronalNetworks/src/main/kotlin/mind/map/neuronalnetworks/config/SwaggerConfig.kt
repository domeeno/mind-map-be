package mind.map.neuronalnetworks.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.hateoas.server.LinkRelationProvider
import org.springframework.hateoas.server.core.DefaultLinkRelationProvider

@Configuration
class SwaggerConfig {
    @Bean
    fun linkRelationProvider(): LinkRelationProvider {
        return DefaultLinkRelationProvider()
    }
}
