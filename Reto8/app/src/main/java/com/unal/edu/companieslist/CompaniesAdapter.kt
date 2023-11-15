package com.unal.edu.companieslist

import android.content.Context
import android.content.Intent
import android.view.CollapsibleActionView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class CompaniesAdapter(private var companies: List<Company>, context: Context) :
    RecyclerView.Adapter<CompaniesAdapter.CompanyViewHolder>() {

    private val db: CompanyDatabaseHelper = CompanyDatabaseHelper(context)
    class CompanyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val titleTexView: TextView = itemView.findViewById(R.id.titleTextView)
        val uriTextView: TextView = itemView.findViewById(R.id.uriTextView)
        val phoneTextView: TextView = itemView.findViewById(R.id.phoneTextView)
        val emailTextView: TextView = itemView.findViewById(R.id.emailTextView)
        val prodandservTextView: TextView = itemView.findViewById(R.id.uprodandservTextView)
        val classificationTextView: TextView = itemView.findViewById(R.id.classificationTextView)
        val updateButton: ImageView = itemView.findViewById(R.id.updateButton)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.companie_item, parent, false)
        return CompanyViewHolder(view)
    }

    override fun getItemCount(): Int = companies.size

    override fun onBindViewHolder(holder: CompanyViewHolder, position: Int) {
        val company = companies[position]
        holder.titleTexView.text = company.name
        holder.uriTextView.text = company.uri
        holder.phoneTextView.text = company.phone.toString()
        holder.emailTextView.text = company.email
        holder.prodandservTextView.text = company.prodAndServ
        holder.classificationTextView.text = company.classification
        holder.updateButton.setOnClickListener{
            val intent = Intent(holder.itemView.context, UpdateCompanyActivity::class.java).apply {
                putExtra("company_id", company.id)
            }
            holder.itemView.context.startActivity(intent)
        }
        holder.deleteButton.setOnClickListener{
            db.deleteCompany(company.id)
            refreshData(db.getAllCompanies())
            Toast.makeText(holder.itemView.context, "Compañia Borrada", Toast.LENGTH_SHORT).show()
        }
    }

    fun refreshData(newCompanies: List<Company>){
        companies = newCompanies
        notifyDataSetChanged()
    }
}