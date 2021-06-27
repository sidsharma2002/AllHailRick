package com.example.celebtalks.other

import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query

// Generic method to Handle error or perform action
// Saves code for try and catch
 inline fun  <T> safeCall (action : () -> Resource<T>) : Resource<T> {
    return  try {
            action()
    }  catch ( e : Exception ){
         Resource.Error( e.localizedMessage ?: "An unknown error occured" )
     }
 }
