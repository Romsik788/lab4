package com.roman.lab4.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.roman.lab4.database.CountryDao
import com.roman.lab4.models.Country

class CountryInfoViewModel: ViewModel() {

    private val countryDetail: MutableLiveData<Country> = MutableLiveData()
    fun getCountryDetail(): LiveData<Country> = countryDetail

    fun fetchCountryByNameAndCountry(name: String, cDao: CountryDao) {
        val country = cDao.findCountryByName(name)
        countryDetail.value = Country(country)
    }
}