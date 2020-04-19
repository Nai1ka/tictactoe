package ru.ndevelop.tictactoe

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_field.*

class SingleplayerActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var TicTacToe: TicTacToeEngine
    lateinit var squares: Array<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_field)

        TicTacToe = TicTacToeEngine()
        TicTacToe.values =
            savedInstanceState?.getIntArray("FIELD")?.toTypedArray()
                ?: Array(9) { 0 }
        squares = arrayOf(rect_1, rect_2, rect_3, rect_4, rect_5, rect_6, rect_7, rect_8, rect_9)
        drawField(TicTacToe.values)
        initButtons()
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
                btn_retry.visibility = View.GONE
                TicTacToe.retry()
                squares.forEach {
                    it.setBackgroundResource(R.drawable.basic_square)
                    squares.forEach { it.isClickable = true }
                }
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
                1 -> squares[i].setBackgroundResource(R.drawable.cross_square)
                2 -> squares[i].setBackgroundResource(R.drawable.zero_square)
            }


        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntArray("FIELD", TicTacToe.values.toIntArray())


    }
    fun handleResult() {
        if (TicTacToe.check() != TYPES.UNDEFINED) {
            squares.forEach {
                it.isClickable = false
                btn_retry.visibility = View.VISIBLE
            }
        }
        when (TicTacToe.check()) {
            TYPES.CROSS -> tv_result.text ="Выиграли крестики!"
            TYPES.ZERO -> tv_result.text ="Выиграли нолики!"
            TYPES.TIE -> tv_result.text ="Ничья"
            TYPES.UNDEFINED -> tv_result.text =""
        }

    }
}

