package ru.ndevelop.tictactoe


class TicTacToeEngine(
    var isMultiplayer: Int = 0, //0-одиночка //1-на одном устрйостве //2 - на 2 устрйоствах
    var roomId: Int = 0
) {
    private var singleplayerStep = 0
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
        } else {
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

    fun check(): TYPES {
        var winner: TYPES = TYPES.UNDEFINED
        if ((values[0] == 1 && values[1] == 1 && values[2] == 1) or (values[3] == 1 && values[4] == 1 && values[5] == 1)
            or (values[6] == 1 && values[7] == 1 && values[8] == 1) or
            (values[0] == 1 && values[3] == 1 && values[6] == 1) or (values[1] == 1 && values[4] == 1 && values[7] == 1)
            or (values[2] == 1 && values[5] == 1 && values[8] == 1) or (values[0] == 1 && values[4] == 1 && values[8] == 1) or
            (values[2] == 1 && values[4] == 1 && values[6] == 1)
        ) {
            winner = TYPES.CROSS
            isGameEnded = true
        } else if ((values[0] == 2 && values[1] == 2 && values[2] == 2) or (values[3] == 2 && values[4] == 2 && values[5] == 2)
            or (values[6] == 2 && values[7] == 2 && values[8] == 2) or
            (values[0] == 2 && values[3] == 2 && values[6] == 2) or (values[1] == 2 && values[4] == 2 && values[7] == 2)
            or (values[2] == 2 && values[5] == 2 && values[8] == 2) or (values[0] == 2 && values[4] == 2 && values[8] == 2) or
            (values[2] == 2 && values[4] == 2 && values[6] == 2)
        ) {
            winner = TYPES.ZERO
            isGameEnded = true
        } else if (values[0] != 0 && values[1] != 0 && values[2] != 0 && values[3] != 0 && values[4] != 0
            && values[5] != 0 && values[6] != 0 && values[7] != 0 && values[8] != 0
        ) {
            winner = TYPES.TIE
            isGameEnded = true
        }
        return winner

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
}


enum class TYPES {
    CROSS,
    ZERO,
    UNDEFINED,
    TIE;


}

