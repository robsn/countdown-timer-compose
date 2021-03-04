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

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class CountdownTimerViewModel : ViewModel() {

    private var startTimeInMillis = 10000L
    private var totalLaps = 3

    private val timeLeftInMillisFlow = MutableStateFlow(startTimeInMillis)
    private val currentLapsFlow = MutableStateFlow(1)
    private val timerStateFlow = MutableStateFlow(TimerState.Init)

    private val _state = MutableStateFlow(CountdownTimerViewState())

    val state: StateFlow<CountdownTimerViewState>
        get() = _state

    private lateinit var countDownTimer: CountDownTimer

    init {
        viewModelScope.launch {
            combine(
                timeLeftInMillisFlow,
                timerStateFlow,
                currentLapsFlow
            ) { millis, timerState, laps ->
                CountdownTimerViewState(millis, timerState, laps, totalLaps)
            }.collect { _state.value = it }
        }
    }


    fun onPlayPause() {
        when (state.value.state) {
            TimerState.Init -> startTimer()
            TimerState.Running -> pauseTimer()
            TimerState.Paused -> startTimer()
            TimerState.Finished -> {
                reset()
                startTimer()
            }
        }
    }

    fun onStop() {
        reset()
    }

    private fun reset() {
        countDownTimer.cancel()
        timeLeftInMillisFlow.value = startTimeInMillis
        countDownTimer = freshCountdownTimer()
        timerStateFlow.value = TimerState.Init
        currentLapsFlow.value = 1
    }

    private fun freshCountdownTimer() = object : CountDownTimer(startTimeInMillis, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            timeLeftInMillisFlow.value = millisUntilFinished
        }

        override fun onFinish() {
            confetti()
        }
    }

    private fun confetti() {
        if (currentLapsFlow.value < state.value.totalLaps) {
            currentLapsFlow.value = currentLapsFlow.value.inc()
            countDownTimer = freshCountdownTimer().start()
        } else {
            timerStateFlow.value = TimerState.Finished
        }
    }

    private fun startTimer() {
        timerStateFlow.value = TimerState.Running
        countDownTimer = object : CountDownTimer(timeLeftInMillisFlow.value, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillisFlow.value = millisUntilFinished
            }

            override fun onFinish() {
                confetti()
            }
        }
        countDownTimer.start()
    }

    private fun pauseTimer() {
        countDownTimer.cancel()
        timerStateFlow.value = TimerState.Paused
    }

    fun totalLaps(laps: Int) {
        totalLaps = laps
    }

    fun startTime(millis: Long) {
        startTimeInMillis = millis
        timeLeftInMillisFlow.value = millis
    }
}

data class CountdownTimerViewState(
    val timeLeftInMs: Long = 30000L,
    val state: TimerState = TimerState.Init,
    val currentLap: Int = 1,
    val totalLaps: Int = 3,
)

enum class TimerState {
    Init,
    Running,
    Paused,
    Finished
}
