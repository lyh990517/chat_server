package com.yunho.chat.handler

import com.yunho.chat.model.Chat
import com.yunho.chat.service.ChatService
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

@Slf4j
@Component
class ChatHandler(private val chatService: ChatService) : WebSocketHandler{
    override fun handle(session: WebSocketSession): Mono<Void> {
        val user : String = session.attributes["user"].toString()
        val chatFlux = chatService.register(user)
        chatService.sendChat(user,Chat("hello $user!!","system"))
        session.receive()
            .doOnNext { webSocketMessage: WebSocketMessage ->
                val payload = webSocketMessage.payloadAsText
                val splits = payload.split(":")
                val to = splits[0]
                val message = splits[1]
                chatService.sendChat(to, Chat(message, user))
            }.subscribe()
        return session.send(chatFlux
            .map { chat: Chat -> session.textMessage(chat.from + ": " + chat.message) }
        );
    }
}