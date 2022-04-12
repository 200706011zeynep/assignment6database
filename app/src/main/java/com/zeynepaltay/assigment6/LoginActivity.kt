package com.zeynepaltay.assigment6

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import org.json.JSONException
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {
    var etName: EditText? = null
    var etPassword: EditText? = null
    var progressBar: ProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }
        progressBar = findViewById(R.id.progressBar)
        etName = findViewById(R.id.etUserName)
        etPassword = findViewById(R.id.etUserPassword)


        //calling the method userLogin() for login the user
        findViewById<View>(R.id.btnLogin).setOnClickListener { userLogin() }

        //if user presses on textview not register calling RegisterActivity
        findViewById<View>(R.id.tvRegister).setOnClickListener {
            finish()
            startActivity(Intent(applicationContext, RegisterActivity::class.java))
        }
    }

    private fun userLogin() {
        //first getting the values
        val username = etName!!.text.toString()
        val password = etPassword!!.text.toString()
        //validating inputs
        if (TextUtils.isEmpty(username)) {
            etName!!.error = "Please enter your username"
            etName!!.requestFocus()
            return
        }
        if (TextUtils.isEmpty(password)) {
            etPassword!!.error = "Please enter your password"
            etPassword!!.requestFocus()
            return
        }

        //if everything is fine
        val stringRequest: StringRequest =
            object : StringRequest(Request.Method.POST, URLs.URL_LOGIN,
                object : Listener<String?>() {
                    fun onResponse(response: String?) {
                        progressBar!!.visibility = View.GONE
                        try {
                            //converting response to json object
                            val obj = JSONObject(response)

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(
                                    applicationContext,
                                    obj.getString("message"),
                                    Toast.LENGTH_SHORT
                                ).show()

                                //getting the user from the response
                                val userJson = obj.getJSONObject("user")

                                //creating a new user object
                                val user = User(
                                    userJson.getInt("id"),
                                    userJson.getString("username"),
                                    userJson.getString("email"),
                                    userJson.getString("gender")
                                )

                                //storing the user in shared preferences
                                SharedPrefManager.getInstance(applicationContext).userLogin(user)
                                //starting the profile activity
                                finish()
                                startActivity(Intent(applicationContext, MainActivity::class.java))
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    obj.getString("message"),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                },
                object : ErrorListener() {
                    fun onErrorResponse(error: VolleyError) {
                        Toast.makeText(applicationContext, error.getMessage(), Toast.LENGTH_SHORT)
                            .show()
                    }
                }) {
                @get:Throws(AuthFailureError::class)
                protected val params: Map<String, String>
                    protected get() {
                        val params: MutableMap<String, String> = HashMap()
                        params["username"] = username
                        params["password"] = password
                        return params
                    }
            }
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest)
    }
}
