package com.shiryaev.githubapi.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "repositories_table")
data class RepositoryData(
    @PrimaryKey
    val id: Long,
    val title: String,
    val authorLogin: String,
    val authorAvatarUrl: String,
    val description: String,
    val forkCount: Int,
    val starCount: Int,
    val date: String,
) : Serializable