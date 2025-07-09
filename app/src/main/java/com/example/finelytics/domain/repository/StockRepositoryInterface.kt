package com.example.finelytics.domain.repository

import com.example.finelytics.domain.model.CompanyListing
import com.example.finelytics.utils.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepositoryInterface {

    suspend fun getCompanyListings(
        fetchFromRemoteApi: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>>
}