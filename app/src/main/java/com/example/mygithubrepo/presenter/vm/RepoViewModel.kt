package com.example.mygithubrepo.presenter.vm

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mygithubrepo.domain.repository.RepoRepository
import com.example.mygithubrepo.domain.entity.AppUserDetails
import com.example.mygithubrepo.domain.entity.RepoItems
import com.example.mygithubrepo.common.SearchResult
import javax.inject.Inject
import javax.inject.Provider

class RepoViewModel @Inject constructor(

    private val repoRepository: RepoRepository

    ) : ViewModel() {

    fun pagingDataFlow(apiQuery:String): LiveData<PagingData<RepoItems>> =
        repoRepository.getRepoResultStream(apiQuery).cachedIn(viewModelScope).asLiveData()

}


class ViewModelFactory @Inject constructor(
    repoViewModelProvider: Provider<RepoViewModel>
) : ViewModelProvider.Factory {

    private val providers = mapOf<Class<*>, Provider<out ViewModel>>(
        RepoViewModel::class.java to repoViewModelProvider
    )

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return providers[modelClass]!!.get() as T
    }
}