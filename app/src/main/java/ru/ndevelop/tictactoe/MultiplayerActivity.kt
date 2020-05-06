package ru.ndevelop.tictactoe

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_field.*


class MultiplayerActivity : AppCompatActivity(), View.OnClickListener,ActivityInterface {
    lateinit var ticTacToe: TicTacToeEngine
    private lateinit var squares: Array<View>
    private var roomId: Int = 0
    private var multilayerStatus = 0
    private var listeners: Array<ValueEventListener?> = arrayOfNulls(4)
    lateinit var room: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_field)

        val arguments = intent
        roomId = arguments.getIntExtra("roomId", (10000..99999).random())
        room = Utils.database.child("$roomId")
        multilayerStatus = arguments.getIntExtra("multiplayerType", 1)
        ticTacToe = TicTacToeEngine(isMultiplayer = multilayerStatus, roomId = roomId)
        ticTacToe.mySymbol = arguments.getStringExtra("mySymbol") ?: ""
        ticTacToe.values =
            savedInstanceState?.getIntArray("FIELD")?.toTypedArray()
                ?: Array(9) { 0 }
        ticTacToe.singleplayerStep= savedInstanceState?.getInt("SINGLE_STEP") ?: 0
        Log.d("DEBUG", "${ticTacToe.values[0]}");
        squares = arrayOf(rect_1, rect_2, rect_3, rect_4, rect_5, rect_6, rect_7, rect_8, rect_9)
        drawField(ticTacToe.values,squares)
        initButtons()
        if (multilayerStatus == 2) {
            setClickable(false,squares)
            tv_id.text = "${resources.getText(R.string.opponent_waiting)} ${roomId.toString()}"
            startTracking()
        }
        if (!ticTacToe.check(TYPES.UNDEFINED) ) {
            setClickable(false,squares)
            btn_retry.visibility = View.VISIBLE
           handleTextView()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        finishActivity()
    }

    override fun onClick(v: View?) {
        when (v) {
            rect_1 -> {
                ticTacToe.editField(1)
                drawField(ticTacToe.values,squares)
            }
            rect_2 -> {
                ticTacToe.editField(2)
                drawField(ticTacToe.values,squares)
            }
            rect_3 -> {
                ticTacToe.editField(3)
                drawField(ticTacToe.values,squares)
            }
            rect_4 -> {
                ticTacToe.editField(4)
                drawField(ticTacToe.values,squares)
            }
            rect_5 -> {
                ticTacToe.editField(5)
                drawField(ticTacToe.values,squares)
            }
            rect_6 -> {
                ticTacToe.editField(6)
                drawField(ticTacToe.values,squares)
            }
            rect_7 -> {
                ticTacToe.editField(7)
                drawField(ticTacToe.values,squares)
            }
            rect_8 -> {
                ticTacToe.editField(8)
                drawField(ticTacToe.values,squares)
            }
            rect_9 -> {
                ticTacToe.editField(9)
                drawField(ticTacToe.values,squares)
            }
            btn_retry -> {
                btn_retry.visibility = View.GONE
                if (multilayerStatus == 2)
                    when (Utils.isHost) {
                        true -> DatabaseHelper.writeRetryStatus(
                            0,
                            true,
                            roomId
                        )
                        false -> DatabaseHelper.writeRetryStatus(
                            1,
                            true,
                            roomId
                        )
                    }

                if (multilayerStatus == 1) retry()
            }
        }
        if (v != btn_retry) handleResult()
    }

    private fun initButtons() {
        squares.forEach { it.setOnClickListener(this) }
        btn_retry.setOnClickListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntArray("FIELD", ticTacToe.values.toIntArray())
        outState.putInt("SINGLE_STEP",ticTacToe.singleplayerStep)


    }

    private fun startTracking() {
        val result: Array<Int> = Array(9) { 0 }
        listeners[0] = room.child("field").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if(dataSnapshot.exists()) {
                    for (i in (0..8)) {
                        result[i] = (dataSnapshot.child("$i").value.toString().toInt())
                    }
                    ticTacToe.values = result
                    drawField(ticTacToe.values,squares)
                    handleResult()
                }
                else{
                    finishActivity()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("DEBUG", "Failed to read value.", error.toException())
            }
        })

        listeners[1] = room.child("retry").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.child("0").value==true && dataSnapshot.child("1").value==true) retry()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("DEBUG", "Failed to read value.", error.toException())
            }
        })

        listeners[2] = room.child("step").addValueEventListener(object : ValueEventListener {
            var step = "x"
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                step = dataSnapshot.value.toString()
                ticTacToe.step = step
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("DEBUG", "Failed to read value.", error.toException())
            }
        })
        listeners[3] = room.child("players").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (Utils.isHost) ticTacToe.mySymbol =
                    dataSnapshot.child("player1").value.toString()
                else ticTacToe.mySymbol = dataSnapshot.child("player2").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("DEBUG", "Failed to read value.", error.toException())
            }
        })

        room.child("connected").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.value == true) {
                    tv_id.visibility = View.GONE
                    setClickable(true,squares)
                    room.child("connected").removeEventListener(this)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("DEBUG", "Failed to read value.", error.toException())
            }
        })
    }

    fun handleResult() {
        if (!ticTacToe.check(TYPES.UNDEFINED)) {
            btn_retry.visibility = View.VISIBLE
            setClickable(false,squares)
        }
        handleTextView()
    }

    fun retry() {
        tv_result.text = ""
        ticTacToe.retry()
        setClickable(true,squares)
        if (multilayerStatus == 1) squares.forEach { it.setBackgroundResource(R.drawable.basic_square) }
    }


    fun finishActivity(){
        if (multilayerStatus == 2) {
            room.child("field").removeEventListener(listeners[0]!!)
            room.child("retry").removeEventListener(listeners[1]!!)
            room.child("child").removeEventListener(listeners[2]!!)
            DatabaseHelper.clearRoom(roomId)
            if(!Utils.isHost) {
                val intent = Intent(this, StartActivity::class.java)
                startActivity(intent)
            }
        }

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
