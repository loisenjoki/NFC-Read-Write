package com.luisa.nfc.UI.Fragments

import android.app.FragmentManager
import android.nfc.tech.Ndef
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

class NFCWriteFragment : DialogFragment(){
    fun show(fragmentManager: FragmentManager?, tag: String) {
    }

    companion object {
        fun newInstance(): NFCWriteFragment {

            return NFCWriteFragment()
        }

        val TAG = "TAG"
    }

    fun onNfcDetected(ndef: Ndef, messageToWrite: String) {

       // mProgress.setVisibility(View.VISIBLE)
        //writeToNfc(ndef, messageToWrite)
    }
}