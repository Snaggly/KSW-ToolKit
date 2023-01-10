package com.snaggly.ksw_toolkit.gui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.recyclerview.widget.RecyclerView
import com.snaggly.ksw_toolkit.IKSWToolKitService
import com.snaggly.ksw_toolkit.core.config.beans.EventManager
import com.snaggly.ksw_toolkit.core.service.helper.CoreServiceClient
import com.snaggly.ksw_toolkit.util.adapters.ListTypeAdapter
import com.snaggly.ksw_toolkit.util.list.app.AppInfo
import com.snaggly.ksw_toolkit.util.list.app.AppsLister
import com.snaggly.ksw_toolkit.util.list.eventtype.EventManagerTypes
import com.snaggly.ksw_toolkit.util.list.eventtype.EventMode
import com.snaggly.ksw_toolkit.util.list.keyevent.KeyEvent
import com.snaggly.ksw_toolkit.util.list.mcu.McuCommandsList
import com.snaggly.ksw_toolkit.util.list.tasker.TaskerTaskInfo
import com.snaggly.ksw_toolkit.util.list.tasker.TaskerTaskLister

class EventManagerSelectActionViewModel(application: Application) : AndroidViewModel(application) {
    private var listKeyEventsAdapter : ListTypeAdapter? = null
    private var availableAppsAdapter : ListTypeAdapter? = null
    private var mcuCommandsListAdapter : ListTypeAdapter? = null
    private var availableTaskerTaskAdapter : ListTypeAdapter? = null

    private var keyEvents: ArrayList<KeyEvent>? = null
    private var appsList: ArrayList<AppInfo>? = null
    private var mcuCommandsList: ArrayList<McuCommandsList>? = null
    private var taskerTaskList: ArrayList<TaskerTaskInfo>? = null
    var config : EventManager? = null

    var coreServiceClient : CoreServiceClient? = null

    var eventCurType : EventManagerTypes = EventManagerTypes.Dummy

    lateinit var actionEvent: com.snaggly.ksw_toolkit.gui.EventManager.OnActionResult

    private fun findKeyCodeFromList(keyCode: Int?) : Int{
        if (keyCode == null)
            return 0
        for (i in 0 until keyEvents?.size!!) {
            if (keyCode == keyEvents!![i].code)
                return i
        }
        return 0
    }

    private fun findAppFromList(name: String?) : Int{
        if (name == null)
            return 0
        for (i in 0 until appsList?.size!!) {
            if (name == appsList!![i].packageName)
                return i
        }
        return 0
    }

    private fun findTaskerTaskFromList(name: String?) : Int{
        if (name == null)
            return 0
        for (i in 0 until taskerTaskList?.size!!) {
            if (name == taskerTaskList!![i].taskName)
                return i
        }
        return 0
    }

    private fun initKeyEventsAdapter() {
        keyEvents = KeyEvent.getKeyEventList(getApplication<Application>().applicationContext)
        listKeyEventsAdapter = ListTypeAdapter(keyEvents!!, findKeyCodeFromList(config?.keyCode), onKeyCodeClickListener)
    }

    private fun initAvailableAppsAdapter() {
        appsList = AppsLister(getApplication<Application>().applicationContext).getAppsList()
        availableAppsAdapter = ListTypeAdapter(appsList!!, findAppFromList(config?.appName), onAppClickListener)
    }

    private fun initMcuCommandsListAdapter() {
        mcuCommandsList = McuCommandsList.getMcuCommandsList(getApplication<Application>().applicationContext)
        mcuCommandsListAdapter = ListTypeAdapter(mcuCommandsList!!, config?.mcuCommandMode, onMcuCommandClickListener)
    }

    private fun initTaskerTaskListAdapter() {
        taskerTaskList = TaskerTaskLister(getApplication<Application>().applicationContext).getTaskList()
        availableTaskerTaskAdapter = ListTypeAdapter(taskerTaskList!!, findTaskerTaskFromList
            (config?.taskerTaskName), onTaskerTaskClickListener)
    }

    fun getListKeyEventsAdapter(): ListTypeAdapter {
        if (listKeyEventsAdapter == null)
            initKeyEventsAdapter()
        return listKeyEventsAdapter!!
    }

    fun getAvailableAppsAdapter() : ListTypeAdapter {
        if (availableAppsAdapter == null)
            initAvailableAppsAdapter()
        return availableAppsAdapter!!
    }

    fun getMcuCommandsAdapter() : ListTypeAdapter {
        if (mcuCommandsListAdapter == null)
            initMcuCommandsListAdapter()
        return mcuCommandsListAdapter!!
    }

    fun getAvailableTaskerTaskAdapter() : ListTypeAdapter {
        if (availableTaskerTaskAdapter == null)
            initTaskerTaskListAdapter()
        return availableTaskerTaskAdapter!!
    }

    fun resetEvent() {
        config?.eventMode = EventMode.NoAssignment
        config?.keyCode = -1
        config?.appName = ""
        config?.mcuCommandMode = -1
        config?.taskerTaskName = ""
        coreServiceClient?.coreService?.changeBtnConfig(eventCurType.ordinal, EventMode.NoAssignment.ordinal, "")
    }

    private val onKeyCodeClickListener = object : ListTypeAdapter.OnAppClickListener {
        override fun onAppClick(position: Int) {
            config?.eventMode = EventMode.KeyEvent
            config?.keyCode = keyEvents?.get(position)!!.code
            config?.appName = ""
            config?.mcuCommandMode = -1
            config?.taskerTaskName = ""
            coreServiceClient?.coreService?.changeBtnConfig(eventCurType.ordinal, EventMode.KeyEvent.ordinal, keyEvents?.get(position)!!.code.toString())
            actionEvent.notifyView()
        }
    }

    private val onAppClickListener = object : ListTypeAdapter.OnAppClickListener {
        override fun onAppClick(position: Int) {
            config?.eventMode = EventMode.StartApp
            config?.keyCode = -1
            config?.appName = appsList?.get(position)!!.packageName
            config?.mcuCommandMode = -1
            config?.taskerTaskName = ""
            coreServiceClient?.coreService?.changeBtnConfig(eventCurType.ordinal, EventMode.StartApp.ordinal, appsList?.get(position)!!.packageName)
            actionEvent.notifyView()
        }
    }

    private val onMcuCommandClickListener = object : ListTypeAdapter.OnAppClickListener {
        override fun onAppClick(position: Int) {
            config?.eventMode = EventMode.McuCommand
            config?.keyCode = -1
            config?.appName = ""
            config?.mcuCommandMode = position
            config?.taskerTaskName = ""
            coreServiceClient?.coreService?.changeBtnConfig(eventCurType.ordinal, EventMode.McuCommand.ordinal, position.toString())
            actionEvent.notifyView()
        }
    }

    private val onTaskerTaskClickListener = object : ListTypeAdapter.OnAppClickListener {
        override fun onAppClick(position: Int) {
            config?.eventMode = EventMode.TaskerTask
            config?.keyCode = -1
            config?.appName = ""
            config?.mcuCommandMode = -1
            config?.taskerTaskName = taskerTaskList?.get(position)!!.taskName
            coreServiceClient?.coreService?.changeBtnConfig(eventCurType.ordinal,
                EventMode.TaskerTask.ordinal,
                taskerTaskList?.get(position)!!.taskName)
            actionEvent.notifyView()
        }
    }
}