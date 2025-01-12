package com.example.koincleanarchitecture.feature.characters.presentation



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.koincleanarchitecture.feature.characters.domain.model.Character
import com.example.koincleanarchitecture.feature.characters.domain.repository.CharacterRepository
import com.example.koincleanarchitecture.utils.network.Result
import com.example.koincleanarchitecture.utils.paging.LoadState
import com.example.koincleanarchitecture.utils.paging.LoadStates
import com.example.koincleanarchitecture.utils.paging.LoadType
import com.pepul.shopsseller.utils.paging.PagedRequest
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.CancellationException


private const val Tag = "MainActivityViewModel"


class MainActivityViewModel(private val repository: CharacterRepository) :ViewModel() {

    private val _uiState = MutableStateFlow(CharacterUiState())
    val uiState = _uiState.asStateFlow()

    private val scrollState = MutableSharedFlow<CharacterUiAction.Scroll>(1)
    val action:(CharacterUiAction)->Unit
    private val job:Job?=null
    private var endOfPagination:Boolean = false
    private var pageNumber = 1

    init {
        scrollState.distinctUntilChanged()
            .filterNot {
                uiState.value.loadState.append is LoadState.Loading
            }
            .onEach {action->
                if (action.shouldFetchMore && !endOfPagination){
                    createCharacterPagedRequest(shouldRefresh = false)
                }
            }
            .launchIn(viewModelScope)
        action={
            onUiAction(it)
        }
        createCharacterPagedRequest(shouldRefresh = true)
    }

    private fun onUiAction(action: CharacterUiAction){
        when(action){
            is CharacterUiAction.Scroll->{
                viewModelScope.launch { scrollState.emit(action) }
            }
            is CharacterUiAction.Refresh->{
                createCharacterPagedRequest(shouldRefresh = true)
            }
        }
    }
    private fun createCharacterPagedRequest(shouldRefresh:Boolean){
        if (job?.isActive==true ||(!shouldRefresh && endOfPagination )) job?.cancel(
            CancellationException("Ongoing Api Call")
        )
        val pagedRequest = if (shouldRefresh){
            PagedRequest.create(LoadType.REFRESH, key = pageNumber, loadSize = DEFAULT_LOAD_SIZE )
        }else{
            PagedRequest.create(LoadType.APPEND, key = pageNumber, loadSize = DEFAULT_LOAD_SIZE)
        }
        getAllCharacters(request = pagedRequest)
    }
    private fun getAllCharacters(request:PagedRequest<Int>){
        val loadType = if (request is PagedRequest.Refresh){
            LoadType.REFRESH
        }else{
            LoadType.APPEND
        }
        viewModelScope.launch {
            repository.getAllCharacters(1).collectLatest {result->
                when(result){
                    is Result.Loading->{
                        setLoading(loadState = LoadStates.IDLE.refresh, loadType = loadType)
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
                        endOfPagination = result.data.data.size < DEFAULT_LOAD_SIZE
                        Timber.tag(Tag).d("endOfPagination...$endOfPagination")
                        if (endOfPagination){
                            setLoading(loadType, LoadState.NotLoading.Complete)
                        }else{
                            pageNumber++
                            setLoading(loadType, LoadState.NotLoading.InComplete)
                        }
                    }
                    is Result.Error->{
                        setLoading(loadType, LoadState.Error(result.exception))
                    }
                }
            }
        }
    }
    private fun setLoading(
        loadType: LoadType = LoadType.REFRESH,
        loadState: LoadState
    ){
        val newLoadState =uiState.value.loadState
            .modifyState(loadType,loadState)
        _uiState.update {
            it.copy(
                loadState = newLoadState
            )
        }

    }
}
sealed class CharacterUiModel{
    data class CharacterUiItem(val item:Character):CharacterUiModel()
}
data class CharacterUiState(
    val data:List<CharacterUiModel> = emptyList(),
    val loadState: LoadStates = LoadStates.IDLE
)
sealed interface CharacterUiAction{
    data class Scroll(
        val visibleItemCount:Int,
        val totalItemCount:Int,
        val lastVisibleItemPosition:Int,
    ):CharacterUiAction

    data object Refresh:CharacterUiAction
}
private const val DEFAULT_LOAD_SIZE = 10
private const val VISIBLE_ITEM_THRESHOLD = 5
private val CharacterUiAction.Scroll.shouldFetchMore get() =
    visibleItemCount+lastVisibleItemPosition+ VISIBLE_ITEM_THRESHOLD>=totalItemCount
