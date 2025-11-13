package top.m7641.sms.forwarder.entity.setting

import top.m7641.sms.forwarder.R
import java.io.Serializable

data class SmsSetting(
    var simSlot: Int = 0,
    var mobiles: String = "",
    var onlyNoNetwork: Boolean = false,
) : Serializable {

    fun getSmsSimSlotCheckId(): Int {
        return when (simSlot) {
            1 -> R.id.rb_sim_slot_1
            2 -> R.id.rb_sim_slot_2
            else -> R.id.rb_sim_slot_org
        }
    }
}