package com.example.mygithubrepo.domain.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.mygithubrepo.domain.entity.RepoItems
import com.example.mygithubrepo.data.datastore.network.api.GitHubAPIService
import com.example.mygithubrepo.data.datastore.network.cons.MyCons
import com.example.mygithubrepo.data.dto.asItems
import retrofit2.HttpException
import java.io.IOException

class ReposPagingSource (private val apiQuery:String,private val remoteDatasource: RepoDataSource):
    PagingSource<Int, RepoItems>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepoItems> {
        val position = params.key ?: MyCons.GITHUB_STARTING_PAGE_INDEX
        return try {
            val users = remoteDatasource.getRepos(apiQuery, position, params.loadSize).asItems()
            val nextKey = if (users.isEmpty()) {
                null
            } else {
                // initial load size = 3 * NETWORK_PAGE_SIZE
                // ensure we're not requesting duplicating items, at the 2nd request
                position + (params.loadSize / MyCons.NETWORK_PAGE_SIZE)
            }
            LoadResult.Page(
                data = users,
                prevKey = if (position == MyCons.GITHUB_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    /**
     * To force refresh from start(as per spec)  returing null
     */
    override fun getRefreshKey(state: PagingState<Int, RepoItems>): Int? {
        return null
    }
}