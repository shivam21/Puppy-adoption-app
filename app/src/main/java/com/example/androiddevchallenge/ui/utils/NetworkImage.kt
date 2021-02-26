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
package com.example.androiddevchallenge.ui.utils

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

@Composable
fun NetworkImage(
    url: String?,
    modifier: Modifier,
    contentScale: ContentScale = ContentScale.None,
    rotate: Float = 0f,
    error: @Composable (() -> Unit)? = null,
    loading: @Composable (() -> Unit?)? = null
) {
    val state = loadImage(url = url, rotate = rotate)
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,

        ) {
        when (state) {
            is RemoteImageState.Loading -> {
                if (loading != null) {
                    loading()
                } else {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator(
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
            is RemoteImageState.Loaded -> {
                Image(
                    bitmap = state.image.asImageBitmap(),
                    contentDescription = "Network Images",
                    contentScale = contentScale,
                    modifier = Modifier.fillMaxSize().testTag("loadedImg")
                )
            }
            is RemoteImageState.LoadError -> {
                if (error != null) {
                    error()
                } else {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(text = "Could not load image")
                    }
                }
            }
        }
    }
}

@Composable
fun loadImage(
    url: String?,
    rotate: Float,
): RemoteImageState {
    var state by remember(url) {
        mutableStateOf<RemoteImageState>(RemoteImageState.Loading)
    }
    if (url.isNullOrEmpty()) {
        state = RemoteImageState.LoadError
    } else {
        Picasso.get().load(url).rotate(rotate).into(object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                if (bitmap == null) {
                    onBitmapFailed(Exception("Incorrect url"), null)
                } else {
                    state = RemoteImageState.Loaded(bitmap)
                }
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                e?.printStackTrace()
                state = RemoteImageState.LoadError
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
            }
        })
    }
    return state
}