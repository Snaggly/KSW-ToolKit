package com.snaggly.ksw_toolkit.util.list.tasker

import android.content.Context
import android.database.Cursor
import android.net.Uri
import androidx.core.content.ContextCompat
import com.snaggly.ksw_toolkit.R

class TaskerTaskLister(context: Context) {
    private val taskerTaskList = ArrayList<TaskerTaskInfo>()

    init {
        val c: Cursor? =
            context.applicationContext.contentResolver.query(Uri.parse("content://net.dinglisch.android.tasker/tasks"),
                null,
                null,
                null,
                null)

        if (c != null) {
            val nameCol: Int = c.getColumnIndex("name")
            while (c.moveToNext()) {
                val thisTaskInfo = TaskerTaskInfo(
                    c.getString(nameCol),
                    ContextCompat.getDrawable(context, R.drawable.ic_baseline_settings_suggest_24)!!
                )
                taskerTaskList.add(thisTaskInfo)
            }
            c.close()
        }
    }

    fun getNumOfTasks(): Int {
        return taskerTaskList.size
    }

    fun getTaskList(): ArrayList<TaskerTaskInfo> {
        return taskerTaskList
    }
}