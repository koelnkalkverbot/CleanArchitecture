package de.jenswangenheim.cleanarchitecture.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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
        setHasOptionsMenu(true)
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
            viewModel.forceRefresh()
            listSwipeRefresh.isRefreshing = false
        }
        observeViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                val action = ListFragmentDirections.actionListFragmentToSettingsFragment()
                view?.let {
                    Navigation.findNavController(it).navigate(action)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun observeViewModel() {
        viewModel.dogs.observe(this, Observer { dogs ->
            dogs.let {
                itemsList.visibility = View.VISIBLE
                progressBar.visibility = View.INVISIBLE
                listErrorTv.visibility = View.INVISIBLE
                dogsListAdapter.updateItemsList(dogs)
            }
        })

        viewModel.dogsLoadError.observe(this, Observer { isError ->
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

        viewModel.loading.observe(this, Observer { isLoading ->
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