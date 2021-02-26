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
package com.example.androiddevchallenge.ui.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.model.DogItem
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.ui.utils.NetworkImage

class DogDetailActivity : AppCompatActivity() {

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp {
                    Box(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
                        DogDetailsCard(intent.getParcelableExtra(INTENT_DOG_ITEM))
                        IconButton(
                            onClick = { onBackPressed() },
                            modifier = Modifier.padding(8.dp)
                                .background(Color.Black.copy(alpha = 0.1f), shape = CircleShape)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_back_24),
                                contentDescription = "Back Button",
                                modifier = Modifier.size(32.dp),
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun DogDetailsCard(dogItem: DogItem?) {
        Box {
            NetworkImage(
                url = dogItem?.image,
                modifier = Modifier.fillMaxWidth().height(300.dp),
                contentScale = ContentScale.Crop
            )
            Card(
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                modifier = Modifier.fillMaxHeight().padding(top = 260.dp)
            ) {
                Column(modifier = Modifier.padding(32.dp)) {
                    Text(
                        text = dogItem?.name.orEmpty(),
                        style = MaterialTheme.typography.h6
                    )
                    Text(
                        text = dogItem?.description.orEmpty(),
                        style = MaterialTheme.typography.subtitle1
                    )
                    OutlinedButton(
                        onClick = {},
                        modifier = Modifier.padding(top = 24.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text(text = "Adopt me \uD83D\uDC36")
                    }
                }

            }

        }
    }

}