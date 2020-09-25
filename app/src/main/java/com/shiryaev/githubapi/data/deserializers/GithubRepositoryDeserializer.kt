package com.shiryaev.githubapi.data.deserializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.shiryaev.githubapi.domain.RepositoryData
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

class GithubRepositoryDeserializer : JsonDeserializer<Array<RepositoryData>>
{
    override fun deserialize(json: JsonElement?,
                             typeOfT: Type?,
                             context: JsonDeserializationContext?): Array<RepositoryData>?
    {
        val result = arrayListOf<RepositoryData>()

        if (json != null)
        {
            val obj = json.asJsonObject

            if (obj.has("message"))
                return null

            val totalCount = obj.get("total_count").asInt

            val items = obj.getAsJsonArray("items")

            if (items.size() == 0 || totalCount == 0)
                return arrayOf()

            items.forEach {
                val repoObj = it.asJsonObject
                val id = repoObj.get("id").asLong
                val title = repoObj.get("name").asString
                val owner = repoObj.get("owner").asJsonObject
                val ownerName = owner.get("login").asString
                val ownerAvatarUrl = owner.get("avatar_url").asString
                val desc = repoObj.get("description")
                val finalDesc = if (desc.isJsonNull) "" else desc.asString
                val forksCount = repoObj.get("forks_count").asInt
                val starsCount = repoObj.get("stargazers_count").asInt
                val date = parseDate(repoObj.get("created_at").asString)
                val repo = RepositoryData(id, title, ownerName, ownerAvatarUrl,
                    finalDesc, forksCount, starsCount, date)
                result.add(repo)
            }
        }

        return result.toTypedArray()
    }

    private fun parseDate(date: String): String
    {
        val inputFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val outputFormatter = SimpleDateFormat("dd-MM-yyyy hh:mm")
        val parsed = inputFormatter.parse(date)

        return outputFormatter.format(parsed)
    }
}