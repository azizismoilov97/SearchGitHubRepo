package com.example.mygithubrepo.domain.entity
/*
 * This class contains all the models
 * that will be used by the app UI
 */

data class RepoItems(
        val id: Int,
        val userName:String?,
        val repoName: String,
        val language: String?,
        val starCount:String?,
        val htmlUrl:String?,
        val avatarUrl: String?
)

data class AppUserDetails (
        val name: String,
        val email: String?,
        val twitter: String?,
        val organisation: String?,
        val bio: String?,
        val followers: Long,
        val following: Long,
        val avatarUrl: String?,
        val location: String?
)

