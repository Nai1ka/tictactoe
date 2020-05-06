package ru.ndevelop.tictactoe

import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_field.*

interface ActivityInterface {
     fun drawField(field: Array<Int>,squares:Array<View>) {
        for (i in field.indices) {
            when (field[i]) {
                0 -> squares[i].setBackgroundResource(R.drawable.basic_square)
                1 -> squares[i].setBackgroundResource(R.drawable.cross_square)
                2 -> squares[i].setBackgroundResource(R.drawable.zero_square)
            }
        }
    }
    fun setClickable(status: Boolean,squares: Array<View>) {
        squares.forEach {
            it.isClickable = status
        }
    }
}