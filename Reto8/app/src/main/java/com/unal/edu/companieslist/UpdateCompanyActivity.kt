package com.unal.edu.companieslist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.unal.edu.companieslist.databinding.ActivityUpdateCompanieBinding

class UpdateCompanyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateCompanieBinding
    private lateinit var db: CompanyDatabaseHelper
    private var companyId: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateCompanieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = CompanyDatabaseHelper(this)
        companyId = intent.getIntExtra("company_id", -1)
        if(companyId == -1){
            finish()
            return
        }

        val company = db.getCompanyById(companyId)
        binding.updateCompanyName.setText(company.name)
        binding.UpdateWebPageUri.setText(company.uri)
        binding.UpdateContactPhoneNumber.setText(company.phone.toString())
        binding.UpdateContactEmail.setText(company.email)
        binding.UpdatePodAndServ.setText(company.prodAndServ)
        binding.UpdateClassification.setText(company.classification)

        binding.updateButton.setOnClickListener{
            var newName = binding.updateCompanyName.toString()
            var newUri = binding.UpdateWebPageUri.toString()
            var newPhone = binding.UpdateContactPhoneNumber.toString().toInt()
            var newEmail = binding.UpdateContactEmail.toString()
            var newProdAndServ = binding.UpdatePodAndServ.toString()
            var newClassification = binding.UpdateClassification.toString()
            var updateCompany = Company(
                companyId,
                newName,
                newUri,
                newPhone,
                newEmail,
                newProdAndServ,
                newClassification
            )
            db.updateCompany(updateCompany)
            finish()
            Toast.makeText(this, "Cambios Guardados", Toast.LENGTH_SHORT).show()
        }
    }
}