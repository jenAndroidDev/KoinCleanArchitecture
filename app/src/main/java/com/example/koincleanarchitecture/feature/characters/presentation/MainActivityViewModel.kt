package com.example.koincleanarchitecture.feature.characters.presentation
import android.util.Log
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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import java.util.concurrent.CancellationException


private const val Tag = "MainActivityViewModel"
class MainActivityViewModel(private val repository: CharacterRepository) :ViewModel() {

    private val _uiState = MutableStateFlow(CharacterUiState())
    val uiState = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CharacterUiState()
    )
    private val scrollState = MutableSharedFlow<CharacterUiAction.Scroll>(1)
    val action:(CharacterUiAction)->Unit
    private var job:Job?=null
    private var endOfPagination:Boolean = false
    private var pageNumber = 1

    init {
        scrollState.distinctUntilChanged()
            .filterNot {
                uiState.value.loadStates.append is LoadState.Loading
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
                //createCharacterPagedRequest(shouldRefresh = true)
            }
        }
    }
    private fun createCharacterPagedRequest(shouldRefresh:Boolean){
        if (job?.isActive==true ||(!shouldRefresh && endOfPagination )) return
        job?.cancel(CancellationException("Ongoing Api Call"))
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
       job=viewModelScope.launch {
            repository.getAllCharacters(pageNo = pageNumber).collectLatest {result->
                when(result){
                    is Result.Loading->{
                        setLoading(loadState =LoadState.Loading(), loadType = loadType)
                    }
                    is Result.Success->{
                        val tempList = uiState.value.data.toMutableList()
                        if(loadType==LoadType.REFRESH){
                            tempList.clear()
                        }
                        tempList.addAll(
                            result.data.data.map {
                                CharacterUiModel.CharacterUiItem(it)
                            }
                        )
                        _uiState.update {
                            it.copy(
                                data = tempList,
                            )
                        }
                        endOfPagination = result.data.nextKey==null
                        Log.d(Tag, "getAllCharacters() called with: result = $endOfPagination")
                        if (endOfPagination){
                            setLoading(loadType, LoadState.NotLoading.Complete)
                        }else{
                            pageNumber = (result.data.nextKey)!!.toInt()
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
        loadType: LoadType,
        loadState: LoadState
    ){
        val newLoadState = uiState.value.loadStates
            .modifyState(loadType,loadState)

            _uiState.update {
                it.copy(
                    loadStates = newLoadState,
                )
            }
    }
    private fun forceRefresh(){
        createCharacterPagedRequest(false)
    }
    fun refreshCharacterFeed() {
        forceRefresh()
    }
}
sealed class CharacterUiModel{
    data class CharacterUiItem(val item:Character):CharacterUiModel()
}
data class CharacterUiState(
    val data:List<CharacterUiModel> = emptyList(),
    val dummyList:List<Sample> = emptyList(),
    val loadStates: LoadStates = LoadStates.IDLE,
    val loadState: LoadState = LoadState.Loading()
)
sealed interface CharacterUiAction{
    data class Scroll(
        val visibleItemCount:Int,
        val totalItemCount:Int,
        val lastVisibleItemPosition:Int,
    ):CharacterUiAction

    data object Refresh:CharacterUiAction
}
data class Sample(
    var id:String = UUID.randomUUID().toString(),
    val name:String,
    val age:String
)
private const val DEFAULT_LOAD_SIZE = 10
private const val VISIBLE_ITEM_THRESHOLD = 5
private val CharacterUiAction.Scroll.shouldFetchMore get() =
    visibleItemCount+lastVisibleItemPosition+ VISIBLE_ITEM_THRESHOLD>=totalItemCount
