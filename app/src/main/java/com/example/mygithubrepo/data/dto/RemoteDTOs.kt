package com.example.mygithubrepo.data.dto


import com.example.mygithubrepo.domain.entity.AppUserDetails
import com.example.mygithubrepo.domain.entity.RepoItems
import com.google.gson.annotations.SerializedName


/*
 *This file consists of all remote data
 * that will be transmuted to app data
 */



data class RemoteUserDetails(
        val name: String,
        val avatar_url: String?,
        val company: String?,
        val blog: String?,
        val location: String?,
        val email: String?,
        val bio: String?,
        val twitter_username: String?,
        val followers: Long,
        val following: Long,
        val created_at: String
)


data class Repos (
        @SerializedName("total_count")var totalCount : Int,
        @SerializedName("incomplete_results")var incompleteResults : Boolean,
        @SerializedName("items")var items : List<Items>
)

fun List<Items>.asItems():List<RepoItems>{
        return  map {
                RepoItems(
                        id = it.id,
                        userName = it.owner.login,
                        repoName = it.name,
                        language = it.language,
                        starCount = it.stargazersCount.toString(),
                        htmlUrl = it.htmlUrl,
                        avatarUrl = it.owner.avatarUrl
                )
        }
}


fun RemoteUserDetails.asUserDetailAppModel() = AppUserDetails(
        name = name,
        email = email,
        twitter = twitter_username,
        bio = bio,
        location = location,
        followers = followers,
        following = following,
        organisation = company,
        avatarUrl = avatar_url
)


data class Items (
        var id : Int,
        var nodeId : String,
        var name : String,
        var fullName : String,
        var private : Boolean,
        var owner : Owner,
        @SerializedName("html_url") var htmlUrl : String,
        var description : String,
        var fork : Boolean,
        var url : String,
        var forksUrl : String,
        var keysUrl : String,
        var collaboratorsUrl : String,
        var teamsUrl : String,
        var hooksUrl : String,
        var issueEventsUrl : String,
        var eventsUrl : String,
        var assigneesUrl : String,
        var branchesUrl : String,
        var tagsUrl : String,
        var blobsUrl : String,
        var gitTagsUrl : String,
        var gitRefsUrl : String,
        var treesUrl : String,
        var statusesUrl : String,
        var languagesUrl : String,
        var stargazersUrl : String,
        var contributorsUrl : String,
        var subscribersUrl : String,
        var subscriptionUrl : String,
        var commitsUrl : String,
        var gitCommitsUrl : String,
        var commentsUrl : String,
        var issueCommentUrl : String,
        var contentsUrl : String,
        var compareUrl : String,
        var mergesUrl : String,
        var archiveUrl : String,
        var downloadsUrl : String,
        var issuesUrl : String,
        var pullsUrl : String,
        var milestonesUrl : String,
        var notificationsUrl : String,
        var labelsUrl : String,
        var releasesUrl : String,
        var deploymentsUrl : String,
        var createdAt : String,
        var updatedAt : String,
        var pushedAt : String,
        var gitUrl : String,
        var sshUrl : String,
        var cloneUrl : String,
        var svnUrl : String,
        var homepage : String,
        var size : Int,
        @SerializedName("stargazers_count") var stargazersCount : Int,
        @SerializedName("watchers_count")var watchersCount : Int,
        @SerializedName("language")var language : String,
        var hasIssues : Boolean,
        var hasProjects : Boolean,
        var hasDownloads : Boolean,
        var hasWiki : Boolean,
        var hasPages : Boolean,
        var forksCount : Int,
        var mirrorUrl : String,
        var archived : Boolean,
        var disabled : Boolean,
        var openIssuesCount : Int,
        var license : License,
        var forks : Int,
        var openIssues : Int,
        var watchers : Int,
        var defaultBranch : String,
        var score : Int

)

data class License (

        var key : String,
        var name : String,
        var spdxId : String,
        var url : String,
        var nodeId : String

)

data class Owner (

        @SerializedName("login") var login : String,
        var id : Int,
        var nodeId : String,
        @SerializedName("avatar_url")var avatarUrl : String,
        var gravatarId : String,
        var url : String,
        var htmlUrl : String,
        @SerializedName("followers_url") var followersUrl : String,
        @SerializedName("following_url") var followingUrl : String,
        var gistsUrl : String,
        var starredUrl : String,
        var subscriptionsUrl : String,
        var organizationsUrl : String,
        var reposUrl : String,
        var eventsUrl : String,
        var receivedEventsUrl : String,
        var type : String,
        var siteAdmin : Boolean

)