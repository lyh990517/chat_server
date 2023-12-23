package com.yunho.chat.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.server.WebSocketService
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.util.function.Predicate

@Configuration
class WebSocketConfig {

    @Bean
    fun providesWebSocketHandler() : WebSocketHandlerAdapter = WebSocketHandlerAdapter(providesWebSocketService())

    @Bean
    fun providesWebSocketService(): WebSocketService {
        val webSocketService = object : HandshakeWebSocketService() {
            override fun handleRequest(exchange: ServerWebExchange, handler: WebSocketHandler): Mono<Void> {
                val user = exchange.request.headers.getFirst("user")
                println("user : $user")
                if (user?.isEmpty() == true) {
                    exchange.response.setStatusCode(HttpStatus.UNAUTHORIZED)
                    return exchange.response.setComplete()
                }

                return exchange.session.flatMap { session ->
                    session.attributes["user"] = user
                    super.handleRequest(exchange, handler)
                }
            }
        }
        webSocketService.sessionAttributePredicate = Predicate { true }
        return webSocketService
    }
}