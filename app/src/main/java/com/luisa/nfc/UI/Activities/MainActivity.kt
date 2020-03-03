package com.luisa.nfc.UI.Activities

import android.app.PendingIntent
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.luisa.nfc.R
import com.luisa.nfc.UI.Callbacks.DialogListener
import com.luisa.nfc.UI.Fragments.NFCReadFragment
import com.luisa.nfc.UI.Fragments.NFCWriteFragment

class MainActivity : AppCompatActivity() , DialogListener{


    private lateinit var mEtMessage: EditText
    private lateinit var mBtWrite: Button
    private lateinit var mBtRead: Button
    private  var mNfcWriteFragment: NFCWriteFragment? = null
    private    var mNfcReadFragment: NFCReadFragment? = null


    private var isDialogDisplayed = false
    private var isWrite = false

    private lateinit var mNfcAdapter: NfcAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this)
        initviews()
    }

    private fun initviews() {
        mEtMessage = findViewById(R.id.et_message)
        mBtWrite = findViewById(R.id.btn_write)
        mBtRead = findViewById(R.id.btn_read)
        mBtWrite.setOnClickListener { view -> showWriteFragment() }
        mBtRead.setOnClickListener { view -> showReadFragment() }
    }

    private fun showReadFragment() {
        mNfcReadFragment = fragmentManager.findFragmentByTag(NFCReadFragment.TAG) as? NFCReadFragment

        if (mNfcReadFragment == null) {
            mNfcReadFragment = NFCReadFragment.newInstance()
        }
        mNfcReadFragment!!.show(supportFragmentManager, NFCReadFragment.TAG)

    }

    private fun showWriteFragment() {
        isWrite = true
        mNfcWriteFragment =(NFCWriteFragment.TAG) as? NFCWriteFragment

        if (mNfcWriteFragment == null) {
            mNfcWriteFragment = NFCWriteFragment.newInstance()
        }
        mNfcWriteFragment!!.show(supportFragmentManager, NFCWriteFragment.TAG)
    }

     override fun onResume() {
        super.onResume()
        val tagDetected = IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
        val ndefDetected = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        val techDetected = IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)
        val nfcIntentFilter = arrayOf(techDetected, tagDetected, ndefDetected)
         val pendingIntent = PendingIntent.getActivity(
             this, 0, Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
         )
         if (mNfcAdapter != null)
             mNfcAdapter.enableForegroundDispatch(this, pendingIntent, nfcIntentFilter, null)


     }

    protected override fun onPause() {
        super.onPause()
        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this)
    }
    fun writeTag(Message:String){
        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
        if (tag != null) {
            Toast.makeText(applicationContext,"message", Toast.LENGTH_SHORT)
                .show()
            val ndef = Ndef.get(tag)
        }
        Toast.makeText(this, "here", Toast.LENGTH_SHORT)
    }


    override fun onDialogDisplayed() {
        isDialogDisplayed = true
    }

    override fun onDialogDismissed() {
        isDialogDisplayed = false
        isWrite = false
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onNewIntent(intent: Intent) {
        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
        Log.d(TAG, "onNewIntent: " + intent.action)
        if (tag != null) {
            Toast.makeText(this,"tag detected", Toast.LENGTH_SHORT)
                .show()
            val ndef = Ndef.get(tag)
            if (isDialogDisplayed) {
                if (isWrite) {
                    val messageToWrite = mEtMessage.text.toString()
                    mNfcWriteFragment =
                        fragmentManager.findFragmentByTag(NFCWriteFragment.TAG) as NFCWriteFragment
                    mNfcWriteFragment!!.onNfcDetected(ndef, messageToWrite)
                } else {

                    mNfcReadFragment = fragmentManager.findFragmentByTag(NFCReadFragment.TAG) as? NFCReadFragment
                    mNfcReadFragment!!.onNfcDetected(ndef)
                }
            }
        }
    }
    companion object {
        val TAG = MainActivity::class.java.simpleName
    }

}
