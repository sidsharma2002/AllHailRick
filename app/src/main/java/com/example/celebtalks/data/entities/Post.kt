package com.example.celebtalks.data.entities

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Post (
    val  id : String = "",
    val authorUid : String = "",
    @get:Exclude var authorUsername : String = "",
    val heading : String = "",
    val body : String = "",
    val date : String = "",
    @get:Exclude var isLiked : Boolean = false,
    @get:Exclude var isLiking : Boolean = false,
    var likedBy: List<String> = listOf()
)
