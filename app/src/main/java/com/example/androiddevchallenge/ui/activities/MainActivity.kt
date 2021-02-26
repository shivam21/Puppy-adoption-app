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

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.data.DogRepository
import com.example.androiddevchallenge.model.DogItem
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.ui.utils.NetworkImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

const val INTENT_DOG_ITEM = "dogItem"

class MainActivity : AppCompatActivity() {

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp("Doggo.cafe") {
                    DogListContent()
                }
            }
        }
    }

}

@ExperimentalAnimationApi
@Composable
fun DogListContent() {
    val dogsList = DogRepository.dogList
    val listState = LazyListState()
    val scope = rememberCoroutineScope()
    Box(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
        DogList(dogsList, listState)
        ScrollToTopButton(listState, scope)
    }
}

// Start building your app here!
@ExperimentalAnimationApi
@Composable
fun MyApp(toolbarTitle: String = "", content: @Composable () -> Unit = {}) {
    Surface(color = MaterialTheme.colors.background) {
        Scaffold(topBar = {
            if (toolbarTitle.isNotEmpty())
                TopAppBar(
                    title = {
                        Column(modifier = Modifier.padding(4.dp)) {
                            Text(text = toolbarTitle)
                        }
                    },
                    backgroundColor = Color.White,
                    elevation = 4.dp
                )
        }) {
            content()
        }

    }
}

@ExperimentalAnimationApi
@Composable
fun ScrollToTopButton(listState: LazyListState, scope: CoroutineScope) {
    AnimatedVisibility(
        visible = listState.firstVisibleItemIndex > 0,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().fillMaxHeight()
        ) {
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        listState.scrollToItem(0, 0)
                    }
                }, shape = CircleShape,
                backgroundColor = Color.White,
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_baseline_keyboard_arrow_up_24),
                    contentDescription = "Arrow Image",
                    contentScale = ContentScale.Crop
                )
            }
        }
    }

}

@Composable
fun DogList(dogsList: List<DogItem>, listState: LazyListState) {
    LazyColumn(state = listState, content = {
        items(dogsList) { item ->
            DogListItem(item)
        }
    }, contentPadding = PaddingValues(bottom = 60.dp))
}

@Composable
fun DogListItem(item: DogItem, context: Context = LocalContext.current) {
    Card(Modifier.fillMaxWidth().padding(8.dp).clickable {
        context.startActivity(Intent(context, DogDetailActivity::class.java).apply {
            putExtra(INTENT_DOG_ITEM, item)
        })
    }) {
        Column(Modifier.fillMaxWidth()) {
            DogCardHeader(item)
            NetworkImage(
                url = item.image,
                modifier = Modifier.fillMaxWidth().height(200.dp),
                contentScale = ContentScale.Crop
            )
            DogCardFooter(item)
        }
    }

}

@Composable
fun DogCardFooter(dogItem: DogItem) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .background(Color.Black.copy(alpha = 0.8f)).padding(8.dp)
    ) {
        Text(
            text = "Bred For:${dogItem.bred_for}",
            style = MaterialTheme.typography.subtitle2,
            color = Color.White,
            modifier = Modifier.padding(start = 8.dp, end = 8.dp)
        )
    }

}

@Composable
fun DogCardHeader(dogItem: DogItem) {
    Row(Modifier.padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
        NetworkImage(
            url = dogItem.image,
            modifier = Modifier.width(40.dp).height(40.dp).clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Column {
            Text(
                text = dogItem.name,
                style = MaterialTheme.typography.subtitle1,
                color = Color.Black,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp)
            )
            Text(
                text = "Origin:${dogItem.origin}",
                style = MaterialTheme.typography.subtitle2,
                color = Color.Black,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp)
            )
        }

    }
}

@ExperimentalAnimationApi
@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp {
            DogListContent()
        }
    }
}

@ExperimentalAnimationApi
@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp {
            DogListContent()
        }
    }
}
