package com.hasanali.kotlincountries.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import com.hasanali.kotlincountries.R
import com.hasanali.kotlincountries.databinding.FragmentCountryBinding
import com.hasanali.kotlincountries.util.downloadFromUrl
import com.hasanali.kotlincountries.util.placeHolderProgressBar
import com.hasanali.kotlincountries.viewmodel.CountryViewModel

class CountryFragment : Fragment() {

    private lateinit var viewModel: CountryViewModel
    private lateinit var countryImage: ImageView
    private lateinit var countryName: TextView
    private lateinit var countryCapital: TextView
    private lateinit var countryRegion: TextView
    private lateinit var countryCurrency: TextView
    private lateinit var countryLanguage: TextView

    private lateinit var view: FragmentCountryBinding

    private var uuidFromArgs: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = DataBindingUtil.inflate(inflater,R.layout.fragment_country,container,false)
        return view.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        countryImage = view.findViewById(R.id.countryImage)
        countryName = view.findViewById(R.id.countryName)
        countryCapital = view.findViewById(R.id.countryCapital)
        countryRegion = view.findViewById(R.id.countryRegion)
        countryCurrency = view.findViewById(R.id.countryCurrency)
        countryLanguage = view.findViewById(R.id.countryLanguage)

        arguments?.let {
            uuidFromArgs = CountryFragmentArgs.fromBundle(it).countryUuid
        }

        viewModel = ViewModelProviders.of(this).get(CountryViewModel::class.java)
        viewModel.getDataFromRoom(uuidFromArgs)

        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.countryLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                view.country = it
                /*
                countryName.text = it.countryName
                countryCapital.text = it.countryCapital
                countryRegion.text = it.countryRegion
                countryCurrency.text = it.countryCurrency
                countryLanguage.text = it.countryLanguage
                context?.let { context ->
                    countryImage.downloadFromUrl(it.countryImageUrl, placeHolderProgressBar(context))
                }

                 */
            }
        })
    }

}