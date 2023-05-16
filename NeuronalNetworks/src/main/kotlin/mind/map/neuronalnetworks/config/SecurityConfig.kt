package mind.map.neuronalnetworks.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.reactive.config.CorsRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer
import java.nio.file.Files
import java.nio.file.Paths
import java.security.KeyFactory
import java.security.interfaces.RSAPublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

@Configuration
@EnableWebFluxSecurity
class SecurityConfig : WebFluxConfigurer {

    @Value("\${app.security.disable:false}")
    private val disableSecurity: Boolean = false

    @Value("\${app.security.public-key}")
    private val publicKey: String = ""

    override fun addCorsMappings(corsRegistry: CorsRegistry) {
        corsRegistry.addMapping("/**")
            .allowedOrigins("*")
            .allowedHeaders("*")
            .allowedMethods("*")
    }

    @Bean
    fun jwtDecoder(): ReactiveJwtDecoder {
        val publicKey = Files.readString(Paths.get("").resolve(publicKey).normalize().toAbsolutePath())
        val keyBytes = Base64.getDecoder().decode(publicKey)

        val keySpec = X509EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance("RSA")

        val keySource = keyFactory.generatePublic(keySpec) as RSAPublicKey

        return NimbusReactiveJwtDecoder(keySource)
    }

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity, jwtDecoder: ReactiveJwtDecoder): SecurityWebFilterChain {
        return if (disableSecurity) {
            http
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers("/api/**", "/**").permitAll()
                .and()
                .build()
        } else {
            return http
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers("/api/**").authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt()
                .jwtDecoder(jwtDecoder)
                .and()
                .and()
                .build()
        }
    }
}
