package top.m7641.sms.forwarder.core

import android.app.Application
import androidx.work.Configuration
import top.m7641.sms.forwarder.App
import top.m7641.sms.forwarder.BuildConfig
import top.m7641.sms.forwarder.database.repository.FrpcRepository
import top.m7641.sms.forwarder.database.repository.LogsRepository
import top.m7641.sms.forwarder.database.repository.MsgRepository
import top.m7641.sms.forwarder.database.repository.RuleRepository
import top.m7641.sms.forwarder.database.repository.SenderRepository
import top.m7641.sms.forwarder.database.repository.TaskRepository
import top.m7641.sms.forwarder.utils.Log
import kotlinx.coroutines.launch

object Core : Configuration.Provider {
    lateinit var app: Application
    val frpc: FrpcRepository by lazy { (app as App).frpcRepository }
    val msg: MsgRepository by lazy { (app as App).msgRepository }
    val logs: LogsRepository by lazy { (app as App).logsRepository }
    val rule: RuleRepository by lazy { (app as App).ruleRepository }
    val sender: SenderRepository by lazy { (app as App).senderRepository }
    val task: TaskRepository by lazy { (app as App).taskRepository }

    fun init(app: Application) {
        this.app = app
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder().apply {
            setDefaultProcessName(app.packageName + ":bg")
            setMinimumLoggingLevel(if (BuildConfig.DEBUG) Log.VERBOSE else Log.INFO)
            setExecutor { (app as App).applicationScope.launch { it.run() } }
            setTaskExecutor { (app as App).applicationScope.launch { it.run() } }
        }.build()
    }
}
