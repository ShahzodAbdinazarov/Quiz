package com.example.quiz

import com.google.gson.annotations.SerializedName

data class Question(
    @SerializedName("savol")
    val question: String,
    @SerializedName("javob_a")
    val answerA: String,
    @SerializedName("javob_b")
    val answerB: String,
    @SerializedName("javob_c")
    val answerC: String,
    @SerializedName("javob_d")
    val answerD: String
)