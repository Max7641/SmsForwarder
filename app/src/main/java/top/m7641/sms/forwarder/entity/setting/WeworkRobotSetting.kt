package top.m7641.sms.forwarder.entity.setting

import top.m7641.sms.forwarder.R
import java.io.Serializable

data class WeworkRobotSetting(
    var webHook: String = "",
    val msgType: String = "text",
    var atAll: Boolean = false,
    var atUserIds: String = "",
    var atMobiles: String = "",
) : Serializable {

    fun getMsgTypeCheckId(): Int {
        return if (msgType == "markdown") {
            R.id.rb_msg_type_markdown
        } else {
            R.id.rb_msg_type_text
        }
    }
}