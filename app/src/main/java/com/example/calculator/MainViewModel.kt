package com.example.calculator

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlin.math.log

class MainViewModel : ViewModel() {

    //    val inputString: MutableState<String> = mutableStateOf("0")
    val infix = mutableStateListOf<String>()

    // 當前狀態
    val currentState: MutableState<CurrentState> = mutableStateOf(CurrentState.NULL)

    // 括號狀態
    val parenthesesState = mutableStateOf(0)

    private fun inputReplace(string: String) {
        infix.removeLast()
        infix.add(string)
    }

    private fun inputNew(string: String) {
        infix.add(string)
    }

    private fun inputAppend(string: String) {
        infix.add(infix.removeLast() + string)
    }


    fun numInput(inputNum: String) {
        when (currentState.value) {
            // null：直接儲存輸入
            CurrentState.NULL -> {
                when (inputNum) {
                    "0", "00" -> {
                        infix.add("0")
                        currentState.value = CurrentState.ZERO
                    }
                    "." -> {
                        infix.add("0.")
                        currentState.value = CurrentState.DOUBLE
                    }
                    "-" -> {
                        infix.add(inputNum)
                        currentState.value = CurrentState.NEGATIVE
                    }
                    else -> {
                        infix.add(inputNum)
                        currentState.value = CurrentState.INTEGER
                    }
                }
            }

            // input:Num state:Zero
            CurrentState.ZERO -> {
                when (inputNum) {
                    "." -> {
                        inputAppend(".")
                        currentState.value = CurrentState.DOUBLE
                    }
                    "0", "00" -> {}
                    "-" -> {
                        inputReplace(inputNum)
                        currentState.value = CurrentState.NEGATIVE
                    }
                    else -> {
                        inputReplace(inputNum)
                        currentState.value = CurrentState.INTEGER
                    }
                }
//                if (inputNum != "0") {
//                    val lastNum = infix.removeLast() + inputNum
//                    infix.add(lastNum)
//
//                    if (inputNum == ".") {
//                        currentState.value = CurrentState.DOUBLE
//                    } else {
//                        currentState.value = CurrentState.INTEGER
//                    }
//                }
            }

            // input:Num  state:Int
            CurrentState.INTEGER -> {
                if (inputNum == ".") {
                    inputAppend(inputNum)
                    currentState.value = CurrentState.DOUBLE
                } else {
                    inputAppend(inputNum)
                }
            }

            // input:Num  state:Double
            CurrentState.DOUBLE -> {
                inputAppend(inputNum)
            }

            // input:Num  state:Double
            CurrentState.NEGATIVE -> {
                when (inputNum) {
                    "0", "00" -> {
                        inputAppend("0")
                        currentState.value = CurrentState.ZERO
                    }
                    "." -> {
                        inputAppend("0.")
                        currentState.value = CurrentState.DOUBLE
                    }
                    else -> {
                        inputAppend(inputNum)
                        currentState.value = CurrentState.INTEGER
                    }
                }
            }

            // input:Num  state:Operator
            CurrentState.OPERATOR -> {
                when (inputNum) {
                    "0", "00" -> {
                        inputNew("0")
                        currentState.value = CurrentState.ZERO
                    }
                    "." -> {
                        inputNew("0.")
                        currentState.value = CurrentState.DOUBLE
                    }
                    "-" -> {
                        inputNew(inputNum)
                        currentState.value = CurrentState.NEGATIVE
                    }
                    else -> {
                        inputNew(inputNum)
                        currentState.value = CurrentState.INTEGER
                    }
                }
            }

            // input:Num  state:(
            CurrentState.LEFT -> {
                when (inputNum) {
                    "0", "00" -> {
                        infix.add("0")
                        currentState.value = CurrentState.ZERO
                    }
                    "." -> {
                        infix.add("0.")
                        currentState.value = CurrentState.DOUBLE
                    }
                    "-" -> {
                        infix.add(inputNum)
                        currentState.value = CurrentState.NEGATIVE
                    }
                    else -> {
                        infix.add(inputNum)
                        currentState.value = CurrentState.INTEGER
                    }
                }
            }

            // input:Num state:)
            CurrentState.RIGHT -> {
                when (inputNum) {
                    "0", "00" -> {

                    }
                }
            }
        }


    }

    fun operatorInput(inputOperator: String) {
        when (currentState.value) {
            // input:Operator
            CurrentState.NULL -> {
                if (inputOperator == "(") inputNew(inputOperator)
                parenthesesState.value += 1
                currentState.value = CurrentState.LEFT
            }

            // input:Operator
            CurrentState.INTEGER, CurrentState.DOUBLE, CurrentState.ZERO -> {
                if (inputOperator == ")") {
                    inputNew(inputOperator)
                    parenthesesState.value -= 1
                    currentState.value = CurrentState.RIGHT
                } else {
                    inputNew(inputOperator)
                    currentState.value = CurrentState.OPERATOR
                }
            }

            // input:Operator
            CurrentState.RIGHT -> {
                inputNew(inputOperator)
                if (inputOperator == ")") {
                    currentState.value = CurrentState.RIGHT
                }
                currentState.value = CurrentState.OPERATOR
            }

            CurrentState.OPERATOR -> {
                if (inputOperator == "(") {
                    inputNew(inputOperator)
                    parenthesesState.value += 1
                    currentState.value = CurrentState.LEFT
                }
            }

            else -> {
                Log.d("!!! error", "operatorInput: $inputOperator")
            }
        }
    }

    private fun insert() {
    }

    private fun infixIsNotEmpty(): Boolean {
        return if (infix.isEmpty()) {
            currentState.value = CurrentState.NULL
            Log.d("!!! ", "infixIsNotEmpty: ${infix.isEmpty()}")
            false
        } else true
    }


    /** UI Function  */

    fun btBackSpace() {
        if (infix.last().isNotEmpty()) {

            when (currentState.value) {
                CurrentState.NULL -> {}

                CurrentState.ZERO -> {
                    infix.dropLast(1)
                    if (infix.isNotEmpty()) {
                        when (infix.last()) {
                            "(" -> currentState.value = CurrentState.LEFT
                            else -> {

                                currentState.value = CurrentState.OPERATOR
                            }
                        }
                    } else {
                        currentState.value = CurrentState.NULL
                    }
                }

                CurrentState.INTEGER -> {
                    infix.add(infix.removeLast().dropLast(1))
//                    if (infix.last() == "") infix.dropLast(1)

//                    if (infixIsNotEmpty())

//                    Log.d("!!!", "btBackSpace: ${infix.isEmpty()}")
                    if (infix.last() == "") {
                        infix.removeLast()
//                        Log.d("!!!", "btBackSpace: ${infix.isEmpty()}")
                        if (infixIsNotEmpty()) {
                            when (infix.last()) {
                                "(" -> currentState.value = CurrentState.LEFT
                                else -> currentState.value = CurrentState.OPERATOR
                            }
                        }
                    }
                }

                CurrentState.DOUBLE -> {
                    val deletedStr = infix.last().takeLast(1)
                    infix.add(infix.removeLast().dropLast(1))

                    if (deletedStr == ".") {
                        when (infix.last()) {
                            "0", "-0" -> currentState.value = CurrentState.ZERO
                            else -> currentState.value = CurrentState.INTEGER
                        }
                    }
                }

                CurrentState.NEGATIVE -> {
                    infix.dropLast(1)

                    if (infixIsNotEmpty()) {
                        if (infix.last() == "(") CurrentState.LEFT
                        else currentState.value = CurrentState.OPERATOR
                    }
                }

                CurrentState.OPERATOR -> {
                    infix.dropLast(1)

                    val previous = infix.last()
                    currentState.value =
                        if (previous == ")") CurrentState.RIGHT
                        else if (previous == "0") CurrentState.ZERO
                        else if (previous.toIntOrNull() is Int) CurrentState.INTEGER
                        else if (previous.toDoubleOrNull() is Double) CurrentState.DOUBLE
                        else {
                            Log.d("!!! error", "btBackSpace: $previous")
                            CurrentState.DOUBLE
                        }
                }

                CurrentState.LEFT -> {
                    infix.dropLast(1)

                    val previous = infix.last()
                    currentState.value =
                        if (previous == "(") CurrentState.LEFT
                        else CurrentState.OPERATOR

                }

                CurrentState.RIGHT -> {
                    infix.dropLast(1)

                    val previous = infix.last()
                    currentState.value =
                        if (previous == ")") CurrentState.RIGHT
                        else if (previous == "0") CurrentState.ZERO
                        else if (previous.toIntOrNull() is Int) CurrentState.INTEGER
                        else if (previous.toDoubleOrNull() is Double) CurrentState.DOUBLE
                        else {
                            Log.d("!!! error", "btBackSpace: $previous")
                            CurrentState.DOUBLE
                        }
                }
            }
        }
    }

    fun btAC() {
        infix.clear()
        currentState.value = CurrentState.NULL
    }

    fun leftParentheses() {

    }

    fun rightParentheses() {

    }
}

// 決定可輸入的運算源和運算子
enum class CurrentState {
    NULL, ZERO, INTEGER, DOUBLE, NEGATIVE, OPERATOR, LEFT, RIGHT
}

