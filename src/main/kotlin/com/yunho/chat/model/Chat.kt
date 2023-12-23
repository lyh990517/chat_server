package com.yunho.chat.model

import lombok.Data

@Data
data class Chat(
    val message: String,
    val from: String
)
