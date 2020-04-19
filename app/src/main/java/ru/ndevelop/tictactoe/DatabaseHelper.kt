package ru.ndevelop.tictactoe

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.reflect.typeOf


class DatabaseHelper
 {
    companion object {

        fun writeInfo(payload: Array<Int>,retry:Boolean=false) {
            Utils.database.child("${Utils.roomId}").child("field").setValue(payload.toList())
        }
        fun addInfo(path:Int,value:Int){
            Utils.database.child("${Utils.roomId}").child("field").child("$path").setValue("$value")
            Log.d("DEBUG", "InfoAdded");
        }
        fun writeStep(step:String){
            Utils.database.child("${Utils.roomId}").child("step").setValue(step)
        }
        fun clearRoom() {
            Utils.database.child("${Utils.roomId}").removeValue()
        }
        fun writeRetryStatus(flag:Int){
            Utils.database.child("${Utils.roomId}").child("retry").setValue(flag)
        }
        //fun writeFirstStep(player:Boolean){
          //  Utils.database.child("${Utils.roomId}").child("winner").setValue(player)
       // }
    }





}