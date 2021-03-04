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
package com.example.androiddevchallenge.ui.countdown

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun Countdown() {

    val countdownTimerViewModel: CountdownTimerViewModel = viewModel()
    val viewState by countdownTimerViewModel.state.collectAsState()

    Column {

        Text(text = viewState.currentTime.toString())

        Button(onClick = countdownTimerViewModel::onPlayPause) {
            when (viewState.state) {
                TimerState.Init -> Text("Start")
                TimerState.Running -> Text("Pause")
                TimerState.Paused -> Text("Continue")
                TimerState.Finished -> Text("Restart")
            }
        }

        Button(enabled = true, onClick = countdownTimerViewModel::onStop) {
            Text("Reset")
        }
    }
}
