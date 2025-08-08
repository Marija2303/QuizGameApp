package com.example.quizgameapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quizgameapp.ui.theme.QuizGameAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: QuizGameViewModel = viewModel()
            QuizGameAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        QuizApp(viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun QuizApp(viewModel: QuizGameViewModel) {
    val questionId by viewModel.questionId.collectAsState()
    val score by viewModel.score.collectAsState() // Compose može da reaguje samo na State, a ne direktno na StateFlow
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "welcomescreen") {
        composable("welcomescreen") {
            WelcomeScreen {
                viewModel.startQuiz()
                navController.navigate("quizgame")
            }
        }

        composable("quizgame") {
            val curQuestion = viewModel.currentQuestion
            if (curQuestion != null) {
                QuizGame(curQuestion, questionId + 1) { idCheckAnswer ->
                    viewModel.checkAnswer(idCheckAnswer)
                }
            } else {
                navController.navigate("gameoverscreen")
            }
        }

        composable("gameoverscreen") {
            GameOverScreen(score) {
                navController.navigate("welcomescreen")
            }
        }
    }
}

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    onStartClick: () -> Unit,
) {
    Box (
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Welcome to",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "Marija's",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "awesome",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "QUIZ",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
        }
        Button(
            onClick = onStartClick,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 150.dp)
        ) {
            Text(
                text = "START",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun QuizGame(question: Question, questionId: Int, onAnswerClick: (Int) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(54.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = questionId.toString() + ". " + question.question,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )

        Spacer(modifier = Modifier.padding(34.dp))

        // Za svaki odgovor se pravi Card dugme
        question.answers.forEachIndexed { index, answer ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable (onClick = { onAnswerClick(index) })
                    .padding(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Text(
                    text = answer,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun GameOverScreen(score: Int, onRestartClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "GAME OVER!",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )

        Text(
            text = "YOUR SCORE: $score",
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 180.dp),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 20.sp
            )
        )
        Button(onClick = onRestartClick, modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 110.dp)) {
            Text("Play Again")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomePreview() {
    QuizGameAppTheme {
        //WelcomeScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun GamePreview() {
    QuizGameAppTheme {
        val q = Question("Kako se zove Marijin PAS?", listOf("Hugica", "Kuglica", "Hugo", "Mohito"), 2)
        QuizGame(q,1 ,{})
    }
}

@Preview(showBackground = true)
@Composable
fun GameOverPreview() {
    QuizGameAppTheme {
        GameOverScreen(2, onRestartClick = {})
    }
}
