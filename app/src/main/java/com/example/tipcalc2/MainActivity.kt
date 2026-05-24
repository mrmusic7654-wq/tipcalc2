package com.example.tipcalc2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tipcalc2.ui.theme.Tipcalc2Theme
import java.text.NumberFormat
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Tipcalc2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TipCalculatorApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipCalculatorApp(modifier: Modifier = Modifier) {
    var billAmountInput by remember { mutableStateOf("") }
    var tipPercentage by remember { mutableStateOf(15f) }

    val billAmount = billAmountInput.toDoubleOrNull() ?: 0.0
    val tipAmount = calculateTip(billAmount, tipPercentage)
    val totalAmount = billAmount + tipAmount

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = billAmountInput,
            onValueChange = { newValue ->
                billAmountInput = newValue.filter { it.isDigit() || it == '.' }
            },
            label = { Text(stringResource(R.string.bill_amount_label)) },
            leadingIcon = { Icon(Icons.Filled.AttachMoney, contentDescription = "Money icon") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TipPercentageSlider(
            tipPercentage = tipPercentage,
            onValueChange = { tipPercentage = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TipAndTotalDisplay(tipAmount = tipAmount, totalAmount = totalAmount)
    }
}

@Composable
fun TipPercentageSlider(
    tipPercentage: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.tip_percentage_label, tipPercentage.roundToInt()),
            style = MaterialTheme.typography.titleMedium
        )
        Slider(
            value = tipPercentage,
            onValueChange = onValueChange,
            valueRange = 0f..30f,
            steps = 29, // 0 to 30, 1% increments
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun TipAndTotalDisplay(tipAmount: Double, totalAmount: Double, modifier: Modifier = Modifier) {
    val currencyFormatter = NumberFormat.getCurrencyInstance()

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.tip_amount_display, currencyFormatter.format(tipAmount)),
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = stringResource(R.string.total_amount_display, currencyFormatter.format(totalAmount)),
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

private fun calculateTip(billAmount: Double, tipPercentage: Float): Double {
    return billAmount * (tipPercentage / 100)
}

@Preview(showBackground = true)
@Composable
fun TipCalculatorAppPreview() {
    Tipcalc2Theme {
        TipCalculatorApp()
    }
}