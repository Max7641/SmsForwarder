package top.m7641.sms.forwarder.server.controller

import android.annotation.SuppressLint
import top.m7641.sms.forwarder.entity.LocationInfo
import top.m7641.sms.forwarder.server.model.BaseRequest
import top.m7641.sms.forwarder.server.model.EmptyData
import top.m7641.sms.forwarder.utils.HttpServerUtils
import top.m7641.sms.forwarder.utils.Log
import com.yanzhenjie.andserver.annotation.CrossOrigin
import com.yanzhenjie.andserver.annotation.PostMapping
import com.yanzhenjie.andserver.annotation.RequestBody
import com.yanzhenjie.andserver.annotation.RequestMapping
import com.yanzhenjie.andserver.annotation.RequestMethod
import com.yanzhenjie.andserver.annotation.RestController

@SuppressLint("SimpleDateFormat")
@Suppress("PrivatePropertyName")
@RestController
@RequestMapping(path = ["/location"])
class LocationController {

    private val TAG: String = LocationController::class.java.simpleName

    //远程找手机
    @CrossOrigin(methods = [RequestMethod.POST])
    @PostMapping("/query")
    fun query(@RequestBody bean: BaseRequest<EmptyData>): LocationInfo {
        Log.d(TAG, bean.data.toString())
        return HttpServerUtils.apiLocationCache
    }

}