package com.snaggly.ksw_toolkit.util.list.mcu

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.snaggly.ksw_toolkit.R
import com.snaggly.ksw_toolkit.util.list.ListType

class McuCommandsList(name: String, icon: Drawable, command: McuCommandsEnum) : ListType(name, icon) {
    companion object {
        fun getMcuCommandsList(context: Context) : ArrayList<McuCommandsList> {
            val result = ArrayList<McuCommandsList>()

            result.add(McuCommandsList("Turn Screen Off", ContextCompat.getDrawable(context,R.drawable.ic_baseline_web_asset_off_64)!!, McuCommandsEnum.ScreenOff))
            result.add(McuCommandsList("Increase Brightness", ContextCompat.getDrawable(context,R.drawable.ic_baseline_brightness_high_64)!!, McuCommandsEnum.BrightnessInc))
            result.add(McuCommandsList("Decrease Brightness", ContextCompat.getDrawable(context,R.drawable.ic_baseline_brightness_low_64)!!, McuCommandsEnum.BrightnessDec))
            result.add(McuCommandsList("Switch to OEM", ContextCompat.getDrawable(context,R.drawable.nbt_src_icon_carinfo)!!, McuCommandsEnum.CarInfo))
            result.add(McuCommandsList("Open Radio", ContextCompat.getDrawable(context,R.drawable.ic_baseline_radio_64)!!, McuCommandsEnum.Radio))
            result.add(McuCommandsList("Open Front Cam", ContextCompat.getDrawable(context,R.drawable.ic_baseline_camera_alt_64)!!, McuCommandsEnum.F_CAM))
            result.add(McuCommandsList("Open AUX", ContextCompat.getDrawable(context,R.drawable.ic_baseline_cable_64)!!, McuCommandsEnum.AUX))
            result.add(McuCommandsList("Open DVR", ContextCompat.getDrawable(context,R.drawable.ic_baseline_videocam_64)!!, McuCommandsEnum.DVR))
            result.add(McuCommandsList("Open DVD", ContextCompat.getDrawable(context,R.drawable.ic_baseline_settings_input_component_64)!!, McuCommandsEnum.DVD))
            result.add(McuCommandsList("Open DTV", ContextCompat.getDrawable(context,R.drawable.ic_baseline_live_tv_64)!!, McuCommandsEnum.DTV))

            return result
        }
    }
}