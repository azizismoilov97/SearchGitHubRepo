package com.example.mygithubrepo.domain.repository

import androidx.paging.PagingData
import com.example.mygithubrepo.domain.entity.AppUserDetails
import com.example.mygithubrepo.domain.entity.RepoItems
import com.example.mygithubrepo.common.SearchResult
import kotlinx.coroutines.flow.Flow


/*
 * Interface for data layer
 */
interface RepoRepository {
    fun getRepoResultStream(apiQuery:String): Flow<PagingData<RepoItems>>
    suspend fun getUserDetails(userName: String): SearchResult<AppUserDetails>
}