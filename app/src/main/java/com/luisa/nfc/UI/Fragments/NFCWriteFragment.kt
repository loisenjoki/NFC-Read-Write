package com.luisa.nfc.UI.Fragments

import android.app.FragmentManager
import android.content.Context
import android.nfc.FormatException
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.tech.Ndef
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.luisa.nfc.R
import com.luisa.nfc.UI.Activities.MainActivity
import com.luisa.nfc.UI.Callbacks.DialogListener
import java.io.IOException
import java.nio.charset.Charset

class NFCWriteFragment : DialogFragment(){

    companion object {
        fun newInstance(): NFCWriteFragment {

            return NFCWriteFragment()
        }

        val TAG = "TAG"
    }

    private var mTvMessage: TextView? = null
    private var mProgress: ProgressBar? = null
    private lateinit var mListener: DialogListener
    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.nfcwrite_dialog, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View) {
        mTvMessage = view.findViewById<View>(R.id.tv_message) as TextView
        mProgress = view.findViewById<View>(R.id.progress) as ProgressBar
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


    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    fun onNfcDetected(ndef: Ndef, messageToWrite: String) {
        mProgress!!.visibility = View.VISIBLE
        writeToNfc(ndef, messageToWrite)
    }
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun writeToNfc(ndef: Ndef, message: String) {
        mTvMessage!!.text = "write semething"
        if (ndef != null) {
            try {
                ndef.connect()
                val mimeRecord = NdefRecord.createMime(
                    "text/plain",
                    message.toByteArray(Charset.forName("US-ASCII"))
                )
                ndef.writeNdefMessage(NdefMessage(mimeRecord))
                ndef.close()
                //Write Successful
                mTvMessage!!.text = "Success"
                Log.e(TAG,"Success")

            } catch (e: IOException) {
                e.printStackTrace()
                mTvMessage!!.text = "something is wrong"
            } catch (e: FormatException) {
                e.printStackTrace()
                mTvMessage!!.text = "something is wrong"
            } finally {
                mProgress!!.visibility = View.GONE
            }
        }
    }

}