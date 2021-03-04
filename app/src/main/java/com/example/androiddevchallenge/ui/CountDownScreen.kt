/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.ui

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.StopCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun CountDownScreen() {
    TopAppBar {
        Row(
            Modifier
                .fillMaxSize()
                .padding(start = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "CountDown App",
                style = MaterialTheme.typography.h6,
            )
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CountDownEntry()
        Spacer(modifier = Modifier.height(50.dp))
        CountDownTimer()
    }
}

val entryState = mutableStateOf("")
var currentEntry = 0f
val isCountDownOn = mutableStateOf(false)

@Composable
fun CountDownEntry() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(0.5f),
            value = entryState.value,
            onValueChange = { entryState.value = it },
            label = {
                Text(text = "Timer Entry")
            },
            placeholder = {
                Text(text = "Seconds")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.size(10.dp))
        Surface(shape = CircleShape) {
            Image(
                modifier = Modifier
                    .clickable {
                        if (entryState.value != "") isCountDownOn.value = !isCountDownOn.value
                        if (isCountDownOn.value) {
                            if (entryState.value.toFloat() != currentEntry) {
                                currentEntry = entryState.value.toFloat()
                                countDownState.countDownValueState.value =
                                    currentEntry
                            }
                        }
                    }
                    .size(50.dp),
                imageVector = if (isCountDownOn.value) Icons.Default.StopCircle else Icons.Default.PlayCircle,
                contentDescription = null,
            )
        }
    }
}

class CountDownProgressState() {

    val countDownValueState = mutableStateOf(0f)

    fun start() {
        Handler(Looper.getMainLooper()).postDelayed(
            updateProgress,
            100
        )
    }

    fun stop() {
    }

    private val updateProgress: Runnable = Runnable {
        if (countDownValueState.value.roundToInt() > 0) {
            countDownValueState.value = countDownValueState.value - 0.1f
            if (countDownValueState.value.roundToInt() == 0) isCountDownOn.value = false
        }
    }
}

val countDownState = CountDownProgressState()

@Composable
fun CountDownTimer() {
    val state = remember { countDownState }
    if (isCountDownOn.value) {
        state.start()
    } else {
        state.stop()
    }
    Box(modifier = Modifier, contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            progress = 100f,
            modifier = Modifier
                .size(200.dp),
            color = Color.Black
        )
        CircularProgressIndicator(
            progress = if (currentEntry != 0f) state.countDownValueState.value.roundToInt() / currentEntry else 1F,
            modifier = Modifier
                .size(200.dp),
        )
        Text(
            text = countDownState.countDownValueState.value.roundToInt().toString(),
            style = MaterialTheme.typography.h1,
            color = MaterialTheme.colors.primary
        )
    }
}
