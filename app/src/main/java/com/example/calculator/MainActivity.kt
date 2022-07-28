package com.example.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.calculator.calculator.Calculator
import com.example.calculator.ui.page.CalculatorUI
import com.example.calculator.ui.theme.CalculatorTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {
                    CalculatorUI(viewModel)
                }
            }
        }

        val list = listOf<String>(
            "(",
            "8",
            "+",
            "7",
            ")",
//            "×",
//            "4",
        )
        Calculator().cal(list)
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

//@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CalculatorTheme {
        Greeting("Android")
    }
}

@Preview
@Composable
fun Test() {
    Column(modifier = Modifier.fillMaxSize()) {

        Box(modifier = Modifier.weight(1f).fillMaxSize()) {
            Text(text = "123", modifier = Modifier.align(Alignment.BottomEnd))
        }
    }
}