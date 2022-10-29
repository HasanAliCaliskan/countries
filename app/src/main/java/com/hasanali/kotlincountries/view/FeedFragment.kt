package com.hasanali.kotlincountries.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hasanali.kotlincountries.R
import com.hasanali.kotlincountries.adapter.CountryAdapter
import com.hasanali.kotlincountries.service.CountryDatabase
import com.hasanali.kotlincountries.viewmodel.FeedViewModel

class FeedFragment : Fragment() {

    private lateinit var viewModel: FeedViewModel
    private val countryAdapter = CountryAdapter(arrayListOf())
    private lateinit var recyclerView: RecyclerView
    private lateinit var countryError: TextView
    private lateinit var countryProgressBar: ProgressBar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)
        countryError = view.findViewById(R.id.countryError)
        countryProgressBar = view.findViewById(R.id.countryProgressBar)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)

        viewModel = ViewModelProviders.of(this).get(FeedViewModel::class.java)
        viewModel.refreshData()

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = countryAdapter

        swipeRefreshLayout.setOnRefreshListener {
            recyclerView.visibility = View.GONE
            countryError.visibility = View.GONE
            countryProgressBar.visibility = View.VISIBLE
            swipeRefreshLayout.isRefreshing = false
            viewModel.refreshFromAPI()
        }

        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.countries.observe(viewLifecycleOwner, Observer {
            it?.let {
                recyclerView.visibility = View.VISIBLE
                countryAdapter.updateCountryList(it)
            }
        })

        viewModel.countryError.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it) {
                    countryError.visibility = View.VISIBLE
                } else {
                    countryError.visibility = View.GONE
                }
            }
        })

        viewModel.countryLoading.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it) {
                    countryProgressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                    countryError.visibility = View.GONE
                } else {
                    countryProgressBar.visibility = View.GONE
                }
            }
        })
    }
}