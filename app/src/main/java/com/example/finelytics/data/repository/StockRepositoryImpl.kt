package com.example.finelytics.data.repository

import coil.network.HttpException
import com.example.finelytics.data.csv.CSVParserInterface
import com.example.finelytics.data.local.RoomStockDatabase
import com.example.finelytics.data.mapper.toCompanyListing
import com.example.finelytics.data.mapper.toCompanyListingEntity
import com.example.finelytics.data.remote.StockApi
import com.example.finelytics.domain.model.CompanyListing
import com.example.finelytics.domain.repository.StockRepositoryInterface
import com.example.finelytics.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    val api: StockApi,
    val roomDb: RoomStockDatabase,
    val companyListingParser: CSVParserInterface<CompanyListing>
): StockRepositoryInterface{

    private val dao = roomDb.dao

    override suspend fun getCompanyListings(
        fetchFromRemoteApi: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {

        return flow {
            emit (Resource.Loading(isLoading = true))
            val localListingEntity = dao.searchCompanyListings(query = query)
            emit(
                Resource.Success(
                    data = localListingEntity.map { it.toCompanyListing()}
                )
            )
            val isRoomDbEmpty = localListingEntity.isEmpty() && query.isBlank()
            val shouldJustLoadFromCache = !isRoomDbEmpty && !fetchFromRemoteApi
            if(shouldJustLoadFromCache){
               emit(Resource.Loading(isLoading = false))
                return@flow
            }
            val remoteApiListings = try{
                val csvResponse = api.getListings()
                companyListingParser.parse(csvResponse.byteStream())

            }catch(e: HttpException){
                e.printStackTrace()
                emit(
                    Resource.Error(
                        data = null,
                        message = "Http Exception Occoured While Fetching Remote Data"
                    )
                )
                null
            }catch(e: IOException){
                e.printStackTrace()
                emit(
                    Resource.Error(
                        data = null,
                        message = "Input Output Exception While Reading CSV File"
                    )
                )
                null
            }

            remoteApiListings?.let{listings->
                dao.clearCompanyListings()
                dao.insertCompanyListings(
                    listings.map {
                        it.toCompanyListingEntity()
                    }
                )
                val listingsEntity = dao.searchCompanyListings(query = "")
                emit(
                    Resource
                        .Success(
                            data = listingsEntity.map{
                                    it.toCompanyListing()
                                }
                        )
                )
                emit(Resource.Loading(isLoading = false))
            }
        }
    }
}