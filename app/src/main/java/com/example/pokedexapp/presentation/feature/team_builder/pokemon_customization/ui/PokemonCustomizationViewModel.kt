package com.example.pokedexapp.presentation.feature.team_builder.pokemon_customization.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.SavedStateHandle
import com.example.pokedexapp.data.local.entities.TeamPokemonEntity
import com.example.pokedexapp.domain.model.Nature
import com.example.pokedexapp.domain.model.PokemonDetails
import com.example.pokedexapp.domain.repository.PokemonRepository
import com.example.pokedexapp.domain.repository.TeamRepository
import com.example.pokedexapp.presentation.feature.team_builder.pokemon_customization.state.PokemonCustomizationState
import com.example.pokedexapp.utils.ApiConfig
import com.example.pokedexapp.utils.ErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.floor

@HiltViewModel
class PokemonCustomizationViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository,
    private val teamRepository: TeamRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val teamId: Int = savedStateHandle.get<Int>("teamId") ?: 0
    private val slot: Int = savedStateHandle.get<Int>("slot") ?: 0
    private val initialPokemonId: Int = savedStateHandle.get<Int>("pokemonId") ?: 1
    private val memberId: Int = savedStateHandle.get<Int>("memberId") ?: 0

    private val _state = MutableStateFlow(PokemonCustomizationState())
    val state = _state.asStateFlow()

    init {
        if (teamId != 0 || initialPokemonId != 1) {
            loadData()
        } else {
            _state.update { it.copy(error = "Invalid parameters provided") }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val details = pokemonRepository.getPokemonDetails("${ApiConfig.BASE_URL}pokemon/$initialPokemonId/")
                val species = pokemonRepository.getPokemonSpecies(details.species.url)
                
                val variantDetails = coroutineScope {
                    species.varieties
                        .filter { !it.pokemon.name.contains("totem") }
                        .map { variety ->
                            async {
                                pokemonRepository.getPokemonDetails(variety.pokemon.url)
                            }
                        }
                        .awaitAll()
                        .sortedBy { v -> species.varieties.indexOfFirst { it.pokemon.name == v.name } }
                }

                var currentMember: TeamPokemonEntity? = null
                if (memberId > 0) {
                    currentMember = teamRepository.getTeamMember(memberId).firstOrNull()
                }

                _state.update {
                    it.copy(
                        pokemonDetails = details,
                        varieties = variantDetails,
                        member = currentMember,
                        nickname = currentMember?.nickname,
                        level = currentMember?.level ?: 100,
                        selectedNature = currentMember?.nature ?: "Hardy",
                        selectedAbility = currentMember?.ability ?: details.abilities.firstOrNull()?.ability?.name,
                        hpEv = currentMember?.hpEv ?: 0,
                        atkEv = currentMember?.atkEv ?: 0,
                        defEv = currentMember?.defEv ?: 0,
                        spaEv = currentMember?.spaEv ?: 0,
                        spdEv = currentMember?.spdEv ?: 0,
                        speEv = currentMember?.speEv ?: 0,
                        hpIv = currentMember?.hpIv ?: 31,
                        atkIv = currentMember?.atkIv ?: 31,
                        defIv = currentMember?.defIv ?: 31,
                        spaIv = currentMember?.spaIv ?: 31,
                        spdIv = currentMember?.spdIv ?: 31,
                        speIv = currentMember?.speIv ?: 31,
                        move1 = currentMember?.move1,
                        move2 = currentMember?.move2,
                        move3 = currentMember?.move3,
                        move4 = currentMember?.move4,
                        isLoading = false
                    )
                }
                
                fetchMoveDetails(details)
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = ErrorHandler.mapException(e)) }
            }
        }
    }

    fun onRetry() {
        loadData()
    }

    private suspend fun fetchMoveDetails(details: PokemonDetails) {
        coroutineScope {
            details.moves.map { moveEntry ->
                async {
                    if (!_state.value.moveDetails.containsKey(moveEntry.move.name)) {
                        try {
                            val moveInfo = pokemonRepository.getMoveDetails(moveEntry.move.url)
                            _state.update { s ->
                                s.copy(moveDetails = s.moveDetails + (moveEntry.move.name to moveInfo))
                            }
                        } catch (e: Exception) {
                            // Silent fail for single move
                        }
                    }
                }
            }.awaitAll()
        }
    }

    fun onNicknameChanged(nickname: String) {
        _state.update { it.copy(nickname = nickname) }
    }

    fun onLevelChanged(level: Int) {
        if (level in 1..100) {
            _state.update { it.copy(level = level) }
        }
    }

    fun onNatureChanged(natureName: String) {
        _state.update { it.copy(selectedNature = natureName) }
    }

    fun onAbilityChanged(abilityName: String) {
        _state.update { it.copy(selectedAbility = abilityName) }
    }

    fun onVarietyChanged(details: PokemonDetails) {
        _state.update { 
            it.copy(
                pokemonDetails = details,
                selectedAbility = details.abilities.firstOrNull()?.ability?.name
            ) 
        }
        viewModelScope.launch {
            fetchMoveDetails(details)
        }
    }

    fun onMoveSearchQueryChanged(query: String) {
        _state.update { it.copy(moveSearchQuery = query) }
    }

    fun onEvChanged(stat: String, value: Int) {
        val currentState = _state.value
        val currentTotal = currentState.hpEv + currentState.atkEv + currentState.defEv + 
                           currentState.spaEv + currentState.spdEv + currentState.speEv
        
        val oldValue = when(stat) {
            "HP" -> currentState.hpEv
            "Atk" -> currentState.atkEv
            "Def" -> currentState.defEv
            "SpA" -> currentState.spaEv
            "SpD" -> currentState.spdEv
            "Spe" -> currentState.speEv
            else -> 0
        }

        val diff = value - oldValue
        if (currentTotal + diff <= 510 && value <= 252 && value >= 0) {
            _state.update {
                when(stat) {
                    "HP" -> it.copy(hpEv = value)
                    "Atk" -> it.copy(atkEv = value)
                    "Def" -> it.copy(defEv = value)
                    "SpA" -> it.copy(spaEv = value)
                    "SpD" -> it.copy(spdEv = value)
                    "Spe" -> it.copy(speEv = value)
                    else -> it
                }
            }
        }
    }

    fun onIvChanged(stat: String, value: Int) {
        if (value in 0..31) {
            _state.update {
                when(stat) {
                    "HP" -> it.copy(hpIv = value)
                    "Atk" -> it.copy(atkIv = value)
                    "Def" -> it.copy(defIv = value)
                    "SpA" -> it.copy(spaIv = value)
                    "SpD" -> it.copy(spdIv = value)
                    "Spe" -> it.copy(speIv = value)
                    else -> it
                }
            }
        }
    }

    fun calculateStat(base: Int, ev: Int, iv: Int, level: Int, isHp: Boolean, statKey: String? = null): Int {
        val multiplier = statKey?.let { 
            Nature.fromName(_state.value.selectedNature).getMultiplierForStat(it) 
        } ?: 1.0

        return if (isHp) {
            floor((2.0 * base + iv + floor(ev / 4.0)) * level / 100.0).toInt() + level + 10
        } else {
            val baseResult = floor(floor((2.0 * base + iv + floor(ev / 4.0)) * level / 100.0) + 5)
            floor(baseResult * multiplier).toInt()
        }
    }

    fun onMoveSelected(index: Int, moveName: String?) {
        _state.update { s ->
            // If the move is already selected in another slot, clear that slot
            var nextS = s
            if (moveName != null) {
                if (s.move1 == moveName && index != 1) nextS = nextS.copy(move1 = null)
                if (s.move2 == moveName && index != 2) nextS = nextS.copy(move2 = null)
                if (s.move3 == moveName && index != 3) nextS = nextS.copy(move3 = null)
                if (s.move4 == moveName && index != 4) nextS = nextS.copy(move4 = null)
            }

            when(index) {
                1 -> nextS.copy(move1 = moveName)
                2 -> nextS.copy(move2 = moveName)
                3 -> nextS.copy(move3 = moveName)
                4 -> nextS.copy(move4 = moveName)
                else -> nextS
            }
        }
    }

    fun save(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val s = _state.value
            val currentPokemonDetails = s.pokemonDetails ?: return@launch
            
            val entity = TeamPokemonEntity(
                id = if (memberId > 0) memberId else 0,
                teamId = teamId,
                pokemonId = currentPokemonDetails.id, // Save the ID of the selected variety
                pokemonName = currentPokemonDetails.name,
                slot = slot,
                nickname = s.nickname,
                level = s.level,
                nature = s.selectedNature,
                ability = s.selectedAbility,
                hpEv = s.hpEv,
                atkEv = s.atkEv,
                defEv = s.defEv,
                spaEv = s.spaEv,
                spdEv = s.spdEv,
                speEv = s.speEv,
                hpIv = s.hpIv,
                atkIv = s.atkIv,
                defIv = s.defIv,
                spaIv = s.spaIv,
                spdIv = s.spdIv,
                speIv = s.speIv,
                move1 = s.move1,
                move2 = s.move2,
                move3 = s.move3,
                move4 = s.move4
            )

            if (memberId > 0) {
                teamRepository.updateTeamMember(entity)
            } else {
                teamRepository.addPokemonToTeamWithDetails(entity)
            }
            onSuccess()
        }
    }
}
