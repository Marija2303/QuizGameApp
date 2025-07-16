package com.example.quizgameapp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import android.util.Log;
import kotlinx.coroutines.flow.StateFlow

class QuizGameViewModel : ViewModel() {
    // Lista data class Question (pitanja i odgovora)
    private val _questions = listOf(
        Question("What is the capital city of Australia?", listOf("Sydney", "Melbourne", "Canberra", "Brisbane"), 2),
        Question("Which planet is known as the Red Planet?", listOf("Venus", "Mars", "Jupiter", "Mercury"), 1),
        Question("Who wrote the play 'Romeo and Juliet'?", listOf("Charles Dickens", "William Shakespeare", "Mark Twain", "Jane Austen"), 1),
        Question("What is the chemical symbol for gold?", listOf("Au", "Ag", "Fe", "Go"), 0)
    )

    private val _score = MutableStateFlow(0) // Broj tacnih odgovora
    private val _questionId = MutableStateFlow(-1) // Broj trenutnog pitanja

    val questionId : StateFlow<Int> = _questionId   // javni nepromenljivi prikaz
    val score :StateFlow<Int> = _score

    // Vraca trenutno pitanje iz liste pitanja ako je index validan
    val currentQuestion: Question?
        get() = if (_questionId.value in _questions.indices)    // indices je raspon od 0 do questions.size - 1
                    _questions[_questionId.value]   // Vraca to pitanje iz liste
                else
                    null    // Ako je korisnik vec zavrsio sva pitanja

    val totalQuestions = _questions.size

    fun checkAnswer(answer: Int) {
        if (answer == currentQuestion?.correctAnswerId) {
            _score.value++
        }
        _questionId.value++
    }

    fun startQuiz() {
        _questionId.value = 0
        _score.value = 0
    }

    fun resetQuiz() {
        _questionId.value = -1
        _score.value = 0
    }
}
