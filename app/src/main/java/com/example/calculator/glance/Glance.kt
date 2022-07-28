package com.example.calculator.glance

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.*
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle

private val countPreferenceKey = intPreferencesKey("count-key")
private val countParamKey = ActionParameters.Key<Int>("count-key")

private val equalPreferenceKey = stringPreferencesKey("equal")
private val numberInputParamKey = ActionParameters.Key<String>("number-input")

class Glance : GlanceAppWidget() {

    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    @Preview
    @Composable
    override fun Content() {
        val prefs = currentState<Preferences>()
        val ans = prefs[equalPreferenceKey] ?: "0"

//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalAlignment = Alignment.CenterVertically,
//        ) {
//            Button(text = "9", onClick = actionRunCallback<Test>(
//                parameters = actionParametersOf(
//                    numberInputParamKey to ("9")
//                )))
//            Text(text = ans)
//        }

        Column(modifier = GlanceModifier.fillMaxSize().background(Color.LightGray).cornerRadius(16.dp)) {
            Column(modifier = GlanceModifier.defaultWeight().fillMaxWidth().background(Color.Gray)
                .cornerRadius(16.dp).padding(16.dp)) {
                Text(text = ans, modifier = GlanceModifier.defaultWeight().appWidgetBackground())
                Row {
                    Text(text = "=")
                    Text(text = "ans")
                }
            }



            Row(modifier = GlanceModifier.defaultWeight().fillMaxWidth().wrapContentHeight()) {
                val weight = GlanceModifier.defaultWeight()
                WidgetText(text = "(", weight)
                WidgetText(text = ")", weight)
                WidgetText(text = "B", weight)
                WidgetText(text = "AC", weight)
            }
            Row(modifier = GlanceModifier.defaultWeight().fillMaxWidth().wrapContentHeight()) {
                val weight = GlanceModifier.defaultWeight()
                WidgetText(text = "7", weight)
                WidgetText(text = "8", weight)
                WidgetText(text = "9", weight)
                WidgetText(text = "+", weight)
            }
            Row(modifier = GlanceModifier.defaultWeight().fillMaxWidth().wrapContentHeight()) {
                val weight = GlanceModifier.defaultWeight()
                WidgetText(text = "4", weight)
                WidgetText(text = "5", weight)
                WidgetText(text = "6", weight)
                WidgetText(text = "-", weight)
            }
            Row(modifier = GlanceModifier.defaultWeight().fillMaxWidth().wrapContentHeight()) {
                val weight = GlanceModifier.defaultWeight()
                WidgetText(text = "1", weight)
                WidgetText(text = "2", weight)
                WidgetText(text = "3", weight)
                WidgetText(text = "×", weight)
            }
            Row(modifier = GlanceModifier.defaultWeight().fillMaxWidth().wrapContentHeight()) {
                val weight = GlanceModifier.defaultWeight()
                WidgetText(text = "00", weight)
                WidgetText(text = "0", weight)
                WidgetText(text = ".", weight)
                WidgetText(text = "÷", weight)
            }
        }
    }

}

class Test : ActionCallback {
    override suspend fun onRun(context: Context, glanceId: GlanceId, parameters: ActionParameters) {

        val num = requireNotNull(parameters[numberInputParamKey]) {
            "error"
        }

        Log.d("!!!", "onRun: ")

        updateAppWidgetState(
            context = context,
            definition = PreferencesGlanceStateDefinition,
            glanceId = glanceId
        ) { preferences ->
            preferences.toMutablePreferences().apply {
                Log.d("!!!", "onRun: $num")
                this[equalPreferenceKey] = num
            }
        }

        Glance().update(context, glanceId)
    }
}

class StatefulWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = Glance()
}

@Composable
fun WidgetText(text: String, weight: GlanceModifier) {
    Box(modifier = weight.padding(8.dp)) {
        Text(text = text,
            modifier = GlanceModifier.fillMaxWidth().wrapContentHeight(),
            style = TextStyle(textAlign = TextAlign.Center, fontSize = 24.sp))
    }
}