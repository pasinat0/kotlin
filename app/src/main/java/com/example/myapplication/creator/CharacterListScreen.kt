package com.example.myapplication.creator

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.persistence.AppDatabase
import com.example.myapplication.persistence.CharacterEntity
import kotlinx.coroutines.launch

@Composable
fun CharacterListScreen(navController: NavController, database: AppDatabase) {
    val coroutineScope = rememberCoroutineScope()
    var characters by remember { mutableStateOf<List<CharacterEntity>>(emptyList()) }

    fun loadCharacters() {
        coroutineScope.launch {
            characters = database.characterDao().getAllCharacters()
        }
    }

    fun deleteCharacter(character: CharacterEntity) {
        coroutineScope.launch {
            database.characterDao().delete(character)
            loadCharacters()
        }
    }

    LaunchedEffect(Unit) {
        loadCharacters()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        IconButton(onClick = {
            navController.navigate("name_screen")
        }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }

        Text(
            text = "Personagens Criados",
            style = MaterialTheme.typography.headlineLarge,
            color = Color(0xFF8C1414),
            modifier = Modifier.padding(vertical = 16.dp)
        )

        if (characters.isNotEmpty()) {
            characters.forEach { character ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xA3EE5656))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Nome: ${character.name}", fontSize = 18.sp, color = Color(
                            0xFF000000
                        )
                        )
                        Text(text = "Raça: ${character.race}", fontSize = 16.sp, color = Color.Black)
                        Text(text = "Pontos de Vida: ${character.hitPoints}", fontSize = 14.sp, color = Color.Green)

                        IconButton(
                            onClick = { deleteCharacter(character) },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Icon(Icons.Filled.Delete, contentDescription = "Delete Character", tint = Color.Red)
                        }
                    }
                }
            }
        } else {
            Text(
                text = "Nenhum personagem foi criado ainda.",
                color = Color.Gray,
                fontSize = 16.sp
            )
        }
    }
}
