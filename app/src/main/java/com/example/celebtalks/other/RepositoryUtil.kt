package com.example.celebtalks.other

// Generic method to Handle error or perform action
// Saves code for try and catch
 inline fun  <T> safeCall (action : () -> Resource<T>) : Resource<T> {
    return    try {
            action()
     } catch ( e : Exception ){
         Resource.Error( e.message ?: "An unknown error occured" )
     }
 }
