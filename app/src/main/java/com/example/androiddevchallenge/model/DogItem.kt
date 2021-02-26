package com.example.androiddevchallenge.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class DogItem(
    val id: Int,
    val name: String,
    val origin: String,
    val image: String,
    val bred_for: String,
    val description: String
):Parcelable