package com.example.quiz

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.random.Random

val answers: MutableList<String> = mutableListOf()

fun MutableList<String>.clearAnswers(): MutableList<String> {
    for (i in this.indices) this[i] = ""
    return this
}

@Composable
fun MainScreen(questions: List<Question>) {
    val quizzes = remember { mutableStateOf(questions) }
    for (i in quizzes.value.indices) answers.add("")
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val all = remember { mutableStateOf(0) }
    val found = remember { mutableStateOf(0) }
    Column {
        TopAppBar(
            title = { Text(text = stringResource(R.string.app_name)) },
            backgroundColor = Color.White,
            actions = {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    backgroundColor = Color.Green,
                ) {
                    Text(
                        text = "${found.value} / ${all.value}",
                        modifier = Modifier.padding(5.dp)
                    )
                }
                IconButton(onClick = {
                    found.value = 0
                    all.value = 0
                    answers.clearAnswers()
                    quizzes.value = quizzes.value.shuffled()
                    scope.launch {
                        if (quizzes.value.size > 10) listState.scrollToItem(10)
                        listState.animateScrollToItem(0)
                    }
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Refresh,
                        contentDescription = null,
                    )
                }
            })
        LazyColumn(contentPadding = PaddingValues(10.dp), state = listState) {
            items(quizzes.value.size) {
                val color = Color(Random.nextInt(250), Random.nextInt(250), Random.nextInt(250))
                val myAnswer = remember { mutableStateOf(answers[it]) }
                val correct = quizzes.value[it].answerA
                val shuffle: List<String> = mutableListOf(
                    quizzes.value[it].answerA,
                    quizzes.value[it].answerB,
                    quizzes.value[it].answerC,
                    quizzes.value[it].answerD
                ).shuffled()
                Card(
                    shape = RoundedCornerShape(16.dp),
                    backgroundColor = color,
                    modifier = Modifier.padding(5.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Card(
                            shape = RoundedCornerShape(5.dp),
                            backgroundColor = Color.White,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = "${it + 1}. ${quizzes.value[it].question}",
                                color = Color.Black,
                                modifier = Modifier.padding(5.dp)
                            )
                        }
                        for (i in shuffle.indices) {
                            AnswerItem(shuffle[i], it, myAnswer, correct, found, all)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnswerItem(
    answer: String,
    i: Int,
    myAnswer: MutableState<String>,
    correct: String,
    found: MutableState<Int>,
    all: MutableState<Int>
) {
    Button(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 5.dp),
        onClick = {
            if (myAnswer.value == "") {
                answers[i] = answer
                myAnswer.value = answer
                all.value++
                if (answer == correct) found.value++
            }
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (myAnswer.value != "") {
                if (myAnswer.value == answer) {
                    if (answer == correct) Color.Green else Color.Red
                } else if (answer == correct) Color.Yellow else Color.White
            } else Color.White
        ),
    ) { Text(text = answer, color = Color.Black) }
}
