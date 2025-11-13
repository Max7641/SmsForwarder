package top.m7641.sms.forwarder.entity.result

data class BarkResult(
    var code: Long,
    var message: String,
    var timestamp: Long?,
)