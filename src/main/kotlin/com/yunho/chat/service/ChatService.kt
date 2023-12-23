package com.yunho.chat.service

import com.yunho.chat.model.Chat
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import reactor.core.publisher.Sinks.Many
import java.util.concurrent.ConcurrentHashMap

@Service
class ChatService {
    val chatSinkMap = ConcurrentHashMap<String, Many<Chat>>()

    fun register(user: String): Flux<Chat> {
        val sink = Sinks.many().unicast().onBackpressureBuffer<Chat>()
        chatSinkMap[user] = sink
        return sink.asFlux()
    }

    fun sendChat(user: String, chat: Chat) : Boolean{
        chatSinkMap[user] ?: return false
        chatSinkMap[user]?.tryEmitNext(chat)
        return true
    }
}