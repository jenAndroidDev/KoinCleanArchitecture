package com.example.koincleanarchitecture

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.koincleanarchitecture.databinding.ActivityMainBinding
import com.example.koincleanarchitecture.feature.characters.presentation.CharacterUiState
import com.example.koincleanarchitecture.feature.characters.presentation.CharactersAdapter
import com.example.koincleanarchitecture.feature.characters.presentation.MainActivityViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

private const val Tag = "MainActivity"
class MainActivity : AppCompatActivity() {
    /*
    * 1.Create a recycler view and populate the data
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
            uiState = viewModel.uiState
        )
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
    }
    private fun ActivityMainBinding.bindState(
        uiState:StateFlow<CharacterUiState>
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
    }
}