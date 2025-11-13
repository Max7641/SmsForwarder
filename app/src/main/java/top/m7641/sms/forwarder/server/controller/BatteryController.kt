package top.m7641.sms.forwarder.server.controller

import android.content.Intent
import android.content.IntentFilter
import top.m7641.sms.forwarder.utils.Log
import top.m7641.sms.forwarder.App
import top.m7641.sms.forwarder.entity.BatteryInfo
import top.m7641.sms.forwarder.server.model.BaseRequest
import top.m7641.sms.forwarder.server.model.EmptyData
import top.m7641.sms.forwarder.utils.BatteryUtils
import com.yanzhenjie.andserver.annotation.*

@Suppress("PrivatePropertyName")
@RestController
@RequestMapping(path = ["/battery"])
class BatteryController {

    private val TAG: String = BatteryController::class.java.simpleName

    //远程查电量
    @CrossOrigin(methods = [RequestMethod.POST])
    @PostMapping("/query")
    fun query(@RequestBody bean: BaseRequest<EmptyData>): BatteryInfo {
        Log.d(TAG, bean.data.toString())

        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val intent: Intent? = App.context.registerReceiver(null, intentFilter)
        return BatteryUtils.getBatteryInfo(intent)
    }

}