package com.example.androiddevchallenge.ui.countdown

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CountdownTimerViewModel : ViewModel() {

    private val startTimeInMillis = 1000L

    private val timeLeftInMillis = MutableStateFlow(startTimeInMillis)

    private val _state = MutableStateFlow(CountdownTimerViewState())

    val state: StateFlow<CountdownTimerViewState>
        get() = _state

    private lateinit var countDownTimer: CountDownTimer

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
        timeLeftInMillis.value = startTimeInMillis;
        countDownTimer = freshCountdownTimer()
        _state.value = CountdownTimerViewState(startTimeInMillis, TimerState.Init)
    }

    private fun freshCountdownTimer() = object : CountDownTimer(startTimeInMillis, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            timeLeftInMillis.value = millisUntilFinished
            _state.value = CountdownTimerViewState(currentTime = timeLeftInMillis.value)
        }

        override fun onFinish() {
            confetti()
        }
    }

    private fun confetti() {
        _state.value = CountdownTimerViewState(0L, TimerState.Finished)
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(timeLeftInMillis.value, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis.value = millisUntilFinished
                _state.value = CountdownTimerViewState(
                    currentTime = timeLeftInMillis.value,
                    state = TimerState.Running
                )
            }

            override fun onFinish() {
                confetti()
            }
        }
        countDownTimer.start()
        _state.value = CountdownTimerViewState(timeLeftInMillis.value, TimerState.Running)
    }

    private fun pauseTimer() {
        countDownTimer.cancel()
        _state.value = CountdownTimerViewState(timeLeftInMillis.value, TimerState.Paused)
    }
}

data class CountdownTimerViewState(
    val currentTime: Long = 0L,
    val state: TimerState = TimerState.Init
)

enum class TimerState {
    Init,
    Running,
    Paused,
    Finished
}