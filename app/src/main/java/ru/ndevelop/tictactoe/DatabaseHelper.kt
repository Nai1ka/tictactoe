package ru.ndevelop.tictactoe

import android.util.Log


class DatabaseHelper
 {
    companion object {

        fun writeInfo(payload: Array<Int>,roomId:Int) {
            Utils.database.child("$roomId").child("field").setValue(payload.toList())
        }
        fun addInfo(path:Int,value:Int,roomId:Int){
            Utils.database.child("$roomId").child("field").child("$path").setValue("$value")
            Log.d("DEBUG", "InfoAdded")
        }
        fun writeStep(step:String,roomId:Int){
            Utils.database.child("$roomId").child("step").setValue(step)
        }
        fun clearRoom(roomId:Int) {
            Utils.database.child("$roomId").removeValue()
        }
        fun writeRetryStatus(flag:Int,roomId:Int){
            Utils.database.child("$roomId").child("retry").setValue(flag)
        }
        fun writeConnected(status:Boolean,roomId:Int){
            Utils.database.child("$roomId").child("connected").setValue(status)
        }
    }





}