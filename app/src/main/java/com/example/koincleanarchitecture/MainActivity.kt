package com.example.koincleanarchitecture

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.koincleanarchitecture.databinding.ActivityMainBinding
import com.example.koincleanarchitecture.feature.characters.presentation.CharacterUiAction
import com.example.koincleanarchitecture.feature.characters.presentation.CharacterUiState
import com.example.koincleanarchitecture.feature.characters.presentation.CharactersAdapter
import com.example.koincleanarchitecture.feature.characters.presentation.MainActivityViewModel
import com.pepul.shopsseller.utils.paging.LoadState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

private const val Tag = "MainActivity"
class MainActivity : AppCompatActivity() {
    /*
    * 1.Create a recycler view and populate the data==>Done
    * 2.Add pagination and loadstate
    * 3.Add End of Pagination Component
    * 4.Add LoadState Adapter
    * 5.Migrate to Ktor
    * 6.Toggle Functionality for Character ReadStatus.
    * 7.Unit Test with Junit4*/
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
        rvCharacters.adapter = adapter
        val characters = uiState.map { it.data }.distinctUntilChanged()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                characters.collectLatest {
                    adapter.submitList(it)
                    Log.d(Tag, "bindState" +
                            "ad() called...$it")
                }
            }
        }
        val endOfPagination = uiState.map {
            it.loadState
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
        val layoutManager = rvCharacters.layoutManager as LinearLayoutManager
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
                        rvCharacters.removeOnScrollListener(scrollListener)
                    }else{
                        rvCharacters.addOnScrollListener(scrollListener)
                    }
                }
            }
        }
    }
}