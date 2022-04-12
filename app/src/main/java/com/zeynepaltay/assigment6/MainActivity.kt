package com.zeynepaltay.assigment6

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.lang.String


class MainActivity : AppCompatActivity(), View.OnClickListener {
    var id: TextView? = null
    var userName: TextView? = null
    var userEmail: TextView? = null
    var gender: TextView? = null
    var btnLogout: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            id = findViewById(R.id.textViewId)
            userName = findViewById(R.id.textViewUsername)
            userEmail = findViewById(R.id.textViewEmail)
            gender = findViewById(R.id.textViewGender)
            btnLogout = findViewById(R.id.buttonLogout)
            val user: User = SharedPrefManager.getInstance(this).getUser()
            id.setText(String.valueOf(user.getId()))
            userEmail.setText(user.getEmail())
            gender.setText(user.getGender())
            userName.setText(user.getName())
            btnLogout.setOnClickListener(this)
        } else {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onClick(view: View) {
        if (view == btnLogout) {
            SharedPrefManager.getInstance(applicationContext).logout()
        }
    }
}
