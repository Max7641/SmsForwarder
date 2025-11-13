package top.m7641.sms.forwarder.database.ext

import androidx.room.TypeConverter
import top.m7641.sms.forwarder.core.Core
import top.m7641.sms.forwarder.database.entity.Sender

class ConvertersSenderList {

    @TypeConverter
    fun stringToObject(value: String): List<Sender> {
        return Core.sender.getByIds(value.split(",").map { it.trim().toLong() }, value)
    }

    @TypeConverter
    fun objectToString(list: List<Sender>): String {
        return list.joinToString(",") { it.id.toString() }
    }
}