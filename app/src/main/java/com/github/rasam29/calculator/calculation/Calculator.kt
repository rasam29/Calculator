package com.github.rasam29.calculator.calculation

import androidx.appcompat.widget.AppCompatButton
import java.util.*

class Calculator(private val callBack: OnExpressionEntered) {


    var argument: String = ""

    fun calculate(): String {
        return try {
            val reversePolishArgument: String? = argument.toReversePolish()
            val result: String = reversePolishArgument!!.read()
            result
        } catch (e: ArithmeticException) {
            e.printStackTrace()
            "Invalid Input"
        }

    }

    fun setNumbersButton(buttonList: MutableList<AppCompatButton>) {
        for (item in buttonList) {
            item.setOnClickListener {
                argument += item.text.toString()
                callBack.onEntered(argument)
            }
        }
    }

    fun clear() {
        argument = ""
    }

    /*
        This function Turn a Post Fix To A calculated Result
     */
    private fun String.read(): String {
        if (isNullOrBlank()) return ""
        var tempNumber = ""
        val tempStack: Stack<String> = Stack()
        try {
            for (i in 0 until length) {
                if (this[i] == ' ') {
                    if (tempNumber.isNotBlank()){
                        tempStack.push(tempNumber)
                    }
                    tempNumber =""
                    continue
                } else if (this[i].isDigit()) {
                    tempNumber += this[i]
                } else if (this[i].isFunction()) {
                    tempNumber =""
                    val secondNumber = tempStack.pop().toInt()
                    val firstNumber = tempStack.pop().toInt()
                    val tempResult = when (this[i]) {
                        '/' -> firstNumber / secondNumber
                        '+' -> firstNumber + secondNumber
                        '*' -> firstNumber * secondNumber
                        '-' -> firstNumber - secondNumber
                        else -> {
                            throw IllegalArgumentException("Invalid Operand")
                        }
                    }
                    tempStack.push(tempResult.toString())

                } else throw IllegalArgumentException("InValid input")
            }
            return tempStack.pop()
        } catch (e: Exception) {
            e.printStackTrace()
            return "Invalid Input"
        }

    }

}
/*
    For example 3 + 2 *4
    this function makes it Reverse polish (postfix notation)
    Result will be  - > 324*+
 */

private fun String.toReversePolish(): String? {
//    return "24 3 * 1 +"
//     initializing empty String for result
    if (isNullOrBlank()) return ""
    var result: String? = ""

    // initializing empty stack
    val stack: Stack<Char> = Stack()
    for (i in 0 until length) {
        val c = this[i]

        // If the scanned character is an
        // operand, add it to output.
        when {
            c.isDigit() -> {
                result += c
            }
            c == '(' -> stack.push(c)
            c == ')' -> {
                while (!stack.isEmpty() &&
                    stack.peek() !== '('
                )
                    result += stack.pop()+" "
                stack.pop()
            }
            else  // an operator is encountered
            -> {
                result +=" "
                while (!stack.isEmpty() && getPrecedence(c)
                    <= getPrecedence(stack.peek())
                ) {
                    result += stack.pop()+" "
                }
                stack.push(c)
            }
        }
    }

    // pop all the operators from the stack
    while (!stack.isEmpty()) {
        if (stack.peek() == '(') return "Invalid Expression"
        result = result+" " + stack.pop()
    }
    return result
}

private fun Char.isFunction(): Boolean = (this == '/' || this == '-' || this == '*' || this == '+')

private fun getPrecedence(ch: Char): Int = when (ch) {
    '+', '-' -> 1
    '*', '/' -> 2
    '^' -> 3
    else -> -1
}


