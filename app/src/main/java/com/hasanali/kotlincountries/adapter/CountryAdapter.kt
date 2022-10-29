package com.hasanali.kotlincountries.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.RecyclerView
import com.hasanali.kotlincountries.R
import com.hasanali.kotlincountries.databinding.ItemCountryBinding
import com.hasanali.kotlincountries.model.Country
import com.hasanali.kotlincountries.util.downloadFromUrl
import com.hasanali.kotlincountries.util.placeHolderProgressBar
import com.hasanali.kotlincountries.view.CountryFragmentArgs
import com.hasanali.kotlincountries.view.FeedFragmentDirections

class CountryAdapter(val countryList: ArrayList<Country>): RecyclerView.Adapter<CountryAdapter.CountryViewHolder>(), CountryClickedListener {

    override fun onCountryClicked(v: View) {
        val uuid = v.findViewById<TextView>(R.id.textViewUuid).text.toString().toInt()
        Navigation.findNavController(v).navigate(FeedFragmentDirections.actionFeedFragmentToCountryFragment(uuid))
    }

    class CountryViewHolder(var view: ItemCountryBinding) : RecyclerView.ViewHolder(view.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        // val view = inflater.inflate(R.layout.item_country,parent,false)
        val view = DataBindingUtil.inflate<ItemCountryBinding>(inflater,R.layout.item_country,parent,false)
        return CountryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {

        holder.view.country = countryList[position]
        holder.view.listener = this

        /*
        val nameText = holder.itemView.findViewById<TextView>(R.id.name)
        val regionText = holder.itemView.findViewById<TextView>(R.id.region)
        val imageView = holder.itemView.findViewById<ImageView>(R.id.imageView)

        nameText.text = countryList[position].countryName
        regionText.text = countryList[position].countryRegion

        imageView.downloadFromUrl(countryList[position].countryImageUrl, placeHolderProgressBar(holder.itemView.context))

        holder.itemView.setOnClickListener {
            Navigation.findNavController(it).navigate(FeedFragmentDirections.actionFeedFragmentToCountryFragment(countryList[position].uuid))
        }

         */
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    fun updateCountryList(newCountryList: List<Country>) {
        countryList.clear()
        countryList.addAll(newCountryList)
        notifyDataSetChanged()
    }
}