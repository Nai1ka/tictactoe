package ru.ndevelop.tictactoe


class TicTacToeEngine(
    var isMultiplayer: Boolean = false
) {
    var step = 0
    var isGameEnded = false
    var values =  Array(9) { 0 }

init {
    if (isMultiplayer && Utils.isHost) {
        DatabaseHelper.writeInfo(values)
        DatabaseHelper.writeStep("x")
        DatabaseHelper.writeRetryStatus(0)
    }
}

    fun editField(position: Int) {
//1-крестики
// 2-нолики
        if (values[position - 1] == 0 && !isGameEnded) {
            if(!isMultiplayer) {
                when (step) {
                    0 -> values[position - 1] = 1
                    1 -> values[position - 1] = 2
                }
                step = (step + 1) % 2

            }
            if (isMultiplayer ) {
                if(Utils.mySymbol=="x" && Utils.step=="x" )  {DatabaseHelper.addInfo(position-1,1);DatabaseHelper.writeStep("o");Utils.step="o" }
                if(Utils.mySymbol=="o"&& Utils.step=="o") {DatabaseHelper.addInfo(position-1,2);DatabaseHelper.writeStep("x");Utils.step="x" }

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
        }
        if ((values[0] == 2 && values[1] == 2 && values[2] == 2) or (values[3] == 2 && values[4] == 2 && values[5] == 2)
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
        Utils.mySymbol=if(Utils.mySymbol=="x") "o" else "x"
        if(Utils.isHost) {
            DatabaseHelper.writeRetryStatus(0)
            DatabaseHelper.writeInfo( Array(9) { 0 })
        }
        Utils.step=if(Utils.step=="x") "o" else "x" // TODO оптимизировать
        DatabaseHelper.writeStep(Utils.step)
        isGameEnded = false
    }
}


enum class TYPES {
    CROSS,
    ZERO,
    UNDEFINED,
    TIE

}

