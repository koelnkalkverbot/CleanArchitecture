package de.jenswangenheim.cleanarchitecture.view

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import de.jenswangenheim.cleanarchitecture.R
import de.jenswangenheim.cleanarchitecture.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment() {

    private lateinit var viewModel: ListViewModel
    private val dogsListAdapter = DogListAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        viewModel.refresh()

        itemsList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = dogsListAdapter
        }
        listSwipeRefresh.setOnRefreshListener {
            itemsList.visibility = View.GONE
            listErrorTv.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
            viewModel.refresh()
            listSwipeRefresh.isRefreshing = false
        }
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.dogs.observe(this, Observer {dogs ->
            dogs.let {
                itemsList.visibility = View.VISIBLE
                dogsListAdapter.updateItemsList(dogs)
            }
        })

        viewModel.dogsLoadError.observe(this, Observer {isError ->
            isError.let {
                if (isError) {
                    listErrorTv.visibility = View.VISIBLE
                    progressBar.visibility = View.INVISIBLE
                    itemsList.visibility = View.INVISIBLE
                } else {
                    progressBar.visibility = View.INVISIBLE
                    listErrorTv.visibility = View.INVISIBLE
                    itemsList.visibility = View.VISIBLE
                }
            }
        })

        viewModel.loading.observe(this, Observer {isLoading ->
            isLoading.let {
                if (isLoading) {
                    progressBar.visibility = View.VISIBLE
                    listErrorTv.visibility = View.INVISIBLE
                    itemsList.visibility = View.INVISIBLE
                } else {
                    progressBar.visibility = View.INVISIBLE
                    listErrorTv.visibility = View.INVISIBLE
                    itemsList.visibility = View.VISIBLE
                }
            }
        })
    }
}