package com.example.finelytics.data.csv

import java.io.InputStream

interface CSVParserInterface<T>{
    suspend fun parse(stream: InputStream): List<T>
}


//The generic type T is the type you want csvReader to parse to. for eg CompanyListings.