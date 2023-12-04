package com.roman.lab4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.roman.lab4.database.CountryDatabase
import com.roman.lab4.databinding.ActivityCountryInfoBinding
import com.roman.lab4.viewmodel.CountryInfoViewModel

class CountryInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCountryInfoBinding
    private lateinit var viewModel: CountryInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountryInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val countryName = intent.getStringExtra("selectedCountryName") ?: ""

        viewModel = ViewModelProvider(this)[CountryInfoViewModel::class.java]
        val cDao = CountryDatabase.getInstance(applicationContext).countryDao()

        viewModel.getCountryDetail().observe(this) { country ->
            country?.let {
                with(binding) {
                    name.text = it.name
                    capital.text = it.capital
                    code.text = it.code
                    incomeLevel.text = it.incomeLevel?.get("value")
                }
            }
        }
        viewModel.fetchCountryByNameAndCountry(countryName, cDao)
    }
}