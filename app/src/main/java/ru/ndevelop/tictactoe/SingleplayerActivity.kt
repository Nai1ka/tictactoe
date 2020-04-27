package ru.ndevelop.tictactoe

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_field.*

class SingleplayerActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var ticTacToe: TicTacToeEngine
    private lateinit var squares: Array<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_field)

        ticTacToe = TicTacToeEngine(isMultiplayer = 0)
        ticTacToe.values =
            savedInstanceState?.getIntArray("FIELD")?.toTypedArray()
                ?: Array(9) { 0 }
        squares = arrayOf(rect_1, rect_2, rect_3, rect_4, rect_5, rect_6, rect_7, rect_8, rect_9)
        drawField(ticTacToe.values)
        initButtons()
        if (ticTacToe.check() != TYPES.UNDEFINED) {
            squares.forEach {
                it.isClickable = false
                btn_retry.visibility = View.VISIBLE
            }
            when (ticTacToe.check()) {
                TYPES.CROSS -> tv_result.text = "Выиграли крестики!"
                TYPES.ZERO -> tv_result.text = "Выиграли нолики!"
                TYPES.TIE -> tv_result.text = "Ничья"
                TYPES.UNDEFINED -> tv_result.text=""
            }
        }

    }


    override fun onClick(v: View?) {
        when (v?.id) {
            rect_1.id -> {
                ticTacToe.editField(1)
               drawField(ticTacToe.values)
            }
            rect_2.id -> {
                ticTacToe.editField(2)
                drawField(ticTacToe.values)
            }
            rect_3.id -> {
                ticTacToe.editField(3)
                drawField(ticTacToe.values)
            }
            rect_4.id -> {
                ticTacToe.editField(4)
                drawField(ticTacToe.values)
            }
            rect_5.id -> {
                ticTacToe.editField(5)
                drawField(ticTacToe.values)
            }
            rect_6.id -> {
                ticTacToe.editField(6)
                drawField(ticTacToe.values)
            }
            rect_7.id -> {
                ticTacToe.editField(7)
                drawField(ticTacToe.values)
            }
            rect_8.id -> {
                ticTacToe.editField(8)
                drawField(ticTacToe.values)
            }
            rect_9.id -> {
                ticTacToe.editField(9)
                drawField(ticTacToe.values)
            }
            btn_retry.id -> {
                btn_retry.visibility = View.GONE
                ticTacToe.retry()
                squares.forEach {
                    it.setBackgroundResource(R.drawable.basic_square)
                    it.isClickable = true
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
        outState.putIntArray("FIELD", ticTacToe.values.toIntArray())


    }
    private fun handleResult() {
        if (ticTacToe.check() != TYPES.UNDEFINED) {
            squares.forEach {
                it.isClickable = false
                btn_retry.visibility = View.VISIBLE
            }
        }
        when (ticTacToe.check()) {
            TYPES.CROSS -> tv_result.text ="Выиграли крестики!"
            TYPES.ZERO -> tv_result.text ="Выиграли нолики!"
            TYPES.TIE -> tv_result.text ="Ничья"
            TYPES.UNDEFINED -> tv_result.text =""
        }

    }
}

