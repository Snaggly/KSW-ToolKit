package com.snaggly.ksw_toolkit.gui

import android.app.AlertDialog
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.snaggly.ksw_toolkit.R
import com.snaggly.ksw_toolkit.gui.viewmodels.ConfigImportExportViewModel

class ConfigImportExport(private val getContent: ActivityResultLauncher<String>,
                         private val createDocument: ActivityResultLauncher<String>) : Fragment() {
    private lateinit var importExportViewModel: ConfigImportExportViewModel
    private lateinit var importConfigBtn: Button
    private lateinit var exportConfigBtn: Button
    private lateinit var restartBtn: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.config_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        importExportViewModel = ViewModelProvider(this).get(ConfigImportExportViewModel::class.java)
        initBtns()
        initBtnClicks()
    }

    private fun initBtns() {
        importConfigBtn = requireView().findViewById(R.id.importConfigBtn)
        exportConfigBtn = requireView().findViewById(R.id.exportConfigBtn)
        restartBtn = requireView().findViewById(R.id.restartSystemBtn)
    }

    private fun initBtnClicks() {
        importConfigBtn.setOnClickListener {

            getContent.launch("*/*")
            try {
                //Toast.makeText(requireContext(), "Imported from: ${importExportViewModel.importConfig()}", Toast.LENGTH_LONG).show()
            } catch (exception: Exception) {
                val alertExc = AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("KSW-ToolKit-Config")
                        .setMessage("Unable to import!\n\n${exception.stackTrace}").create()
                //alertExc.window?.setType(WindowManager.LayoutParams.TYPE_APPLICATION)
                alertExc.show()
            }
        }
        exportConfigBtn.setOnClickListener {

            createDocument.launch("KSW-ToolKit.json")
            try {
                //Toast.makeText(requireContext(), "Exported to: ${importExportViewModel.exportConfig()}", Toast.LENGTH_LONG).show()
            } catch (exception: Exception) {
                val alertExc = AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("KSW-ToolKit-Config")
                        .setMessage("Unable to export!\n\n${exception.stackTrace}").create()
                //alertExc.window?.setType(WindowManager.LayoutParams.TYPE_APPLICATION)
                alertExc.show()
            }
        }
        restartBtn.setOnClickListener {
            try {
                importExportViewModel.restartSystem(requireContext())
            } catch (exception: Exception) {
                val alertExc = AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("KSW-ToolKit-Config")
                        .setMessage("Unable to restart!\n\n${exception.stackTrace}").create()
                //alertExc.window?.setType(WindowManager.LayoutParams.TYPE_APPLICATION)
                alertExc.show()
            }
        }
    }
}