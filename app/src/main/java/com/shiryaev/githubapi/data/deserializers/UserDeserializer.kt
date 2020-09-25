package com.shiryaev.githubapi.data.deserializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.shiryaev.githubapi.domain.User
import java.lang.reflect.Type

class UserDeserializer : JsonDeserializer<User>
{
    override fun deserialize(json: JsonElement?,
                             typeOfT: Type?,
                             context: JsonDeserializationContext?): User?
    {
        if (json != null)
        {
            val userJson = json.asJsonObject
            val login = userJson.get("login").asString

            return User(null, login, null, null)
        }

        return null
    }

}