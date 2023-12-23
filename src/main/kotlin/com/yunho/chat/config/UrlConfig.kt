package com.yunho.chat.config

import com.yunho.chat.handler.ChatHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping

@Configuration
class UrlConfig(private val chatHandler: ChatHandler) {

    @Bean
    fun urlRouting(): SimpleUrlHandlerMapping {
        val urlRouter = mapOf("/chat" to chatHandler)
        val mapping = SimpleUrlHandlerMapping()
        mapping.order = 1
        mapping.urlMap = urlRouter
        return mapping
    }
}