package com.example.finelytics.data.csv

import com.example.finelytics.domain.model.CompanyListing
import com.opencsv.CSVReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompanyListingParser @Inject constructor(): CSVParserInterface<CompanyListing> {

    override suspend fun parse(stream: InputStream): List<CompanyListing> {
        val csvReader = CSVReader(InputStreamReader(stream))
        return withContext(Dispatchers.IO){
            csvReader
                .readAll()
                .drop(1)   //Line 1 contain the headings(column names)
                .mapNotNull{line->
                    val symbol = line.getOrNull(index = 0)
                    val name = line.getOrNull(index = 1)
                    val exchange = line.getOrNull(index = 2)
                    CompanyListing(
                        name = name?: return@mapNotNull null,
                        symbol = symbol?: return@mapNotNull null,
                        exchange = exchange?: return@mapNotNull null
                    )
                }
                .also{
                    csvReader.close()
                }
        }
    }

}