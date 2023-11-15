package com.unal.edu.companieslist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.unal.edu.companieslist.databinding.ActivityAddCompanieBinding
import com.unal.edu.companieslist.databinding.ActivityMainBinding
import java.lang.Integer.parseInt

class AddCompanieActivity : AppCompatActivity() {

    private  lateinit var binding: ActivityAddCompanieBinding
    private  lateinit var db: CompanyDatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCompanieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = CompanyDatabaseHelper(this)
        binding.saveButton.setOnClickListener{
            val name = binding.companyName.text.toString()
            val uri = binding.webPageUri.text.toString()
            val phone = binding.contactPhoneNumber.text.toString().toInt()
            val email = binding.contactEmail.text.toString()
            val prodandserv = binding.prodAndServ.text.toString()
            val classification = binding.classification.text.toString()
            val company = Company(
                0,
                name,
                uri,
                phone,
                email,
                prodandserv,
                classification
            )
            db.insertCompany(company)
            finish()
            Toast.makeText(this, "Empresa Guardada", Toast.LENGTH_SHORT).show()
        }
    }
}