package top.m7641.sms.forwarder.entity.result

data class PushplusResult(
    var code: Long,
    var msg: String,
    var data: String?,
    var count: Long?,
)