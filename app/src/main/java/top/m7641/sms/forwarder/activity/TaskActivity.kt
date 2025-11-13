package top.m7641.sms.forwarder.activity

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import top.m7641.sms.forwarder.core.BaseActivity
import top.m7641.sms.forwarder.fragment.TasksFragment

class TaskActivity : BaseActivity<ViewBinding?>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openPage(TasksFragment::class.java)
    }
}