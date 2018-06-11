package com.dannie.p2p

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dannie.p2p.other.Const
import com.dannie.p2p.other.extensions.defaultPrefs
import com.dannie.p2p.other.extensions.getValue
import com.dannie.p2p.other.extensions.setValue

class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        val sharedPrefs = defaultPrefs(this)
        val firstOpen = sharedPrefs.getValue(Const.SharedPrefs.FIRST_OPEN, true)

        if (firstOpen){
            sharedPrefs.setValue(Const.SharedPrefs.FIRST_OPEN, false)
            Toast.makeText(this, "first open!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "second open", Toast.LENGTH_SHORT).show()
        }
    }
}
