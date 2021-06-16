package com.example.celebtalks.ui

import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

// To save code for snack bar
fun Fragment.snackbar(text : String){
    Snackbar.make(
                requireView(),
                text,
                Snackbar.LENGTH_LONG
    ).show()
}