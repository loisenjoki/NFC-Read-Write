package com.luisa.nfc.UI.Fragments

import android.content.Context
import android.nfc.FormatException
import android.nfc.tech.Ndef
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.luisa.nfc.R
import com.luisa.nfc.UI.Activities.MainActivity
import com.luisa.nfc.UI.Callbacks.DialogListener
import java.io.IOException

class NFCReadFragment : DialogFragment() {
    companion object {
        fun newInstance(): NFCReadFragment {

            return NFCReadFragment()
        }

        val TAG = "TAG"
    }
    fun newInstance(): NFCReadFragment? {
        return NFCReadFragment()
    }

    private lateinit var mTvMessage: TextView
    private lateinit var mListener: DialogListener


    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.read_fragment, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View) {
        mTvMessage = view.findViewById(R.id.tv_message)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as MainActivity
        mListener.onDialogDisplayed()
    }
    override fun onDetach() {
        super.onDetach()
        mListener.onDialogDismissed()
    }

    fun onNfcDetected(ndef: Ndef) {
        readFromNFC(ndef)
    }

    private fun readFromNFC(ndef: Ndef) {
        try {
            ndef.connect()
            val ndefMessage = ndef.ndefMessage
            val message = String(ndefMessage.records[0].payload)
            Log.d(TAG, "readFromNFC: $message")
            mTvMessage.text = message
            ndef.close()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: FormatException) {
            e.printStackTrace()
        }
    }




}
