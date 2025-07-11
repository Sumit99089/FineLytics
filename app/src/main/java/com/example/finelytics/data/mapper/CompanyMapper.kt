package com.example.finelytics.data.mapper

import com.example.finelytics.data.local.CompanyListingEntity
import com.example.finelytics.domain.model.CompanyListing

fun CompanyListingEntity.toCompanyListing(): CompanyListing{
    return CompanyListing(
        name = name,
        exchange = exchange,
        symbol= symbol
    )
}
//This is an extension function.Works same as writing methods inside of CompanyListingEntity

fun CompanyListing.toCompanyListingEntity(): CompanyListingEntity{
    return CompanyListingEntity(
        name = name,
        exchange = exchange,
        symbol= symbol
    )
}


//This is an extension function.Works same as writing methods inside of CompanyListingEntity