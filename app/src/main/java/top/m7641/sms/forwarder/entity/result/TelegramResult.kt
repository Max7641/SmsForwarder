package top.m7641.sms.forwarder.entity.result

data class TelegramResult(
    var ok: Boolean?,
    var message: String,
    var timestamp: Long?,
)