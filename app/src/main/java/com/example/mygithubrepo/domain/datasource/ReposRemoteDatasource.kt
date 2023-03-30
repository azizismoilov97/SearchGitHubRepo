package com.example.mygithubrepo.domain.datasource

import com.example.mygithubrepo.data.dto.Items
import com.example.mygithubrepo.data.dto.RemoteUserDetails
import com.example.mygithubrepo.data.dto.Repos
import com.example.mygithubrepo.data.datastore.network.api.GitHubAPIService
import javax.inject.Inject


/**
 * Ideally  split the paging and remote data source
 */
class  ReposRemoteDatasource @Inject constructor(
    private val gitHubApiService: GitHubAPIService
    ): RepoDataSource {

    override suspend fun getRepos(apiQuery:String, position:Int, loadSize:Int): List<Items> {
       val remotePostsResponse : Repos =
           gitHubApiService.getRepos(apiQuery, position, loadSize)
       return remotePostsResponse.items
    }

    override suspend fun getUserDetails(userName: String): RemoteUserDetails {
        return gitHubApiService.getUserDetail(userName)
    }

}
