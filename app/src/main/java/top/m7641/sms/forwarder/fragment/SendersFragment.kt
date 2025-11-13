package top.m7641.sms.forwarder.fragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import top.m7641.sms.forwarder.R
import top.m7641.sms.forwarder.activity.MainActivity
import top.m7641.sms.forwarder.adapter.SenderPagingAdapter
import top.m7641.sms.forwarder.adapter.WidgetItemAdapter
import top.m7641.sms.forwarder.core.BaseFragment
import top.m7641.sms.forwarder.database.entity.Sender
import top.m7641.sms.forwarder.database.viewmodel.BaseViewModelFactory
import top.m7641.sms.forwarder.database.viewmodel.SenderViewModel
import top.m7641.sms.forwarder.databinding.FragmentSendersBinding
import top.m7641.sms.forwarder.fragment.senders.BarkFragment
import top.m7641.sms.forwarder.fragment.senders.DingtalkGroupRobotFragment
import top.m7641.sms.forwarder.fragment.senders.DingtalkInnerRobotFragment
import top.m7641.sms.forwarder.fragment.senders.EmailFragment
import top.m7641.sms.forwarder.fragment.senders.FeishuAppFragment
import top.m7641.sms.forwarder.fragment.senders.FeishuFragment
import top.m7641.sms.forwarder.fragment.senders.GotifyFragment
import top.m7641.sms.forwarder.fragment.senders.PushplusFragment
import top.m7641.sms.forwarder.fragment.senders.ServerchanFragment
import top.m7641.sms.forwarder.fragment.senders.SmsFragment
import top.m7641.sms.forwarder.fragment.senders.SocketFragment
import top.m7641.sms.forwarder.fragment.senders.TelegramFragment
import top.m7641.sms.forwarder.fragment.senders.UrlSchemeFragment
import top.m7641.sms.forwarder.fragment.senders.WebhookFragment
import top.m7641.sms.forwarder.fragment.senders.WeworkAgentFragment
import top.m7641.sms.forwarder.fragment.senders.WeworkRobotFragment
import top.m7641.sms.forwarder.utils.KEY_SENDER_CLONE
import top.m7641.sms.forwarder.utils.KEY_SENDER_ID
import top.m7641.sms.forwarder.utils.KEY_SENDER_TYPE
import top.m7641.sms.forwarder.utils.Log
import top.m7641.sms.forwarder.utils.TYPE_BARK
import top.m7641.sms.forwarder.utils.TYPE_DINGTALK_GROUP_ROBOT
import top.m7641.sms.forwarder.utils.TYPE_DINGTALK_INNER_ROBOT
import top.m7641.sms.forwarder.utils.TYPE_EMAIL
import top.m7641.sms.forwarder.utils.TYPE_FEISHU
import top.m7641.sms.forwarder.utils.TYPE_FEISHU_APP
import top.m7641.sms.forwarder.utils.TYPE_GOTIFY
import top.m7641.sms.forwarder.utils.TYPE_PUSHPLUS
import top.m7641.sms.forwarder.utils.TYPE_SERVERCHAN
import top.m7641.sms.forwarder.utils.TYPE_SMS
import top.m7641.sms.forwarder.utils.TYPE_SOCKET
import top.m7641.sms.forwarder.utils.TYPE_TELEGRAM
import top.m7641.sms.forwarder.utils.TYPE_URL_SCHEME
import top.m7641.sms.forwarder.utils.TYPE_WEBHOOK
import top.m7641.sms.forwarder.utils.TYPE_WEWORK_AGENT
import top.m7641.sms.forwarder.utils.TYPE_WEWORK_ROBOT
import top.m7641.sms.forwarder.utils.XToastUtils
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.xuexiang.xaop.annotation.SingleClick
import com.xuexiang.xpage.annotation.Page
import com.xuexiang.xpage.base.XPageFragment
import com.xuexiang.xpage.core.PageOption
import com.xuexiang.xpage.enums.CoreAnim
import com.xuexiang.xpage.model.PageInfo
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder
import com.xuexiang.xui.utils.DensityUtils
import com.xuexiang.xui.utils.WidgetUtils
import com.xuexiang.xui.widget.actionbar.TitleBar
import com.xuexiang.xui.widget.alpha.XUIAlphaTextView
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog
import com.xuexiang.xutil.resource.ResUtils.getStringArray
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Suppress("PrivatePropertyName", "DEPRECATION")
@Page(name = "发送通道")
class SendersFragment : BaseFragment<FragmentSendersBinding?>(),
    SenderPagingAdapter.OnItemClickListener,
    RecyclerViewHolder.OnItemClickListener<PageInfo> {

    private val TAG: String = SendersFragment::class.java.simpleName
    private val that = this
    private var titleBar: TitleBar? = null
    private var adapter = SenderPagingAdapter(this)
    private val viewModel by viewModels<SenderViewModel> { BaseViewModelFactory(context) }
    private val dialog: BottomSheetDialog by lazy { BottomSheetDialog(requireContext()) }
    private var currentStatus: Int = 1
    private var SENDER_FRAGMENT_LIST = listOf(
        PageInfo(
            getString(R.string.dingtalk_robot),
            "top.m7641.sms.forwarder.fragment.senders.DingtalkGroupRobotFragment",
            "{\"\":\"\"}",
            CoreAnim.slide,
            R.drawable.icon_dingtalk
        ),
        PageInfo(
            getString(R.string.email),
            "top.m7641.sms.forwarder.fragment.senders.EmailFragment",
            "{\"\":\"\"}",
            CoreAnim.slide,
            R.drawable.icon_email
        ),
        PageInfo(
            getString(R.string.bark),
            "top.m7641.sms.forwarder.fragment.senders.BarkFragment",
            "{\"\":\"\"}",
            CoreAnim.slide,
            R.drawable.icon_bark
        ),
        PageInfo(
            getString(R.string.webhook),
            "top.m7641.sms.forwarder.fragment.senders.WebhookFragment",
            "{\"\":\"\"}",
            CoreAnim.slide,
            R.drawable.icon_webhook
        ),
        PageInfo(
            getString(R.string.wework_robot),
            "top.m7641.sms.forwarder.fragment.senders.WeworkRobotFragment",
            "{\"\":\"\"}",
            CoreAnim.slide,
            R.drawable.icon_wework_robot
        ),
        PageInfo(
            getString(R.string.wework_agent),
            "top.m7641.sms.forwarder.fragment.senders.WeworkAgentFragment",
            "{\"\":\"\"}",
            CoreAnim.slide,
            R.drawable.icon_wework_agent
        ),
        PageInfo(
            getString(R.string.server_chan),
            "top.m7641.sms.forwarder.fragment.senders.ServerchanFragment",
            "{\"\":\"\"}",
            CoreAnim.slide,
            R.drawable.icon_serverchan
        ),
        PageInfo(
            getString(R.string.telegram),
            "top.m7641.sms.forwarder.fragment.senders.TelegramFragment",
            "{\"\":\"\"}",
            CoreAnim.slide,
            R.drawable.icon_telegram
        ),
        PageInfo(
            getString(R.string.sms_menu),
            "top.m7641.sms.forwarder.fragment.senders.SmsFragment",
            "{\"\":\"\"}",
            CoreAnim.slide,
            R.drawable.icon_sms
        ),
        PageInfo(
            getString(R.string.feishu),
            "top.m7641.sms.forwarder.fragment.senders.FeishuFragment",
            "{\"\":\"\"}",
            CoreAnim.slide,
            R.drawable.icon_feishu
        ),
        PageInfo(
            getString(R.string.pushplus),
            "top.m7641.sms.forwarder.fragment.senders.PushplusFragment",
            "{\"\":\"\"}",
            CoreAnim.slide,
            R.drawable.icon_pushplus
        ),
        PageInfo(
            getString(R.string.gotify),
            "top.m7641.sms.forwarder.fragment.senders.GotifyFragment",
            "{\"\":\"\"}",
            CoreAnim.slide,
            R.drawable.icon_gotify
        ),
        PageInfo(
            getString(R.string.dingtalk_inner_robot),
            "top.m7641.sms.forwarder.fragment.senders.DingtalkInnerRobotFragment",
            "{\"\":\"\"}",
            CoreAnim.slide,
            R.drawable.icon_dingtalk_inner
        ),
        PageInfo(
            getString(R.string.feishu_app),
            "top.m7641.sms.forwarder.fragment.senders.FeishuAppFragment",
            "{\"\":\"\"}",
            CoreAnim.slide,
            R.drawable.icon_feishu_app
        ),
        PageInfo(
            getString(R.string.url_scheme),
            "top.m7641.sms.forwarder.fragment.senders.UrlSchemeFragment",
            "{\"\":\"\"}",
            CoreAnim.slide,
            R.drawable.icon_url_scheme
        ),
        PageInfo(
            getString(R.string.socket),
            "top.m7641.sms.forwarder.fragment.senders.SocketFragment",
            "{\"\":\"\"}",
            CoreAnim.slide,
            R.drawable.icon_socket
        ),
    )

    override fun viewBindingInflate(
        inflater: LayoutInflater,
        container: ViewGroup,
    ): FragmentSendersBinding {
        return FragmentSendersBinding.inflate(inflater, container, false)
    }

    override fun initTitle(): TitleBar? {
        titleBar = super.initTitle()!!.setImmersive(false)
        titleBar!!.setLeftImageResource(R.drawable.ic_action_menu)
        titleBar!!.setTitle(R.string.menu_senders)
        titleBar!!.setLeftClickListener { getContainer()?.openMenu() }
        titleBar!!.addAction(object : TitleBar.ImageAction(R.drawable.ic_add) {
            @SuppressLint("InflateParams")
            @SingleClick
            override fun performAction(view: View) {
                val bottomSheet: View = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_sender_bottom_sheet, null)
                val recyclerView: RecyclerView = bottomSheet.findViewById(R.id.recyclerView)

                WidgetUtils.initGridRecyclerView(recyclerView, 4, DensityUtils.dp2px(1f))
                val widgetItemAdapter = WidgetItemAdapter(SENDER_FRAGMENT_LIST)
                widgetItemAdapter.setOnItemClickListener(that)
                recyclerView.adapter = widgetItemAdapter

                val bottomSheetCloseButton: XUIAlphaTextView = bottomSheet.findViewById(R.id.bottom_sheet_close_button)
                bottomSheetCloseButton.setOnClickListener { dialog.dismiss() }

                dialog.setContentView(bottomSheet)
                dialog.setCancelable(true)
                dialog.setCanceledOnTouchOutside(true)
                dialog.show()
                WidgetUtils.transparentBottomSheetDialogBackground(dialog)
            }
        })
        return titleBar
    }

    private fun getContainer(): MainActivity? {
        return activity as MainActivity?
    }

    /**
     * 初始化控件
     */
    override fun initViews() {
        val virtualLayoutManager = VirtualLayoutManager(requireContext())
        binding!!.recyclerView.layoutManager = virtualLayoutManager
        val viewPool = RecycledViewPool()
        binding!!.recyclerView.setRecycledViewPool(viewPool)
        viewPool.setMaxRecycledViews(0, 10)

        binding!!.tabBar.setTabTitles(getStringArray(R.array.status_param_option))
        binding!!.tabBar.setOnTabClickListener { _, position ->
            //XToastUtils.toast("点击了$title--$position")
            //currentStatus = statusValueArray[position]
            currentStatus = 1 - position //注意：这里刚好相反，可以取巧
            viewModel.setStatus(currentStatus)
            adapter.refresh()
            binding!!.recyclerView.scrollToPosition(0)
        }
    }

    override fun initListeners() {
        binding!!.recyclerView.adapter = adapter

        //下拉刷新
        binding!!.refreshLayout.setOnRefreshListener { refreshLayout: RefreshLayout ->
            refreshLayout.layout.postDelayed({
                //adapter!!.refresh()
                lifecycleScope.launch {
                    viewModel.setStatus(currentStatus).allSenders.collectLatest { adapter.submitData(it) }
                }
                refreshLayout.finishRefresh()
            }, 200)
        }

        binding!!.refreshLayout.autoRefresh()
    }

    override fun onItemClicked(view: View?, item: Sender) {
        Log.e(TAG, item.toString())
        when (view?.id) {
            R.id.iv_copy -> {
                PageOption.to(getFragment(item.type))
                    .setNewActivity(true)
                    .putLong(KEY_SENDER_ID, item.id)
                    .putInt(KEY_SENDER_TYPE, item.type)
                    .putBoolean(KEY_SENDER_CLONE, true)
                    .open(this)
            }

            R.id.iv_edit -> {
                PageOption.to(getFragment(item.type))
                    .setNewActivity(true)
                    .putLong(KEY_SENDER_ID, item.id)
                    .putInt(KEY_SENDER_TYPE, item.type)
                    .open(this)
            }

            R.id.iv_delete -> {
                MaterialDialog.Builder(requireContext())
                    .title(R.string.delete_sender_title)
                    .content(R.string.delete_sender_tips)
                    .positiveText(R.string.lab_yes)
                    .negativeText(R.string.lab_no)
                    .onPositive { _: MaterialDialog?, _: DialogAction? ->
                        viewModel.delete(item.id)
                        XToastUtils.success(R.string.delete_sender_toast)
                    }
                    .show()
            }

            else -> {}
        }
    }

    override fun onItemRemove(view: View?, id: Int) {}

    @SingleClick
    override fun onItemClick(itemView: View, widgetInfo: PageInfo, pos: Int) {
        try {
            @Suppress("UNCHECKED_CAST")
            PageOption.to(Class.forName(widgetInfo.classPath) as Class<XPageFragment>) //跳转的fragment
                .setNewActivity(true)
                .putInt(KEY_SENDER_TYPE, pos) //注意：目前刚好是这个顺序而已
                .open(this)
            dialog.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "onItemClick error: ${e.message}")
            XToastUtils.error(e.message.toString())
        }
    }

    private fun getFragment(type: Int): Class<out XPageFragment> {
        return when (type) {
            TYPE_DINGTALK_GROUP_ROBOT -> DingtalkGroupRobotFragment::class.java
            TYPE_EMAIL -> EmailFragment::class.java
            TYPE_BARK -> BarkFragment::class.java
            TYPE_WEBHOOK -> WebhookFragment::class.java
            TYPE_WEWORK_ROBOT -> WeworkRobotFragment::class.java
            TYPE_WEWORK_AGENT -> WeworkAgentFragment::class.java
            TYPE_SERVERCHAN -> ServerchanFragment::class.java
            TYPE_TELEGRAM -> TelegramFragment::class.java
            TYPE_SMS -> SmsFragment::class.java
            TYPE_FEISHU -> FeishuFragment::class.java
            TYPE_PUSHPLUS -> PushplusFragment::class.java
            TYPE_GOTIFY -> GotifyFragment::class.java
            TYPE_DINGTALK_INNER_ROBOT -> DingtalkInnerRobotFragment::class.java
            TYPE_FEISHU_APP -> FeishuAppFragment::class.java
            TYPE_URL_SCHEME -> UrlSchemeFragment::class.java
            TYPE_SOCKET -> SocketFragment::class.java
            else -> DingtalkGroupRobotFragment::class.java
        }
    }

}