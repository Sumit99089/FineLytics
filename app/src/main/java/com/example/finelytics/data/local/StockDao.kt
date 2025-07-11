package com.example.finelytics.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query


//Dao-> Data Access Object. for room databases.caching
//This Interface is to make functions for Insert ,Update , Delete, Search Query.
@Dao
interface StockDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertCompanyListings(
        companyListingEntity: List<CompanyListingEntity>
    )

    //The Table name is coming from @ENTITY in CompanyListingEntity which becomes the name of the table but all in lower case
    //So that's why "DELETE FROM companylistingentity"

    @Query("DELETE FROM companylistingentity")
    suspend fun clearCompanyListings()


    @Query(
        """
            SELECT * 
            FROM companylistingentity 
            WHERE LOWER(name) LIKE '%'||LOWER(:query)||'%' 
            OR UPPER(:query) == symbol
        """
            )
    suspend fun searchCompanyListings(query: String): List<CompanyListingEntity>
}


//