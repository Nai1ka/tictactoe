package ru.ndevelop.tictactoe

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

object Utils {
   // var values: Array<Int> =  Array(9) { 0 }
    var roomId: Int = (1000..99999).random()
    var database: DatabaseReference = Firebase.database.getReference("rooms")
    var isHost:Boolean=true
    var step= "x"
    var retryStatus=0
    var mySymbol=""
}