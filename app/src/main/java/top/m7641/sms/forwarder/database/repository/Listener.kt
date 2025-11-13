package top.m7641.sms.forwarder.database.repository

interface Listener {
    fun onDelete(id: Long)
}