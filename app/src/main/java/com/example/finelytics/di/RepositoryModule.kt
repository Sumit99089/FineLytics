package com.example.finelytics.di

import com.example.finelytics.data.csv.CSVParserInterface
import com.example.finelytics.data.csv.CompanyListingParser
import com.example.finelytics.data.repository.StockRepositoryImpl
import com.example.finelytics.domain.model.CompanyListing
import com.example.finelytics.domain.repository.StockRepositoryInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCompanyListingParser(
        companyListingParser: CompanyListingParser
    ): CSVParserInterface<CompanyListing>

    @Binds
    @Singleton
    abstract fun bindStockRepository(
        stockRepositoryImpl: StockRepositoryImpl
    ): StockRepositoryInterface

}