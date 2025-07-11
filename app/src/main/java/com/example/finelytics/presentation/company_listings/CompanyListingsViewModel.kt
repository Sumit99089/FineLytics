package com.example.finelytics.presentation.company_listings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.query
import com.example.finelytics.domain.model.CompanyListing
import com.example.finelytics.domain.repository.StockRepositoryInterface
import com.example.finelytics.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyListingsViewModel @Inject constructor(
    private val repository: StockRepositoryInterface
): ViewModel() {
    var state by mutableStateOf(CompanyListingsState())
    var searchJob: Job? = null
    fun onEvent(event: CompanyListingsEvent){
        if(event is CompanyListingsEvent.Refresh){
            getCompanyListings(
                fetchFromRemote = true
            )
        }
        else if(event is CompanyListingsEvent.OnSearchQueryChange){
            state = state.copy(searchQuery = event.query)
            searchJob?.cancel()
            searchJob = viewModelScope.launch{
                delay(timeMillis = 500L)
                getCompanyListings()
            }
        }
    }

    private fun getCompanyListings(
        query: String = state.searchQuery.lowercase(),
        fetchFromRemote: Boolean = false){
        viewModelScope.launch {
            val resultFlow = repository.getCompanyListings(fetchFromRemote, query )
            resultFlow.collect { value->
                when(value){
                    is Resource.Success ->{
                        value.data?.let{listings->
                            state = state.copy(companies = listings)
                        }
                    }
                    is Resource.Error-> Unit
                    is Resource.Loading->{
                        state = state.copy(isLoading = value.isLoading)
                    }
                }
            }
        }
    }
}