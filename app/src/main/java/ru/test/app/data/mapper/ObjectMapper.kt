package ru.test.app.data.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.test.app.data.mapper.ObjectMapper.convert

object ObjectMapper {
    val gson = Gson()

    inline fun <I, reified O> I.convert(): O {
        val json = gson.toJson(this)
        return gson.fromJson(json, object : TypeToken<O>() {}.type)
    }
}

fun <T> T.serializeToMap(): Map<String, Any> {
    return convert()
}