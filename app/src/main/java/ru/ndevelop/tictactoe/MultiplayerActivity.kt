package ru.ndevelop.tictactoe

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_field.*


class MultiplayerActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var TicTacToe: TicTacToeEngine
    lateinit var squares: Array<View>
    val room: DatabaseReference =  Utils.database.child("${Utils.roomId}")
    lateinit var listener:ValueEventListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_field)


        TicTacToe = TicTacToeEngine(isMultiplayer = true)
        TicTacToe.values =
            savedInstanceState?.getIntArray("FIELD")?.toTypedArray()
                ?: Array(9) { 0 }
        squares = arrayOf(rect_1, rect_2, rect_3, rect_4, rect_5, rect_6, rect_7, rect_8, rect_9)
        initButtons()
        startTracking()
        if (TicTacToe.check() != TYPES.UNDEFINED) {
            squares.forEach {
                it.isClickable = false
                btn_retry.visibility = View.VISIBLE
            }
            when (TicTacToe.check()) {
                TYPES.CROSS -> tv_result.text = "Выиграли крестики!"
                TYPES.ZERO -> tv_result.text = "Выиграли нолики!"
                TYPES.TIE -> tv_result.text = "Ничья"
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        DatabaseHelper.clearRoom()
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            rect_1.id -> {
                TicTacToe.editField(1)
                drawField(TicTacToe.values)
            }
            rect_2.id -> {
                TicTacToe.editField(2)
                drawField(TicTacToe.values)
            }
            rect_3.id -> {
                TicTacToe.editField(3)
                drawField(TicTacToe.values)
            }
            rect_4.id -> {
                TicTacToe.editField(4)
                drawField(TicTacToe.values)
            }
            rect_5.id -> {
                TicTacToe.editField(5)
                drawField(TicTacToe.values)
            }
            rect_6.id -> {
                TicTacToe.editField(6)
                drawField(TicTacToe.values)
            }
            rect_7.id -> {
                TicTacToe.editField(7)
                drawField(TicTacToe.values)
            }
            rect_8.id -> {
                TicTacToe.editField(8)
                drawField(TicTacToe.values)
            }
            rect_9.id -> {
                TicTacToe.editField(9)
                drawField(TicTacToe.values)
            }
            btn_retry.id -> {
                Utils.retryStatus += 1;DatabaseHelper.writeRetryStatus(Utils.retryStatus)
            }


        }
        handleResult()


    }

    private fun initButtons() {
        squares.forEach { it.setOnClickListener(this) }
        btn_retry.setOnClickListener(this)
    }

    private fun drawField(field: Array<Int>) {

        for (i in field.indices) {
            when (field[i]) {
                0 -> squares[i].setBackgroundResource(R.drawable.basic_square)
                1 -> squares[i].setBackgroundResource(R.drawable.cross_square)
                2 -> squares[i].setBackgroundResource(R.drawable.zero_square)
            }


        }

    }

    fun startTracking() {
        val result: Array<Int> = Array(9) { 0 }
        room.child("field").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (i in (0..8)) {
                    result[i] = (dataSnapshot.child("$i").value.toString().toInt())
                }
                TicTacToe.values = result
                drawField(TicTacToe.values)
                handleResult()
                Log.d("DEBUG", "field")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("DEBUG", "Failed to read value.", error.toException())

            }
        })


        room.child("retry").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                //Utils.step = dataSnapshot.child("step").value.toString()
                if (dataSnapshot.value != null) Utils.retryStatus =
                    dataSnapshot.value.toString().toInt()

                if (Utils.retryStatus >= 2) retry()
                Log.d("DEBUG", "retry")

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("DEBUG", "Failed to read value.", error.toException())

            }
        })


        room.child("step").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                Utils.step = dataSnapshot.value.toString()

                Log.d("DEBUG", Utils.step)

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("DEBUG", "Failed to read value.", error.toException())

            }
        })

    }

    override fun onBackPressed() {
        super.onBackPressed()
        //Utils.database.child("${Utils.roomId}").removeEventListener(room.lis)

    }

    fun handleResult() {
        if (TicTacToe.check() != TYPES.UNDEFINED) {
            squares.forEach {
                it.isClickable = false
                btn_retry.visibility = View.VISIBLE
            }
        }
        when (TicTacToe.check()) {
            TYPES.CROSS -> tv_result.text = "Выиграли крестики!"
            TYPES.ZERO -> tv_result.text = "Выиграли нолики!"
            TYPES.TIE -> tv_result.text = "Ничья"
            TYPES.UNDEFINED -> tv_result.text = ""
        }

    }

    fun retry() {

        btn_retry.visibility = View.GONE
        TicTacToe.retry()
        squares.forEach {
            it.isClickable = true
        }

    }


}
