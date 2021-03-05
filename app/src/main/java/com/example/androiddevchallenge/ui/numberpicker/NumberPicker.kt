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
package com.example.androiddevchallenge.ui.numberpicker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun NumberPicker(modifier: Modifier, number: Int, onNumberChange: (Int) -> Unit) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        val (count, setCount) = remember { mutableStateOf(number) }

        IconButton(onClick = { setCount(count.dec()) }) {
            Icon(
                imageVector = Icons.Filled.Remove,
                contentDescription = "decrement"
            )
            onNumberChange(count)
        }

        Text(
            text = "$count",
            style = MaterialTheme.typography.h2,
            textAlign = TextAlign.End,
            modifier = Modifier.padding(
                horizontal = 8.dp,
                vertical = 8.dp
            )
        )

        IconButton(onClick = { setCount(count.inc()) }) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "increment"
            )
            onNumberChange(count)
        }
    }
}
