package com.example.mygithubrepo.domain.datasource

import com.example.mygithubrepo.data.dto.Items
import com.example.mygithubrepo.data.dto.RemoteUserDetails


/*
 * Interface for DataSource
 * could be local or remote
 */
interface RepoDataSource{
    suspend fun getRepos(apiQuery: String, position: Int, loadSize: Int): List<Items>
    suspend fun getUserDetails(userName: String): RemoteUserDetails
}