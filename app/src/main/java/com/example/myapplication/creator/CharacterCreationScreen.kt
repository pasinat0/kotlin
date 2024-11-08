package com.example.myapplication.creator

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.persistence.AppDatabase
import com.example.myapplication.persistence.CharacterEntity
import com.example.myapplication.model.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterCreationScreen(modifier: Modifier = Modifier, database: AppDatabase, characterName: String, navController: NavController) {
    var strength by remember { mutableStateOf("8") }
    var dexterity by remember { mutableStateOf("8") }
    var constitution by remember { mutableStateOf("8") }
    var intelligence by remember { mutableStateOf("8") }
    var wisdom by remember { mutableStateOf("8") }
    var charisma by remember { mutableStateOf("8") }
    var selectedRace by remember { mutableStateOf("Humano") }
    var hitPoints by remember { mutableStateOf(0) }
    var expanded by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }

    val races = listOf("Humano", "Elfo", "Anão", "Halfling", "Gnomo", "Draconato")
    val initialAttributes = getInitialAttributesForRace(selectedRace)
    val totalPoints = listOf(
        strength.toIntOrNull() ?: 8,
        dexterity.toIntOrNull() ?: 8,
        constitution.toIntOrNull() ?: 8,
        intelligence.toIntOrNull() ?: 8,
        wisdom.toIntOrNull() ?: 8,
        charisma.toIntOrNull() ?: 8
    ).sum() - initialAttributes.values.sum()

    val darkPurple = Color(0xFF1FAED2)
    val lightPurple = Color(0xA175FFF9)

    val coroutineScope = rememberCoroutineScope()

    var characters by remember { mutableStateOf<List<CharacterEntity>>(emptyList()) }

    fun loadCharacters() {
        coroutineScope.launch {
            characters = database.characterDao().getAllCharacters()
        }
    }

    LaunchedEffect(Unit) {
        loadCharacters()
    }

    Column(modifier = modifier.padding(16.dp)) {
        TextField(
            value = strength,
            onValueChange = { strength = it },
            label = { Text("Força") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = lightPurple,
                focusedIndicatorColor = darkPurple,
                unfocusedIndicatorColor = darkPurple
            ),
            shape = MaterialTheme.shapes.medium
        )
        TextField(
            value = dexterity,
            onValueChange = { dexterity = it },
            label = { Text("Destreza") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = lightPurple,
                focusedIndicatorColor = darkPurple,
                unfocusedIndicatorColor = darkPurple
            ),
            shape = MaterialTheme.shapes.medium
        )
        TextField(
            value = intelligence,
            onValueChange = { intelligence = it },
            label = { Text("Inteligência") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = lightPurple,
                focusedIndicatorColor = darkPurple,
                unfocusedIndicatorColor = darkPurple
            ),
            shape = MaterialTheme.shapes.medium
        )
        TextField(
            value = charisma,
            onValueChange = { charisma = it },
            label = { Text("Carisma") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = lightPurple,
                focusedIndicatorColor = darkPurple,
                unfocusedIndicatorColor = darkPurple
            ),
            shape = MaterialTheme.shapes.medium
        )
        TextField(
            value = wisdom,
            onValueChange = { wisdom = it },
            label = { Text("Sabedoria") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = lightPurple,
                focusedIndicatorColor = darkPurple,
                unfocusedIndicatorColor = darkPurple
            ),
            shape = MaterialTheme.shapes.medium
        )
        TextField(
            value = constitution,
            onValueChange = { constitution = it },
            label = { Text("Constituição") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = lightPurple,
                focusedIndicatorColor = darkPurple,
                unfocusedIndicatorColor = darkPurple
            ),
            shape = MaterialTheme.shapes.medium
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedRace,
                onValueChange = {},
                readOnly = true,
                label = { Text("Raça") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = lightPurple,
                    focusedIndicatorColor = darkPurple,
                    unfocusedIndicatorColor = darkPurple
                ),
                shape = MaterialTheme.shapes.medium
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                races.forEach { race ->
                    DropdownMenuItem(
                        text = { Text(text = race) },
                        onClick = {
                            selectedRace = race
                            expanded = false
                            val initialAttributes = getInitialAttributesForRace(race)
                            strength = initialAttributes["Força"].toString()
                            dexterity = initialAttributes["Destreza"].toString()
                            constitution = initialAttributes["Constituição"].toString()
                            intelligence = initialAttributes["Inteligência"].toString()
                            wisdom = initialAttributes["Sabedoria"].toString()
                            charisma = initialAttributes["Carisma"].toString()
                        }
                    )
                }
            }
        }

        // Button to Create Character
        Button(
            onClick = {
                coroutineScope.launch {
                    try {
                        val valid = validateAttributes(strength, dexterity, constitution, intelligence, wisdom, charisma, initialAttributes)
                        if (valid) {
                            val points = listOf(strength, dexterity, constitution, intelligence, wisdom, charisma)
                            val characterCreator = DefaultCharacterCreator()
                            val race = getRace(selectedRace)
                            val character = characterCreator.createCharacter(race, points)
                            val characterEntity = CharacterEntity(
                                name = characterName,
                                race = selectedRace,
                                strength = character.attributes["Força"] ?: 8,
                                dexterity = character.attributes["Destreza"] ?: 8,
                                constitution = character.attributes["Constituição"] ?: 8,
                                intelligence = character.attributes["Inteligência"] ?: 8,
                                wisdom = character.attributes["Sabedoria"] ?: 8,
                                charisma = character.attributes["Carisma"] ?: 8,
                                hitPoints = character.calculateHealthPoints()
                            )
                            database.characterDao().insert(characterEntity)
                            loadCharacters()
                            successMessage = "Personagem criado com sucesso!"
                            errorMessage = ""
                            navController.navigate("character_list")
                        } else {
                            errorMessage = "Distribuição de pontos inválida. Certifique-se de que a soma dos pontos distribuídos é 27 e cada atributo está entre 8 e 15."
                            successMessage = ""
                        }
                    } catch (e: Exception) {
                        errorMessage = "Erro ao criar personagem: ${e.message}"
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = darkPurple)
        ) {
            Text("Criar Personagem", color = Color.White, fontSize = 16.sp)
        }

        // Display Success or Error Messages
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        if (successMessage.isNotEmpty()) {
            Text(
                text = successMessage,
                color = Color.Green,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Text(
            text = "Pontos de Vida: $hitPoints",
            style = MaterialTheme.typography.bodyLarge,
            color = darkPurple,
            modifier = Modifier.padding(top = 16.dp)
        )

        Text(
            text = "Total de Pontos Distribuídos: $totalPoints",
            style = MaterialTheme.typography.bodyLarge,
            color = if (totalPoints == 27) darkPurple else Color.Red,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

fun validateAttributes(strength: String, dexterity: String, constitution: String, intelligence: String, wisdom: String, charisma: String, initialAttributes: Map<String, Int>): Boolean {
    val attributes = listOf(strength, dexterity, constitution, intelligence, wisdom, charisma)
    if (attributes.any { it.toIntOrNull() == null || it.toInt() !in 8..15 }) {
        return false
    }
    val totalDistributedPoints = attributes.sumOf { it.toInt() } - initialAttributes.values.sum()
    return totalDistributedPoints == 27
}

fun getInitialAttributesForRace(raceName: String): Map<String, Int> {
    return when (raceName) {
        "Humano" -> mapOf("Força" to 8, "Destreza" to 8, "Constituição" to 8, "Inteligência" to 8, "Sabedoria" to 8, "Carisma" to 8)
        "Elfo" -> mapOf("Força" to 8, "Destreza" to 10, "Constituição" to 8, "Inteligência" to 8, "Sabedoria" to 8, "Carisma" to 8)
        "Anão" -> mapOf("Força" to 10, "Destreza" to 8, "Constituição" to 10, "Inteligência" to 8, "Sabedoria" to 8, "Carisma" to 8)
        "Halfling" -> mapOf("Força" to 8, "Destreza" to 10, "Constituição" to 8, "Inteligência" to 8, "Sabedoria" to 8, "Carisma" to 10)
        "Gnomo" -> mapOf("Força" to 8, "Destreza" to 8, "Constituição" to 8, "Inteligência" to 10, "Sabedoria" to 8, "Carisma" to 8)
        "Draconato" -> mapOf("Força" to 10, "Destreza" to 8, "Constituição" to 8, "Inteligência" to 8, "Sabedoria" to 8, "Carisma" to 10)
        else -> mapOf("Força" to 8, "Destreza" to 8, "Constituição" to 8, "Inteligência" to 8, "Sabedoria" to 8, "Carisma" to 8)
    }
}

fun getRace(raceName: String): Race {
    return when (raceName) {
        "Humano" -> Human()
        "Elfo" -> Elf()
        "Anão" -> Dwarf()
        "Halfling" -> Halfling()
        "Gnomo" -> Gnome()
        "Draconato" -> DragonBorn()
        else -> Human()
    }
}
