package com.example.finelytics.presentation.company_listings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finelytics.domain.repository.StockRepositoryInterface
import com.example.finelytics.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyListingsViewModel @Inject constructor(
    private val repository: StockRepositoryInterface
): ViewModel() {
    var state by mutableStateOf(CompanyListingsState())
    private var searchJob: Job? = null

    init {
        getCompanyListings()
    }
    fun onEvent(event: CompanyListingsEvent){
        when(event){
            is CompanyListingsEvent.Refresh->{
                getCompanyListings(fetchFromRemote = true)
            }
            is CompanyListingsEvent.OnSearchQueryChange->{
                state = state.copy(searchQuery = event.query)
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500L)
                    getCompanyListings()
                }
            }
        }
    }

    fun getCompanyListings(
        query: String = state.searchQuery.lowercase(),
        fetchFromRemote: Boolean = false){
        viewModelScope.launch {
            repository.getCompanyListings(fetchFromRemote, query )
                .collect {result->
                when(result){
                    is Resource.Success ->{
                        result.data?.let{listings->
                            state = state.copy(companies = listings)
                        }
                    }
                    is Resource.Error-> Unit
                    is Resource.Loading->{
                        state = state.copy(isLoading = result.isLoading, isRefreshing = result.isLoading)
                    }
                }
            }
        }
    }
}