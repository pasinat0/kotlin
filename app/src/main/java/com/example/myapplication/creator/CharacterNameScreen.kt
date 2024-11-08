package com.example.myapplication.creator

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.persistence.AppDatabase
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterNameScreen(modifier: Modifier = Modifier, database: AppDatabase, onNameValidated: (String) -> Unit) {
    var characterName by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val characterDao = database.characterDao()
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier.padding(16.dp)) {
        TextField(
            value = characterName,
            onValueChange = { characterName = it },
            label = { Text("Nome do Personagem") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xA175FFF9),
                focusedIndicatorColor = Color(0xFF1FAED2),
                unfocusedIndicatorColor = Color(0xFF1FAED2)
            ),
            shape = MaterialTheme.shapes.medium
        )

        Button(
            onClick = {
                coroutineScope.launch {
                    val existingCharacter = characterDao.getCharacterByNickname(characterName)
                    if (existingCharacter != null) {
                        errorMessage = "Nome já existe. Escolha outro nome."
                    } else {
                        errorMessage = ""
                        onNameValidated(characterName)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1FAED2))
        ) {
            Text("Continuar", color = Color.White, fontSize = 16.sp)
        }

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}