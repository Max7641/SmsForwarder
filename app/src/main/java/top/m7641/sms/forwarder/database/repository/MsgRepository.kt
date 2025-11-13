package top.m7641.sms.forwarder.database.repository

import androidx.annotation.WorkerThread
import top.m7641.sms.forwarder.database.dao.MsgDao
import top.m7641.sms.forwarder.database.entity.Msg

class MsgRepository(private val msgDao: MsgDao) {

    @WorkerThread
    suspend fun insert(msg: Msg): Long = msgDao.insert(msg)

    @WorkerThread
    fun delete(id: Long) = msgDao.delete(id)

    fun deleteAll() = msgDao.deleteAll()

    @WorkerThread
    fun deleteTimeAgo(time: Long) = msgDao.deleteTimeAgo(time)

}