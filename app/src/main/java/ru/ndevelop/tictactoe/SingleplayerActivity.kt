package ru.ndevelop.tictactoe

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_field.*

class SingleplayerActivity : AppCompatActivity(), View.OnClickListener,ActivityInterface {
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
        drawField(ticTacToe.values,squares)
        initButtons()
        if (!ticTacToe.check(TYPES.UNDEFINED)) {
            squares.forEach {
                it.isClickable = false
                btn_retry.visibility = View.VISIBLE
            }
           handleTextView()
        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            rect_1.id -> {
                ticTacToe.editField(1)
               drawField(ticTacToe.values,squares)
            }
            rect_2.id -> {
                ticTacToe.editField(2)
                drawField(ticTacToe.values,squares)
            }
            rect_3.id -> {
                ticTacToe.editField(3)
                drawField(ticTacToe.values,squares)
            }
            rect_4.id -> {
                ticTacToe.editField(4)
                drawField(ticTacToe.values,squares)
            }
            rect_5.id -> {
                ticTacToe.editField(5)
                drawField(ticTacToe.values,squares)
            }
            rect_6.id -> {
                ticTacToe.editField(6)
                drawField(ticTacToe.values,squares)
            }
            rect_7.id -> {
                ticTacToe.editField(7)
                drawField(ticTacToe.values,squares)
            }
            rect_8.id -> {
                ticTacToe.editField(8)
                drawField(ticTacToe.values,squares)
            }
            rect_9.id -> {
                ticTacToe.editField(9)
                drawField(ticTacToe.values,squares)
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


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntArray("FIELD", ticTacToe.values.toIntArray())


    }
    private fun handleResult() {
        if (!ticTacToe.check(TYPES.UNDEFINED)) {
            squares.forEach {
                it.isClickable = false
                btn_retry.visibility = View.VISIBLE
            }
        }
       handleTextView()

    }
    private fun handleTextView(){
        when {
            ticTacToe.check(TYPES.CROSS) -> tv_result.text = "Выиграли крестики!"
            ticTacToe.check(TYPES.ZERO) -> tv_result.text = "Выиграли нолики!"
            ticTacToe.check(TYPES.TIE) -> tv_result.text = "Ничья"
            ticTacToe.check(TYPES.UNDEFINED) -> tv_result.text = ""
        }

    }
}

