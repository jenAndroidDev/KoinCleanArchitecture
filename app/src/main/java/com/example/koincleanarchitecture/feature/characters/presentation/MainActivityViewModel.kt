package com.example.koincleanarchitecture.feature.characters.presentation

import android.nfc.Tag
import android.util.Log
import androidx.collection.longIntMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.koincleanarchitecture.feature.characters.domain.repository.CharacterRepository
import com.example.koincleanarchitecture.utils.network.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

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
        }
    }
    private fun getAllCharacters(){
        viewModelScope.launch {
            repository.getAllCharacters(1).collectLatest {result->
                when(result){
                    is Result.Loading->{

                    }
                    is Result.Success->{
                        Log.d(Tag, "getAllCharacters() called with: result = ${result.data.data}")
                    }
                    is Result.Error->{

                    }
                }
            }
        }
    }


}
data class CharacterUiState(
    val data:List<Character> = emptyList()
)
sealed interface CharacterUiAction{
    data class Scroll(
        val firstVisiblePosition:Int,
        val lastVisibleItemPosition:Int
    ):CharacterUiAction
}