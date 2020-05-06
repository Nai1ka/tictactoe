package ru.ndevelop.tictactoe

import android.util.Log


class TicTacToeEngine(
    var isMultiplayer: Int = 0, //0-одиночка //1-на одном устрйостве //2 - на 2 устрйоствах
    var roomId: Int = 0
) {
     var singleplayerStep = 0
    var step = ""
    var mySymbol = ""
    private var isGameEnded = false
    var values = Array(9) { 0 }

    init {
        if (isMultiplayer == 2 && Utils.isHost) {
            DatabaseHelper.writeInfo(values, roomId)
            DatabaseHelper.writeStep("x", roomId)
            DatabaseHelper.writeRetryStatus(0, false, roomId)
            Utils.database.child("$roomId").child("retry").setValue(List(2) { false })
            DatabaseHelper.writePlayerStep(
                "x",
                1,
                roomId
            )
        } else if(isMultiplayer==2 && !Utils.isHost) {
            DatabaseHelper.writeConnected(true, roomId); DatabaseHelper.writePlayerStep(
                "o",
                2,
                roomId
            )

        }
    }

    fun editField(position: Int) {
//1-крестики
// 2-нолики
        if (values[position - 1] == 0 && !isGameEnded) {
            if (isMultiplayer == 0) {
                values[position - 1] = 1
                if(check(TYPES.UNDEFINED,values)) values[makeStep(values)]=2

            }
            if (isMultiplayer == 1) {
                when (singleplayerStep) {
                    0 -> values[position - 1] = 1
                    1 -> values[position - 1] = 2
                }
                singleplayerStep = (singleplayerStep + 1) % 2

            }
            if (isMultiplayer == 2) {
                if (mySymbol == "x" && step == "x") {
                    DatabaseHelper.addInfo(position - 1, 1, roomId);DatabaseHelper.writeStep(
                        "o",
                        roomId
                    );step = "o"
                }
                if (mySymbol == "o" && step == "o") {
                    DatabaseHelper.addInfo(position - 1, 2, roomId);DatabaseHelper.writeStep(
                        "x",
                        roomId
                    );step = "x"
                }
            }
        }
    }

    fun check(type: TYPES, field: Array<Int> = values): Boolean {
        val player = type.value
        var status = false
        if (type == TYPES.CROSS || type == TYPES.ZERO) {

            if ((field[0] == player && field[1] == player && field[2] == player) or (field[3] == player && field[4] == player && field[5] == player)
                or (field[6] == player && field[7] == player && field[8] == player) or
                (field[0] == player && field[3] == player && field[6] == player) or (field[1] == player && field[4] == player && field[7] == player)
                or (field[2] == player && field[5] == player && field[8] == player) or (field[0] == player && field[4] == player && field[8] == player) or
                (field[2] == player && field[4] == player && field[6] == player)
            ) {
                status = true
                isGameEnded = true
            }
        } else if (type == TYPES.TIE) {

            if (field[0] != 0 && field[1] != 0 && field[2] != 0 && field[3] != 0 && field[4] != 0
                && field[5] != 0 && field[6] != 0 && field[7] != 0 && field[8] != 0
            ) {
                status = true
                isGameEnded = true
            }

        } else if (type == TYPES.UNDEFINED) {
            if (!check(TYPES.CROSS) && !check(TYPES.TIE) && !check(TYPES.ZERO)) status = true
        }
        return status
    }

    fun retry() {
        values = Array(9) { 0 }
        mySymbol = if (mySymbol == "x") "o" else "x"
        if (isMultiplayer == 1) singleplayerStep = 0

        if (Utils.isHost && isMultiplayer == 2) {
            DatabaseHelper.writePlayerStep(mySymbol, 1, roomId)
            step = "x"
            DatabaseHelper.writeStep(step, roomId)
            DatabaseHelper.writeRetryStatus(0, false, roomId)
            DatabaseHelper.writeInfo(Array(9) { 0 }, roomId)
        } else {
            DatabaseHelper.writePlayerStep(mySymbol, 2, roomId)
            DatabaseHelper.writeRetryStatus(1, false, roomId)
        }
        isGameEnded = false

    }

    private fun makeStep(field: Array<Int>): Int {

        field.forEachIndexed { index, element ->
            if (element == 0) {
                field[index] = 2
                if (check(TYPES.ZERO, field)) {
                    Log.d("DEBUG", "$index")
                    return index
                }
                else field[index]=0
            }
        }

        for (i in field.indices) {
            val randInt = (0..8).random()
            if (field[randInt] == 0) {
                Log.d("DEBUG", "$randInt")
                return randInt
            }

        }
        return 0

    }
}

enum class TYPES(val value: Int) {
    CROSS(1),
    ZERO(2),
    TIE(3),
    UNDEFINED(0);


}

