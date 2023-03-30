package com.example.mygithubrepo.presenter.screen.fragment

import android.graphics.Color
import android.view.*
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.mygithubrepo.common.Utils
import com.example.mygithubrepo.common.base.BaseFragment
import com.example.mygithubrepo.common.getAppComponent
import com.example.mygithubrepo.common.getRetryTime
import com.example.mygithubrepo.databinding.UserListFragmentBinding
import com.example.mygithubrepo.presenter.screen.adapter.RepoListAdapter
import com.example.mygithubrepo.presenter.vm.RepoViewModel
import com.example.mygithubrepo.presenter.screen.adapter.RepoLoadStateAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import retrofit2.HttpException


class RepoListFragment : BaseFragment<UserListFragmentBinding>() {

   private lateinit var listAdapter: RepoListAdapter


    private val viewModel: RepoViewModel by activityViewModels{
        getAppComponent().viewModelsFactory()
    }

    override fun getViewBinding(): UserListFragmentBinding = UserListFragmentBinding.inflate(layoutInflater)

    override fun setObserver() {
        listAdapter= RepoListAdapter(requireContext())

        listAdapter.loadStateFlow.asLiveData().observe(viewLifecycleOwner) { loadState ->
            val isListEmpty =
                loadState.refresh is LoadState.NotLoading && listAdapter.itemCount == 0
            // show empty list
            binding.emptyList.isVisible = isListEmpty
            // Only show the list if refresh succeeds.
            binding.listView.isVisible = !isListEmpty
            // Show loading spinner during initial load or refresh.
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            // Show the retry state if initial load or refresh fails.
//            binding.retryButton.isVisible = loadState.source.refresh is LoadState.Error


            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                val ex = errorState.error
                if (ex is HttpException && ex.code() == 403) {
                    ex.getRetryTime()?.let {
                        Snackbar.make(
                            binding.listView, "Rate limit exceeded please try at $it ",
                            Snackbar.LENGTH_INDEFINITE
                        )
                            .setAction("OK") {}  // action text on the right side
                            .setActionTextColor(Color.GREEN)
                            .show();
                    }
                } else {
                    ex.let {
                        Toast.makeText(
                            requireContext(),
                            ex.localizedMessage,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }


    override fun setupViews() {

        if(!Utils.isNetworkAvailable(requireContext())) Utils.showNetworkError(requireContext())



        binding.topButton.setOnClickListener {
            setListToTop()
        }

        binding.listView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    //Scrolling down
                    binding.topButton.visibility = View.VISIBLE

                } else if (dy < 0) {
                    //Scrolling up
                    binding.topButton.visibility = View.GONE
                }
            }
        })


        binding.suggestQuery.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                if (query != null) {
                    binding.suggestQuery.clearFocus()
                    setQuery(query)

                }
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {

                return true
            }

        })



        binding.swipeContainer.setDistanceToTriggerSync(SwipeRefreshLayout.LARGE)
        binding.swipeContainer.setOnRefreshListener {
            binding.swipeContainer.isRefreshing = false
            listAdapter.refresh() //Launches  new paging
            if(!Utils.isNetworkAvailable(requireContext())) {
                Utils.showNetworkError(requireContext())

            }
        }

        binding.listView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        binding.listView.adapter = listAdapter.withLoadStateFooter(
            footer = RepoLoadStateAdapter { listAdapter.retry() }
        )



    }

    private fun setQuery(query:String){

        viewModel.pagingDataFlow(query).observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                listAdapter.submitData(it)
                binding.progressBar.visibility = View.GONE //Let subscriber count load without progress bar
            }
        }
    }


    private fun setListToTop(){
       binding.listView.smoothScrollToPosition(0)
   }

}

