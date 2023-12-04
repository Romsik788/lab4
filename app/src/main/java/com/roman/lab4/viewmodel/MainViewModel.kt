package com.roman.lab4.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.roman.lab4.database.CountryApiService
import com.roman.lab4.database.CountryDao
import com.roman.lab4.models.Country
import com.roman.lab4.models.CountryEntity
import com.roman.lab4.models.PageInfo
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

class MainViewModel : ViewModel() {

    private val countryList: MutableLiveData<List<Country>> = MutableLiveData()

    fun getCountryList(): LiveData<List<Country>> = countryList

    private val countryApiService: CountryApiService = Retrofit.Builder()
        .baseUrl("https://api.worldbank.org/v2/")
        .addConverterFactory(JacksonConverterFactory.create())
        .build()
        .create(CountryApiService::class.java)

    fun fetchCountries(countryDao: CountryDao) {
        val call = countryApiService.getCountries()
        call.enqueue(object : Callback<ArrayList<Any>> {
            override fun onResponse(
                call: Call<ArrayList<Any>>,
                response: Response<ArrayList<Any>>
            ) {
                if (response.isSuccessful) {
                    val objectMapper = jacksonObjectMapper()
                    val pageInfo = objectMapper.readValue<PageInfo>(objectMapper.writeValueAsString(response.body()?.get(0)))

                    val pages = pageInfo.pages
                    val countries = CompletableDeferred<List<Country>>()
                    val newCountryList = mutableListOf<Country>()

                    for (page in 1..pages) {
                        countryApiService.getCountries(page)
                            .enqueue(object : Callback<ArrayList<Any>> {
                                override fun onResponse(
                                    call: Call<ArrayList<Any>>,
                                    response: Response<ArrayList<Any>>
                                ) {
                                    if (response.isSuccessful) {
                                        newCountryList.addAll(
                                            objectMapper.readValue(
                                                objectMapper.writeValueAsString(response.body()?.get(1)),
                                                objectMapper.typeFactory.constructCollectionType(List::class.java, Country::class.java)
                                            )
                                        )

                                        if (newCountryList.size == pageInfo.total){
                                            countries.complete(newCountryList)
                                        }
                                    }
                                }

                                override fun onFailure(call: Call<ArrayList<Any>>, t: Throwable) {
                                    t.printStackTrace()
                                }
                            })
                    }

                    viewModelScope.launch {
                        countryDao.insertCountries(countries.await().map { CountryEntity(it) })
                        countryList.postValue(countries.await())
                    }
                } else {
                    countryList.postValue(countryDao.getAllCountries().map { Country(it) })
                }
            }

            override fun onFailure(call: Call<ArrayList<Any>>, t: Throwable) {
                t.printStackTrace()
                countryList.postValue(countryDao.getAllCountries().map { Country(it) })
            }
        })
    }
}
