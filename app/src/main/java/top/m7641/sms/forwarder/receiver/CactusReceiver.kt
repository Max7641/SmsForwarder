package top.m7641.sms.forwarder.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import top.m7641.sms.forwarder.utils.Log
import com.gyf.cactus.Cactus
import top.m7641.sms.forwarder.App

//接收Cactus广播
class CactusReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        intent.action?.apply {
            when (this) {
                Cactus.CACTUS_WORK -> {
                    Log.d(
                        App.TAG,
                        this + "--" + intent.getIntExtra(Cactus.CACTUS_TIMES, 0)
                    )
                }
                Cactus.CACTUS_STOP -> {
                    Log.d(App.TAG, this)
                }
                Cactus.CACTUS_BACKGROUND -> {
                    Log.d(App.TAG, this)
                }
                Cactus.CACTUS_FOREGROUND -> {
                    Log.d(App.TAG, this)
                }
            }
        }
    }
}