package com.example.calculator.ui.page

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculator.CurrentState
import com.example.calculator.MainViewModel
import com.example.calculator.R
import com.example.calculator.calculator.Calculator
import com.example.calculator.ui.theme.primaryContainer
import com.example.calculator.ui.theme.surface
import com.example.calculator.ui.theme.surfaceVariant

val NUMBER_FONT_SIZE = 48.sp
val BUTTON_FONT_SIZE = 36.sp

@Composable
fun CalculatorUI(viewModel: MainViewModel) {
    Column(Modifier.background(surface)) {
        Column(modifier = Modifier
            .weight(1f)
            .fillMaxSize()
            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            .background(surfaceVariant)
            .padding(16.dp)

        ) {

            val infix = remember {
                derivedStateOf {
                    var text = ""
                    viewModel.infix.map { text += it }
                    text
                }
            }
//            BasicTextField(
//                value = infix.value,
//                onValueChange = {},
//                readOnly = true,
//                modifier = Modifier
//                    .weight(1F)
//                    .verticalScroll(rememberScrollState()),
//                textStyle = TextStyle(fontSize = 48.sp),
//            )
            Text(
                text = infix.value,
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(1F),
                fontSize = NUMBER_FONT_SIZE,
//                maxLines = 1
            )
            Row {
                Text(text = "=", fontSize = NUMBER_FONT_SIZE)

                var lastNumber by remember {
                    mutableStateOf(0.0)
                }

                var formatState by remember {
                    mutableStateOf(true)
                }

                val equal = remember {
                    derivedStateOf {
                        val ans = Calculator().cal(viewModel.infix.toList())
//
//                        ans

                        if (ans == "no value") {
                            formatState = false

                            if (lastNumber % 1.0 == 0.0) {
                                lastNumber.toInt().toString()
                            } else lastNumber.toString()
                        } else {
                            formatState = true

                            lastNumber = ans.toDouble()
                            ans
                        }
                    }
                }
                Text(text = equal.value,
                    modifier = Modifier
                        .weight(1F)
                        .horizontalScroll(rememberScrollState())
                        .alpha(if (formatState) LocalContentAlpha.current else ContentAlpha.disabled),
                    fontSize = NUMBER_FONT_SIZE,
                    textAlign = TextAlign.End)
            }
        }


        Row(modifier = Modifier
            .height(IntrinsicSize.Min)
            .padding(horizontal = 4.dp)) {
            val modifier = Modifier
                .padding(4.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(primaryContainer)
                .weight(1f)
                .align(Alignment.CenterVertically)

            TextButton(string = "(", modifier, {
                when (viewModel.currentState.value) {
                    CurrentState.NULL, CurrentState.OPERATOR, CurrentState.LEFT -> true
                    else -> false
                }
            }) { viewModel.operatorInput("(") }
            TextButton(string = ")", modifier, {
                if (viewModel.parenthesesState.value > 0) {
                    when (viewModel.currentState.value) {
                        CurrentState.ZERO, CurrentState.INTEGER, CurrentState.DOUBLE, CurrentState.RIGHT -> true
                        else -> false
                    }
                } else false
            }) { viewModel.operatorInput(")") }
            IconButton(painter = painterResource(id = R.drawable.ic_backspace_fill0_wght400_grad0_opsz48),
                modifier = modifier, { true }) { viewModel.btBackSpace() }
            TextButton(string = "AC", modifier, { true }) { viewModel.btAC() }
        }

        Row(modifier = Modifier.padding(horizontal = 4.dp)) {
            val modifier = Modifier
                .padding(4.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(primaryContainer)
                .weight(1f)
                .align(Alignment.CenterVertically)

            TextButton(string = "7", modifier, {
                when (viewModel.currentState.value) {
                    CurrentState.RIGHT -> false
                    else -> true
                }
            }) { viewModel.numInput("7") }
            TextButton(string = "8", modifier, {
                when (viewModel.currentState.value) {
                    CurrentState.RIGHT -> false
                    else -> true
                }
            }) { viewModel.numInput("8") }
            TextButton(string = "9", modifier, {
                when (viewModel.currentState.value) {
                    CurrentState.RIGHT -> false
                    else -> true
                }
            }) { viewModel.numInput("9") }
            TextButton(string = "+", modifier, {
                when (viewModel.currentState.value) {
                    CurrentState.ZERO, CurrentState.INTEGER, CurrentState.DOUBLE, CurrentState.RIGHT -> true
                    else -> false
                }
            }) { viewModel.operatorInput("+") }
        }

        Row(modifier = Modifier.padding(horizontal = 4.dp)) {
            val modifier = Modifier
                .padding(4.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(primaryContainer)
                .weight(1f)
                .align(Alignment.CenterVertically)

            TextButton(string = "4", modifier, {
                when (viewModel.currentState.value) {
                    CurrentState.RIGHT -> false
                    else -> true
                }
            }) { viewModel.numInput("4") }
            TextButton(string = "5", modifier, {
                when (viewModel.currentState.value) {
                    CurrentState.RIGHT -> false
                    else -> true
                }
            }) { viewModel.numInput("5") }
            TextButton(string = "6", modifier, {
                when (viewModel.currentState.value) {
                    CurrentState.RIGHT -> false
                    else -> true
                }
            }) { viewModel.numInput("6") }
            TextButton(string = "-", modifier, {
//                when (viewModel.currentState.value) {
//                    CurrentState.ZERO, CurrentState.INTEGER, CurrentState.DOUBLE, CurrentState.RIGHT -> true
//                    else -> false
//                }

                true
            }) {
                when (viewModel.currentState.value) {
                    CurrentState.OPERATOR, CurrentState.LEFT, CurrentState.NULL -> viewModel.numInput(
                        "-")
                    else -> viewModel.operatorInput("-")
                }
            }
        }

        Row(modifier = Modifier.padding(horizontal = 4.dp)) {
            val modifier = Modifier
                .padding(4.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(primaryContainer)
                .weight(1f)
                .align(Alignment.CenterVertically)

            TextButton(string = "1", modifier, {
                when (viewModel.currentState.value) {
                    CurrentState.RIGHT -> false
                    else -> true
                }
            }) { viewModel.numInput("1") }
            TextButton(string = "2", modifier, {
                when (viewModel.currentState.value) {
                    CurrentState.RIGHT -> false
                    else -> true
                }
            }) { viewModel.numInput("2") }
            TextButton(string = "3", modifier, {
                when (viewModel.currentState.value) {
                    CurrentState.RIGHT -> false
                    else -> true
                }
            }) { viewModel.numInput("3") }
            TextButton(string = "×", modifier, {
                when (viewModel.currentState.value) {
                    CurrentState.ZERO, CurrentState.INTEGER, CurrentState.DOUBLE, CurrentState.RIGHT -> true
                    else -> false
                }
            }) { viewModel.operatorInput("×") }
        }

        Row(modifier = Modifier.padding(horizontal = 4.dp)) {
            val modifier = Modifier
                .padding(4.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(primaryContainer)
                .weight(1f)
                .align(Alignment.CenterVertically)

            TextButton(string = "00", modifier, {
                when (viewModel.currentState.value) {
                    CurrentState.ZERO, CurrentState.RIGHT -> false
                    else -> true
                }
            }) { viewModel.numInput("00") }
            TextButton(string = "0", modifier, {
                when (viewModel.currentState.value) {
                    CurrentState.ZERO, CurrentState.RIGHT -> false
                    else -> true
                }
            }) { viewModel.numInput("0") }
            TextButton(string = ".", modifier, {
                when (viewModel.currentState.value) {
                    CurrentState.NULL, CurrentState.ZERO, CurrentState.INTEGER, CurrentState.NEGATIVE -> true
                    else -> false
                }
            }) { viewModel.numInput(".") }
            TextButton(string = "÷", modifier, {
                when (viewModel.currentState.value) {
                    CurrentState.ZERO, CurrentState.INTEGER, CurrentState.DOUBLE, CurrentState.RIGHT -> true
                    else -> false
                }
            }) { viewModel.operatorInput("÷") }
        }

    }
}

@Composable
fun TextButton(string: String, modifier: Modifier, clickable: () -> Boolean, click: () -> Unit) {

    val contentAlpha by animateFloatAsState(buttonAlpha(boolean = clickable()))

    Box(modifier = modifier
        .alpha(contentAlpha)
        .clickable(clickable()) { click() }
        .padding(8.dp)) {
        Text(text = string, modifier = Modifier.align(Alignment.Center), fontSize = BUTTON_FONT_SIZE)
    }
}

@Composable
fun IconButton(painter: Painter, modifier: Modifier, clickable: () -> Boolean, click: () -> Unit) {

    val contentAlpha by animateFloatAsState(buttonAlpha(boolean = clickable()))

    Box(modifier = modifier
        .alpha(contentAlpha)
        .clickable(clickable()) { click() }
        .padding(8.dp)) {
        Icon(painter = painter,
            contentDescription = null,
            modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun buttonAlpha(boolean: Boolean) =
    if (boolean) LocalContentAlpha.current else ContentAlpha.disabled
