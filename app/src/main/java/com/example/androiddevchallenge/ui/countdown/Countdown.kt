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
