package com.snaggly.ksw_toolkit.gui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.snaggly.ksw_toolkit.R
import com.snaggly.ksw_toolkit.core.service.adb.AdbServiceConnection
import com.snaggly.ksw_toolkit.core.service.helper.CoreServiceClient
import com.snaggly.ksw_toolkit.core.service.helper.ServiceAliveCheck
import com.snaggly.ksw_toolkit.gui.viewmodels.ToolKitViewModel
import com.snaggly.ksw_toolkit.util.github.DownloadController
import com.snaggly.ksw_toolkit.util.github.GitHubRelease
import com.snaggly.ksw_toolkit.util.github.VersionTag

class ToolKit(private val coreServiceClient: CoreServiceClient) : Fragment() {
    private lateinit var mViewModel: ToolKitViewModel
    private lateinit var carInfo: LinearLayout
    private lateinit var radio: LinearLayout
    private lateinit var frontCam: LinearLayout
    private lateinit var aux: LinearLayout
    private lateinit var dvr: LinearLayout
    private lateinit var dvd: LinearLayout
    private lateinit var dtv: LinearLayout
    private lateinit var screenOffBtn: Button
    private lateinit var openSourceTV: TextView
    private lateinit var openSourceLayout: LinearLayout

    private lateinit var startStopServiceBtn : Button
    private lateinit var runningSymbol : ImageView
    private lateinit var runningStatusTextView : TextView
    private lateinit var versionTextView : TextView
    private lateinit var versionLiteralTV : TextView
    private lateinit var updateBtn : Button
    private lateinit var startAtBootSwitch: SwitchCompat
    private lateinit var startAtBootSwitchText: TextView
    private lateinit var newerVersionTextView : TextView
    private lateinit var enableUsbDebuggingTextView : TextView

    private lateinit var progressSpinner : ProgressBar

    private var latestServiceGitHubRelease : GitHubRelease? = null
    //private var latestClientGitHubRelease : GitHubRelease? = null

    private val serviceAliveObserver: Observer<Boolean>
    private val serviceConnectedObserver: Observer<Boolean>

    private var isServiceInstalled = false
    private var isInInstallMode = false
        set(value) {
            field = value
            if (value) {
                updateBtn.text = resources.getText(R.string.download)
            }
            else {
                updateBtn.text = resources.getText(R.string.check_for_updates)
                newerVersionTextView.text = ""
            }
        }

    companion object {
        var ServiceRunning = false
        var ServiceConnected = false
    }

    init {
        serviceAliveObserver = Observer<Boolean> { t ->
            if (t != ServiceRunning) {
                ServiceRunning = t
                resetStatus()
            }
        }

        serviceConnectedObserver = Observer<Boolean> { t ->
            requireActivity().runOnUiThread {
                if (t != ServiceConnected) {
                    ServiceConnected = t
                    resetConnection()
                    mViewModel.checkService(requireContext())
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.toolkit_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = ViewModelProvider(this).get(ToolKitViewModel::class.java)
        mViewModel.coreServiceClient = coreServiceClient

        initViews()
        initClickEvents()
    }

    override fun onResume() {
        super.onResume()
        ServiceAliveCheck.serviceAliveObservers.add(serviceAliveObserver)
        coreServiceClient.clientObservers.add(serviceConnectedObserver)
        resetStatus()
        resetConnection()
        mViewModel.checkService(requireContext())
    }

    override fun onPause() {
        super.onPause()
        ServiceAliveCheck.serviceAliveObservers.remove(serviceAliveObserver)
        coreServiceClient.clientObservers.remove(serviceConnectedObserver)
    }

    private fun resetStatus() {
        val serviceVersion = CoreServiceClient.getInstalledServiceVersion(requireContext())
        requireActivity().runOnUiThread {
            progressSpinner.visibility = View.GONE
            startStopServiceBtn.visibility = View.VISIBLE
            versionTextView.visibility = View.VISIBLE
            versionLiteralTV.visibility = View.VISIBLE
            if (serviceVersion == null) {
                isServiceInstalled = false
                startStopServiceBtn.text = resources.getText(R.string.start_service)
                startStopServiceBtn.visibility = View.GONE
                runningSymbol.background = ContextCompat.getDrawable(requireContext(),R.mipmap.grey_button)
                runningStatusTextView.text = resources.getText(R.string.uninstalled)
                versionTextView.visibility = View.GONE
                versionLiteralTV.visibility = View.GONE
                isInInstallMode = true
            }
            else if (ServiceRunning) {
                isServiceInstalled = true
                startStopServiceBtn.text = resources.getText(R.string.stop_service)
                runningSymbol.background = ContextCompat.getDrawable(requireContext(),R.mipmap.green_button)
                runningStatusTextView.text = resources.getText(R.string.running)
                versionTextView.text = serviceVersion.toString()
                isInInstallMode = false
            } else {
                isServiceInstalled = true
                startStopServiceBtn.text = resources.getText(R.string.start_service)
                runningSymbol.background = ContextCompat.getDrawable(requireContext(),R.mipmap.red_button)
                runningStatusTextView.text = resources.getText(R.string.inactive)
                versionTextView.text = serviceVersion.toString()
                isInInstallMode = false
            }
            enableUsbDebuggingTextView.visibility = View.GONE
            if (mViewModel.isAdbDebuggingDisabled(requireContext()))
                enableUsbDebuggingTextView.visibility = View.VISIBLE
        }
    }

    private fun resetConnection() {
        if (ServiceConnected) {
            try {
                startAtBootSwitch.isChecked = mViewModel.getStartAtBootOption()
            } catch (e : Exception) {
                Toast.makeText(requireContext(), "${requireActivity().resources.getText(R.string.error_in_reading_service)}\n$e", Toast.LENGTH_LONG).show()
            }
            startAtBootSwitch.visibility = View.VISIBLE
            startAtBootSwitchText.text = getString(R.string.start_at_boot)
            screenOffBtn.visibility = View.VISIBLE
            openSourceLayout.visibility = View.VISIBLE
            openSourceTV.visibility = View.VISIBLE
        } else {
            startAtBootSwitch.visibility = View.GONE
            startAtBootSwitchText.text = getString(R.string.not_connected_to_service)
            screenOffBtn.visibility = View.GONE
            openSourceLayout.visibility = View.GONE
            openSourceTV.visibility = View.GONE
        }
    }

    private fun initViews() {
        carInfo = requireView().findViewById(R.id.carInfoBtn)
        radio = requireView().findViewById(R.id.radioBtn)
        frontCam = requireView().findViewById(R.id.frontCamBtn)
        aux = requireView().findViewById(R.id.auxBtn)
        dvr = requireView().findViewById(R.id.dvrBtn)
        dvd = requireView().findViewById(R.id.dvdBtn)
        dtv = requireView().findViewById(R.id.dtvBtn)
        screenOffBtn = requireView().findViewById(R.id.screenOffBtn)
        openSourceTV = requireView().findViewById(R.id.openSourceTV)
        openSourceLayout = requireView().findViewById(R.id.openSourceLayout)

        startStopServiceBtn = requireView().findViewById(R.id.startStopServiceBtn)
        runningSymbol = requireView().findViewById(R.id.runningSymbol)
        runningStatusTextView = requireView().findViewById(R.id.runningStatusTextView)
        versionTextView = requireView().findViewById(R.id.versionTextView)
        versionLiteralTV = requireView().findViewById(R.id.versionLiteralTV)
        updateBtn = requireView().findViewById(R.id.updateBtn)
        startAtBootSwitch = requireView().findViewById(R.id.startAppAtBootTgl)
        startAtBootSwitchText = requireView().findViewById(R.id.start_at_boot_txt)
        newerVersionTextView = requireView().findViewById(R.id.newerVersionTextView)
        enableUsbDebuggingTextView = requireView().findViewById(R.id.enableUsbDebuggingTextView)

        progressSpinner = requireView().findViewById(R.id.progress_spinner)

        startStopServiceBtn.requestFocus()
    }

    @SuppressLint("SetTextI18n")
    private fun initClickEvents() {
        carInfo.setOnClickListener {
            try {
                mViewModel.openOEMScreen()
            } catch (exception: Exception) {
                val alertExc = AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("${requireActivity().resources.getText(R.string.app_name)}")
                        .setMessage("${requireActivity().resources.getText(R.string.unable_to_open_oem_screen)}\n\n$exception").create()
                alertExc.show()
            }
        }

        radio.setOnClickListener {
            try {
                mViewModel.openRadioScreen()
            } catch (exception: Exception) {
                val alertExc = AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("${requireActivity().resources.getText(R.string.app_name)}")
                        .setMessage("${requireActivity().resources.getText(R.string.unable_to_open_radio_screen)}\n\n$exception").create()
                alertExc.show()
            }
        }

        frontCam.setOnClickListener {
            try {
                mViewModel.openFCamScreen()
            } catch (exception: Exception) {
                val alertExc = AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("${requireActivity().resources.getText(R.string.app_name)}")
                        .setMessage("${requireActivity().resources.getText(R.string.unable_to_open_f_cam_screen)}\n\n$exception").create()
                alertExc.show()
            }
        }

        aux.setOnClickListener {
            try {
                mViewModel.openAuxScreen()
            } catch (exception: Exception) {
                val alertExc = AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("${requireActivity().resources.getText(R.string.app_name)}")
                        .setMessage("${requireActivity().resources.getText(R.string.unable_to_open_aux_screen)}\n\n$exception").create()
                alertExc.show()
            }
        }

        dvr.setOnClickListener {
            try {
                mViewModel.openDvrScreen()
            } catch (exception: Exception) {
                val alertExc = AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("${requireActivity().resources.getText(R.string.app_name)}")
                        .setMessage("${requireActivity().resources.getText(R.string.unable_to_open_dvr_screen)}\n\n$exception").create()
                alertExc.show()
            }
        }

        dvd.setOnClickListener {
            try {
                mViewModel.openDvdScreen()
            } catch (exception: Exception) {
                val alertExc = AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("${requireActivity().resources.getText(R.string.app_name)}")
                        .setMessage("${requireActivity().resources.getText(R.string.unable_to_open_dvr_screen)}\n\n$exception").create()
                alertExc.show()
            }
        }

        dtv.setOnClickListener {
            try {
                mViewModel.openDtvScreen()
            } catch (exception: Exception) {
                val alertExc = AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("${requireActivity().resources.getText(R.string.app_name)}")
                        .setMessage("${requireActivity().resources.getText(R.string.unable_to_open_dtv_screen)}\n\n$exception").create()
                alertExc.show()
            }
        }

        screenOffBtn.setOnClickListener {
            try {
                mViewModel.closeScreen()
            } catch (exception: Exception) {
                val alertExc = AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("${requireActivity().resources.getText(R.string.app_name)}")
                        .setMessage("${requireActivity().resources.getText(R.string.unable_to_close_screen)}\n\n$exception").create()
                alertExc.show()
            }
        }

        startStopServiceBtn.setOnClickListener {
            if (!ServiceRunning) {
                try{
                    AdbServiceConnection.startThisService(requireContext())
                    mViewModel.checkService(requireContext())
                }catch (exception : Exception) {
                    Toast.makeText(requireContext(), resources.getText(R.string.could_not_start_service), Toast.LENGTH_LONG).show()
                }
            } else {
                try{
                    AdbServiceConnection.stopThisService(requireContext())
                    mViewModel.checkService(requireContext())
                }catch (exception : Exception) {
                    exception.printStackTrace()
                    Toast.makeText(requireContext(), resources.getText(R.string.could_not_stop_service), Toast.LENGTH_LONG).show()
                }
            }
        }

        startAtBootSwitch.setOnClickListener {
            try {
                if (coreServiceClient.coreService != null)
                    mViewModel.setStartAtBootOption((it as SwitchCompat).isChecked)
            }
            catch (e: Exception) {
                Toast.makeText(requireContext(), "${resources.getText(R.string.unable_to_set_settings)}\n$e", Toast.LENGTH_LONG).show()
            }
        }

        updateBtn.setOnClickListener {
            progressSpinner.visibility = View.VISIBLE
            Thread {
                try {
                    if (isInInstallMode) {
                        if (latestServiceGitHubRelease == null)
                            latestServiceGitHubRelease = DownloadController.getServiceGitHubRelease()
                        val dc = DownloadController.draftDownload(latestServiceGitHubRelease)
                        if (dc != null) {
                            DownloadController.enqueueDownload(requireContext(), dc, object : BroadcastReceiver() {
                                override fun onReceive(context: Context?, intent: Intent?) {
                                    requireActivity().runOnUiThread{ progressSpinner.visibility = View.GONE}
                                    DownloadController.onReceive(context, this)
                                }
                            })
                        } else {
                            requireActivity().runOnUiThread {
                                progressSpinner.visibility = View.GONE
                                Toast.makeText(requireContext(), getString(R.string.no_apk_found), Toast.LENGTH_LONG).show()
                            }
                        }
                    } else {
                        latestServiceGitHubRelease = DownloadController.getServiceGitHubRelease()
                        val remoteServiceVersion = VersionTag.getVersion(latestServiceGitHubRelease!!.tag_name)
                        if (VersionTag.isNewerServiceVersionAvailable(requireContext(), remoteServiceVersion)) {
                            requireActivity().runOnUiThread{
                                progressSpinner.visibility = View.GONE
                                newerVersionTextView.text = remoteServiceVersion.toString()
                                    Toast.makeText(
                                        requireContext(),
                                        "${getString(R.string.new_service_version_found)} $remoteServiceVersion",
                                        Toast.LENGTH_LONG
                                    ).show()
                                isInInstallMode = true
                            }
                        } else {
                            requireActivity().runOnUiThread{
                                progressSpinner.visibility = View.GONE
                                Toast.makeText(requireContext(), getString(R.string.already_up_to_date), Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                } catch (e : Exception) {
                    requireActivity().runOnUiThread {
                        progressSpinner.visibility = View.GONE
                        Toast.makeText(requireContext(), "${getString(R.string.error_reading_url)}: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                    }
                }
            }.start()
        }
    }
}
