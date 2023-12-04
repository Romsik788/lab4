package com.roman.lab4.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.roman.lab4.models.CountryEntity

@Dao
interface CountryDao {
    @Query("SELECT * FROM country")
    fun getAllCountries(): List<CountryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCountries(countries: List<CountryEntity>)

    @Query("SELECT * FROM country WHERE name = :countryName")
    fun findCountryByName(countryName: String): CountryEntity?
}