package com.futurecode.scarymonstercallchat.model

data class Monster(
    val name: String,
    val imageResId: Int,
    val isReady: Boolean = true,
    val soundResId: Int? = null,
    val videoCall:Int?=null
)