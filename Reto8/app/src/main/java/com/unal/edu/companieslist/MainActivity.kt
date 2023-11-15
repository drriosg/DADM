package com.unal.edu.companieslist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.unal.edu.companieslist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: CompanyDatabaseHelper
    private lateinit var companiesAdapter: CompaniesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = CompanyDatabaseHelper(this)
        companiesAdapter = CompaniesAdapter(db.getAllCompanies(), this)

        binding.CompaniesReciclerView.layoutManager = LinearLayoutManager(this)
        binding.CompaniesReciclerView.adapter = companiesAdapter

        binding.addButton.setOnClickListener{
            val intent = Intent(this, AddCompanieActivity::class.java)
            startActivity(intent)
        }
    }

    override  fun onResume(){
        super.onResume()
        companiesAdapter.refreshData(db.getAllCompanies())
    }
}