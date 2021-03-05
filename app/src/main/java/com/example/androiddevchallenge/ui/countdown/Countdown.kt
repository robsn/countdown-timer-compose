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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.ui.numberpicker.NumberPicker
import com.example.androiddevchallenge.ui.theme.four
import com.example.androiddevchallenge.ui.theme.one
import com.example.androiddevchallenge.ui.theme.two

@Composable
fun Countdown() {

    val countdownTimerViewModel: CountdownTimerViewModel = viewModel()
    val viewState by countdownTimerViewModel.state.collectAsState()

    if (viewState.state == TimerState.Init) {
        LandingCountdown(viewState, countdownTimerViewModel)
    } else {
        ActiveCountdown(viewState, countdownTimerViewModel)
    }
}

@Composable
private fun ActiveCountdown(
    viewState: CountdownTimerViewState,
    countdownTimerViewModel: CountdownTimerViewModel
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .background(one)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                style = MaterialTheme.typography.h4,
                textAlign = TextAlign.Center,
                text = "Lap ${viewState.currentLap} / ${viewState.totalLaps}"
            )
        }
        Row(
            modifier = Modifier
                .background(two)
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                style = MaterialTheme.typography.h1,
                textAlign = TextAlign.Center,
                text = "${
                String.format(
                    "%02d",
                    viewState.timeLeftInMs / 1000 / 60
                )
                }:${String.format("%02d", viewState.timeLeftInMs / 1000 % 60)}"
            )
        }

        Row(
            modifier = Modifier
                .background(four)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            IconButton(onClick = countdownTimerViewModel::onPlayPause) {
                when (viewState.state) {
                    TimerState.Init, TimerState.Paused -> Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = stringResource(id = R.string.start)
                    )
                    TimerState.Running -> Icon(
                        imageVector = Icons.Filled.Pause,
                        contentDescription = stringResource(id = R.string.pause)
                    )
                    TimerState.Finished -> Icon(
                        imageVector = Icons.Filled.Replay,
                        contentDescription = stringResource(id = R.string.restart)
                    )
                }
            }

            IconButton(onClick = countdownTimerViewModel::onStop) {
                Icon(
                    imageVector = Icons.Filled.Stop,
                    contentDescription = stringResource(id = R.string.reset)
                )
            }
        }
    }
}

@Composable
private fun LandingCountdown(
    viewState: CountdownTimerViewState,
    countdownTimerViewModel: CountdownTimerViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
    ) {
        Text(
            text = stringResource(R.string.seconds_per_lap),
            style = MaterialTheme.typography.h4,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .background(one)
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 16.dp
                )
        )

        NumberPicker(
            modifier = Modifier
                .background(two)
                .fillMaxWidth()
                .weight(1f)
                .padding(
                    horizontal = 16.dp,
                    vertical = 32.dp
                ),
            number = (viewState.timeLeftInMs / 1000).toInt(),
            onNumberChange = { countdownTimerViewModel.startTime(it * 1000L) }
        )

        Text(
            modifier = Modifier
                .background(one)
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 16.dp
                ),
            text = stringResource(R.string.number_of_laps),
            style = MaterialTheme.typography.h4,
            textAlign = TextAlign.Center,
        )

        NumberPicker(
            modifier = Modifier
                .background(two)
                .fillMaxWidth()
                .weight(1f)
                .padding(
                    horizontal = 16.dp,
                    vertical = 32.dp
                ),
            number = viewState.totalLaps,
            onNumberChange = { countdownTimerViewModel.totalLaps(it) }
        )

        IconButton(
            modifier = Modifier
                .background(four)
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 32.dp
                ),
            onClick = countdownTimerViewModel::onPlayPause
        ) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = stringResource(id = R.string.start)
            )
        }
    }
}
