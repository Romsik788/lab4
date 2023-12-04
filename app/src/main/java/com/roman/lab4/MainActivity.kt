package com.roman.lab4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.roman.lab4.database.CountryAdapter
import com.roman.lab4.database.CountryDatabase
import com.roman.lab4.databinding.ActivityMainBinding
import com.roman.lab4.viewmodel.MainViewModel

class MainActivity : AppCompatActivity(), CountryAdapter.OnCountryItemClickListener {

    private lateinit var countryAdapter: CountryAdapter
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        countryAdapter = CountryAdapter(this)
        binding.recyclerView.adapter = countryAdapter

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.getCountryList().observe(this) {
            it?.let { countries -> countryAdapter.setData(countries) }
        }

        fetchCountries()
    }

    private fun fetchCountries() {
        val cDao = CountryDatabase.getInstance(applicationContext).countryDao()
        viewModel.fetchCountries(cDao)
    }

    override fun onCountryItemClick(position: Int) {
        val intent = Intent(this, CountryInfoActivity::class.java)
        val selectedCountryName = countryAdapter.currentList[position].name
        intent.putExtra("selectedCountryName", selectedCountryName)
        startActivity(intent)
    }
}