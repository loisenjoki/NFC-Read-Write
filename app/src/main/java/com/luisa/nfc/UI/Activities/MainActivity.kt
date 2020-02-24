package com.luisa.nfc.UI.Activities

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.luisa.nfc.R
import com.luisa.nfc.UI.Fragments.NFCReadFragment
import com.luisa.nfc.UI.Fragments.NFCWriteFragment

class MainActivity : AppCompatActivity() {


    private lateinit var mEtMessage: EditText
    private lateinit var mBtWrite: Button
    private lateinit var mBtRead: Button
    private lateinit var mNfcWriteFragment: NFCWriteFragment
    private lateinit var mNfcReadFragment: NFCReadFragment


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
        mEtMessage = findViewById(R.id.et_message) as EditText
        mBtWrite = findViewById(R.id.btn_write) as Button
        mBtRead = findViewById(R.id.btn_read) as Button

        mBtWrite.setOnClickListener { view -> showWriteFragment() }
        mBtRead.setOnClickListener { view -> showReadFragment() }
    }

    private fun showReadFragment() {

    }

    private fun showWriteFragment() {
        isWrite = true

        mNfcWriteFragment =
            fragmentManager.findFragmentByTag(NFCWriteFragment.TAG) as NFCWriteFragment

        if (mNfcWriteFragment == null) {

            mNfcWriteFragment = NFCWriteFragment.newInstance()
        }
        mNfcWriteFragment.show(fragmentManager, NFCWriteFragment.TAG)
    }

    protected override fun onResume() {
        super.onResume()
        val tagDetected = IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
        val ndefDetected = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        val techDetected = IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)
        val nfcIntentFilter = arrayOf(techDetected, tagDetected, ndefDetected)
        val notifyIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val notifyPendingIntent = PendingIntent.getActivity(
            this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)

    }

    protected override fun onPause() {
        super.onPause()
        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this)
    }

    protected override fun onNewIntent(intent: Intent) {
        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)

        Log.d(TAG, "onNewIntent: " + intent.action!!)

        if (tag != null) {
            Toast.makeText(this, "tag detected", Toast.LENGTH_SHORT)
                .show()
            val ndef = Ndef.get(tag)

            if (isDialogDisplayed) {

                if (isWrite) {

                    val messageToWrite = mEtMessage.getText().toString()
                    mNfcWriteFragment =
                        fragmentManager.findFragmentByTag(NFCWriteFragment.TAG) as NFCWriteFragment
                    mNfcWriteFragment.onNfcDetected(ndef, messageToWrite)

                }
            }
        }
    }
    companion object {

        val TAG = MainActivity::class.java.simpleName
    }
}
