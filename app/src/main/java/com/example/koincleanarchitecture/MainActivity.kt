package com.example.koincleanarchitecture

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.koincleanarchitecture.databinding.ActivityMainBinding
import com.example.koincleanarchitecture.feature.characters.presentation.CharacterUiAction
import com.example.koincleanarchitecture.feature.characters.presentation.CharacterUiState
import com.example.koincleanarchitecture.feature.characters.presentation.CharactersAdapter
import com.example.koincleanarchitecture.feature.characters.presentation.CharactersLoadStateAdapter
import com.example.koincleanarchitecture.feature.characters.presentation.DummyAdapter
import com.example.koincleanarchitecture.feature.characters.presentation.MainActivityViewModel
import com.example.koincleanarchitecture.utils.paging.LoadState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber

private const val Tag = "MainActivity"
class MainActivity : AppCompatActivity() {
    /*
    * 1.Migrate Retrofit Instance to Ktor
    * 2.Toggle Functionality for Character ReadStatus.
    * 3.Unit Test with Junit4
    * 4.Migrate ViewModel by NowinAndroid Backed Offline Architecture Approach.
    * 5.Implement Dark Mode and Light Mode.
    * 6.Handle Last Page Response.
    * 7.Migrate to Koin Annotations*/
    lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        viewModel.action
        binding.bindState(
            uiState = viewModel.uiState,
            action = viewModel.action
        )

    }
    private fun ActivityMainBinding.bindState(
        uiState:StateFlow<CharacterUiState>,
        action:(CharacterUiAction)->Unit
    ){
        val adapter = CharactersAdapter()
        val footerAdapter = CharactersLoadStateAdapter{
            viewModel.refreshCharacterFeed()
        }
        val characters = uiState.map { it.data }.distinctUntilChanged()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                characters.collectLatest {
                    adapter.submitList(it)
                }
            }
        }
        val concatAdapter = ConcatAdapter(
            adapter,footerAdapter
        )
        listCharacters.adapter = concatAdapter

        val loadStates = uiState.map { it.loadStates }.distinctUntilChanged()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                loadStates.collectLatest {loadState->
                    footerAdapter.loadState = loadState.append
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                loadStates.collectLatest { loadState->
                    when{
                        loadState.refresh is LoadState.Loading->{
                            Log.d(Tag, "check refresh state called with: loadState = $loadState")
                           pgCharacters.isVisible =true
                            //listCharacters.isVisible = true
                        }
                        loadState.refresh is LoadState.NotLoading->{
                            pgCharacters.isVisible=uiState.value.data.isEmpty()
                            listCharacters.isVisible = uiState.value.data.isNotEmpty()
                        }
                        loadState.append is LoadState.Loading->{
                        }
                        loadState.append is LoadState.NotLoading->{
                            //Toast.makeText(this@MainActivity,"Hello",Toast.LENGTH_LONG).show()
                            Timber.tag(Tag)
                                .d("check refresh state called with: loadState = " + loadState)
                        }
                        else->{
                        }
                    }
                }
            }
        }
        val endOfPagination = uiState.map {
            it.loadStates
        }.distinctUntilChanged()
            .map {
                it.refresh is LoadState.Loading||it.refresh.endOfPaginationReached
                        && it.append is LoadState.Loading||it.append.endOfPaginationReached
            }
        onScrollListener(
            onScrollChanged = action,
            endOfPagination = endOfPagination
        )
    }
    private fun ActivityMainBinding.onScrollListener(
        onScrollChanged:(CharacterUiAction)->Unit,
        endOfPagination: Flow<Boolean>
    ){
        val layoutManager = listCharacters.layoutManager as LinearLayoutManager
        val scrollListener = object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy>0){
                    val totalItemCount = layoutManager.itemCount
                    val visibleItemCount = layoutManager.childCount
                    val lastVisibleItemPosition= layoutManager.findLastVisibleItemPosition()
                    onScrollChanged(
                        CharacterUiAction.Scroll(
                            visibleItemCount = visibleItemCount,
                            totalItemCount = totalItemCount,
                            lastVisibleItemPosition = lastVisibleItemPosition
                        )
                    )
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                endOfPagination.collectLatest { endOfPagination->
                    if (endOfPagination) {
                        listCharacters.removeOnScrollListener(scrollListener)
                    }else{
                        listCharacters.addOnScrollListener(scrollListener)
                    }
                }
            }
        }
    }
}