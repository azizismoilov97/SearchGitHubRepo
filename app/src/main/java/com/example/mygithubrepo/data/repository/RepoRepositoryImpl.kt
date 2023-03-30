package com.example.mygithubrepo.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.mygithubrepo.domain.entity.AppUserDetails
import com.example.mygithubrepo.domain.entity.RepoItems
import com.example.mygithubrepo.common.SearchResult
import com.example.mygithubrepo.domain.datasource.RepoDataSource
import com.example.mygithubrepo.domain.datasource.ReposPagingSource
import com.example.mygithubrepo.data.datastore.network.cons.MyCons.NETWORK_PAGE_SIZE
import com.example.mygithubrepo.data.dto.asUserDetailAppModel
import com.example.mygithubrepo.domain.repository.RepoRepository


import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepoRepositoryImpl @Inject constructor (
    private val remoteDataSource: RepoDataSource,
    private val ioDispatcher: CoroutineDispatcher
) : RepoRepository {


    override fun getRepoResultStream(apiQuery:String): Flow<PagingData<RepoItems>> {
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { ReposPagingSource(apiQuery,remoteDataSource) }
        ).flow
    }

    override suspend fun getUserDetails(userName: String): SearchResult<AppUserDetails>
    = withContext(ioDispatcher){
        try {
            val data = remoteDataSource.getUserDetails(userName).asUserDetailAppModel()
            SearchResult.Success(data)
        }catch (ex: Exception){
            SearchResult.Error(ex)
        }

    }
}

