package com.example.calculator.calculator

import android.util.Log
import java.util.*

class Calculator {
    var infix: List<String> = emptyList()

    var postfix: List<String> = emptyList()

    var stack: Stack<Double> = Stack()

    fun cal(list: List<String>): String {

        if (list.isEmpty()) {
            return "0"
        } else if (list[0] == "") {
            return "0"
        }else {
            try {
                infix = list
                val output = infixToPostfix()
                postfix = infixToPostfix()

                Log.d("!!!", output.toString())

                calculate()

                return if (stack.peek()%1.0 == 0.0) {
                    stack.peek().toInt().toString()
                } else stack.peek().toString()
            } catch (e: Exception) {
                Log.d("!!! catch", "cal: $e")

                Log.d("!!! stack", stack.toString())

                Log.d("!!! infix", infix.toString())

                Log.d("!!! postfix", postfix.toString())

                return "no value"
            }
        }
    }

    private fun calculate() {
        for (it in postfix) {
            assort(it)
        }

//        Log.d("!!!", "calculate: ${stack.peek()}")
        Log.d("!!!", "calculate: ${stack.isEmpty()}")
    }

    private fun assort(string: String) {

        Log.d("!!! stack", "assort: $stack")

        var numLeft = 0.0
        var numRight = 0.0

        fun num() {
            numRight = stack.pop()
            numLeft = stack.pop()
        }

        if (string.toDoubleOrNull() != null) {
            stack.push(string.toDouble())
        } else {
            num()
            when (string) {
                "+" -> {
                    stack.push(
                        numLeft + numRight
                    )
                }
                "-" -> {
                    stack.push(
                        numLeft - numRight
                    )
                }
                "×" -> {
                    stack.push(
                        numLeft * numRight
                    )
                }
                "÷" -> {
                    stack.push(
                        numLeft / numRight
                    )
                }
                else -> error(string)
            }

        }

        Log.d("!!! stack", "assort: $stack")
    }

    private fun infixToPostfix(): List<String> {
        val stack: Stack<String> = Stack()
        val postfix: MutableList<String> = mutableListOf()

        fun output(string: String) {
            postfix.add(string)
        }

        fun operatorCheck(string: String) {
            if (stack.size != 0) {
                if (stack.peek() == "×" || stack.peek() == "÷") {
                    postfix.add(stack.pop())
                    stack.push(string)
                } else {
                    stack.push(string)
                }
            } else {
                stack.push(string)
            }
        }

        fun outputUntilLeft() {
            while (stack.peek() != "(") {
                postfix.add(stack.pop())
            }
            stack.pop()
        }

        fun assort(string: String) {
            if (string.toDoubleOrNull() != null) {
                output(string)
            } else {
                when (string) {
                    "+" -> {
                        operatorCheck(string)
                    }
                    "-" -> {
                        operatorCheck(string)
                    }
                    "×" -> {
                        stack.push(string)
                    }
                    "÷" -> {
                        stack.push(string)
                    }
                    "(" -> {
                        stack.push(string)
                    }
                    ")" -> {
                        outputUntilLeft()
                    }
                    else -> error(string)
                }
            }
        }

        for (it in infix) {
            assort(it)
        }

        while (stack.isNotEmpty()) {
            postfix.add(stack.pop())
        }

        return postfix
    }

    private fun error(string: String) {
        Log.d("!!! error", "error string: $string")
    }
}