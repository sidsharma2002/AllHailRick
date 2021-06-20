package com.example.celebtalks.data.entities

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
        val uid : String = "",
        val username : String = "",
        var follows : List<String> = listOf() ,
        val description : String = "",
        @Exclude
        var isfollowing : Boolean = false
)
