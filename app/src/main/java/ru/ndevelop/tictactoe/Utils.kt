package ru.ndevelop.tictactoe

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

object Utils {
    var database: DatabaseReference = Firebase.database.getReference("rooms")
    var isHost:Boolean=true
    var mySymbol=""
}