package com.snaggly.ksw_toolkit.util.list.keyevent

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.snaggly.ksw_toolkit.R
import com.snaggly.ksw_toolkit.util.list.ListType

class KeyEvent(name: String, icon: Drawable, val code: Int) : ListType(name, icon) {

    companion object {
        fun getKeyEventList(context: Context) : ArrayList<KeyEvent> {
            val result = ArrayList<KeyEvent>()
            result.add(KeyEvent(KeyCode.APP_SWITCH.name, ContextCompat.getDrawable(context, R.drawable.ic_baseline_flip_to_front_36)!!, KeyCode.APP_SWITCH.keycode))
            result.add(KeyEvent(KeyCode.BACK.name, ContextCompat.getDrawable(context, R.drawable.ic_baseline_keyboard_backspace_36)!!, KeyCode.BACK.keycode))
            result.add(KeyEvent(KeyCode.CALCULATOR.name, ContextCompat.getDrawable(context, R.drawable.ic_baseline_calculate_36)!!, KeyCode.CALCULATOR.keycode))
            result.add(KeyEvent(KeyCode.CALENDAR.name, ContextCompat.getDrawable(context, R.drawable.ic_baseline_calendar_today_36)!!, KeyCode.CALENDAR.keycode))
            result.add(KeyEvent(KeyCode.CONTACTS.name, ContextCompat.getDrawable(context, R.drawable.ic_baseline_contacts_36)!!, KeyCode.CONTACTS.keycode))
            result.add(KeyEvent(KeyCode.DPAD_CENTER.name, ContextCompat.getDrawable(context, R.drawable.ic_baseline_center_focus_weak_36)!!, KeyCode.DPAD_CENTER.keycode))
            result.add(KeyEvent(KeyCode.DPAD_DOWN.name, ContextCompat.getDrawable(context, R.drawable.ic_baseline_keyboard_arrow_down_36)!!, KeyCode.DPAD_DOWN.keycode))
            result.add(KeyEvent(KeyCode.DPAD_LEFT.name, ContextCompat.getDrawable(context, R.drawable.ic_baseline_keyboard_arrow_left_36)!!, KeyCode.DPAD_LEFT.keycode))
            result.add(KeyEvent(KeyCode.DPAD_RIGHT.name, ContextCompat.getDrawable(context, R.drawable.ic_baseline_keyboard_arrow_right_36)!!, KeyCode.DPAD_RIGHT.keycode))
            result.add(KeyEvent(KeyCode.DPAD_UP.name, ContextCompat.getDrawable(context, R.drawable.ic_baseline_keyboard_arrow_up_36)!!, KeyCode.DPAD_UP.keycode))
            result.add(KeyEvent(KeyCode.ENTER.name, ContextCompat.getDrawable(context, R.drawable.ic_baseline_filter_center_focus_24)!!, KeyCode.ENTER.keycode))
            result.add(KeyEvent(KeyCode.ENVELOPE.name, ContextCompat.getDrawable(context, R.drawable.ic_baseline_email_36)!!, KeyCode.ENVELOPE.keycode))
            result.add(KeyEvent(KeyCode.EXPLORER.name, ContextCompat.getDrawable(context, R.drawable.ic_baseline_file_copy_36)!!, KeyCode.EXPLORER.keycode))
            result.add(KeyEvent(KeyCode.HOME.name,  ContextCompat.getDrawable(context, R.drawable.ic_baseline_home_24)!!, KeyCode.HOME.keycode))
            result.add(KeyEvent(KeyCode.MEDIA_NEXT.name, ContextCompat.getDrawable(context, R.drawable.ic_baseline_skip_next_36)!!, KeyCode.MEDIA_NEXT.keycode))
            result.add(KeyEvent(KeyCode.MEDIA_PLAY_PAUSE.name, ContextCompat.getDrawable(context, R.drawable.ic_baseline_play_arrow_36)!!, KeyCode.MEDIA_PLAY_PAUSE.keycode))
            result.add(KeyEvent(KeyCode.MEDIA_PREVIOUS.name, ContextCompat.getDrawable(context, R.drawable.ic_baseline_skip_previous_36)!!, KeyCode.MEDIA_PREVIOUS.keycode))
            result.add(KeyEvent(KeyCode.MEDIA_STOP.name, ContextCompat.getDrawable(context, R.drawable.ic_baseline_stop_36)!!, KeyCode.MEDIA_STOP.keycode))
            result.add(KeyEvent(KeyCode.MODE_SWITCH.name, ContextCompat.getDrawable(context, R.drawable.baseline_music_video_36)!!, KeyCode.MODE_SWITCH.keycode))
            result.add(KeyEvent(KeyCode.MUSIC.name, ContextCompat.getDrawable(context, R.drawable.ic_baseline_library_music_36)!!, KeyCode.MUSIC.keycode))
            result.add(KeyEvent(KeyCode.NAVIGATION.name, ContextCompat.getDrawable(context, R.drawable.baseline_map_36)!!, KeyCode.NAVIGATION.keycode))
            result.add(KeyEvent(KeyCode.SETTINGS.name, ContextCompat.getDrawable(context, R.drawable.ic_baseline_settings_24)!!, KeyCode.SETTINGS.keycode))
            result.add(KeyEvent(KeyCode.CALL.name, ContextCompat.getDrawable(context, R.drawable.ic_baseline_call_36)!!, KeyCode.CALL.keycode))
            result.add(KeyEvent(KeyCode.ENDCALL.name, ContextCompat.getDrawable(context, R.drawable.ic_baseline_call_end_36)!!, KeyCode.ENDCALL.keycode))
            result.add(KeyEvent(KeyCode.VOICE_ASSIST.name, ContextCompat.getDrawable(context, R.drawable.ic_baseline_record_voice_over_36)!!, KeyCode.VOICE_ASSIST.keycode))
            result.add(KeyEvent(KeyCode.VOLUME_DOWN.name, ContextCompat.getDrawable(context, R.drawable.ic_baseline_volume_down_36)!!, KeyCode.VOLUME_DOWN.keycode))
            result.add(KeyEvent(KeyCode.VOLUME_MUTE.name, ContextCompat.getDrawable(context, R.drawable.ic_baseline_volume_off_36)!!, KeyCode.VOLUME_MUTE.keycode))
            result.add(KeyEvent(KeyCode.VOLUME_UP.name, ContextCompat.getDrawable(context, R.drawable.ic_baseline_volume_up_36)!!, KeyCode.VOLUME_UP.keycode))
            result.add(KeyEvent(KeyCode.KEYCODE_1.name, ContextCompat.getDrawable(context, R.drawable.ic_baseline_code_36)!!, KeyCode.KEYCODE_1.keycode))
            result.add(KeyEvent(KeyCode.KEYCODE_2.name, ContextCompat.getDrawable(context, R.drawable.ic_baseline_code_36)!!, KeyCode.KEYCODE_2.keycode))

            return result
        }
    }
}