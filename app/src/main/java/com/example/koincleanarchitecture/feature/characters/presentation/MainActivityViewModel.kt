package com.example.koincleanarchitecture.feature.characters.presentation


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.koincleanarchitecture.feature.characters.domain.model.Character
import com.example.koincleanarchitecture.feature.characters.domain.repository.CharacterRepository
import com.example.koincleanarchitecture.utils.network.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


private const val Tag = "MainActivityViewModel"


class MainActivityViewModel(private val repository: CharacterRepository) :ViewModel() {

    private val _uiState = MutableStateFlow(CharacterUiState())
    val uiState = _uiState.asStateFlow()

    val action:(CharacterUiAction)->Unit

    init {

        action={
            onUiAction(it)
        }
        getAllCharacters()
    }
    private fun onUiAction(action: CharacterUiAction){
        when(action){
            is CharacterUiAction.Scroll->{
                Log.d(Tag, "onUiAction() called with: action = $action")
            }
            is CharacterUiAction.Refresh->{

            }
        }
    }
    private fun getAllCharacters(){
        viewModelScope.launch {
            repository.getAllCharacters(1).collectLatest {result->
                when(result){
                    is Result.Loading->{
                        //TODO W.R.T R J Jenin Joseph
                    }
                    is Result.Success->{
                        val tempList = uiState.value.data.toMutableList()
                        tempList.addAll(
                            result.data.data.map {
                                CharacterUiModel.CharacterUiItem(it)
                            }
                        )
                        _uiState.update {
                            it.copy(
                                data = tempList
                            )
                        }
                        Log.d(Tag, "getAllCharacters() called with: result = ${result.data.data}")
                    }
                    is Result.Error->{
                        //TODO W.r.t Jenin Joseph
                    }
                }
            }
        }
    }
}
sealed class CharacterUiModel{
    data class CharacterUiItem(val item:Character):CharacterUiModel()
}
data class CharacterUiState(
    val data:List<CharacterUiModel> = emptyList()
)
sealed interface CharacterUiAction{
    data class Scroll(
        val firstVisiblePosition:Int,
        val lastVisibleItemPosition:Int,
        val totalItemCount:Int
    ):CharacterUiAction

    data object Refresh:CharacterUiAction
}