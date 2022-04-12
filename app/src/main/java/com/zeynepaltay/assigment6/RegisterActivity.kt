package com.zeynepaltay.assigment6

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import org.json.JSONException
import org.json.JSONObject


class RegisterActivity : AppCompatActivity() {
    var editTextUsername: EditText? = null
    var editTextEmail: EditText? = null
    var editTextPassword: EditText? = null
    var radioGroupGender: RadioGroup? = null
    var progressBar: ProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        progressBar = findViewById(R.id.progressBar)

        //if the user is already logged in we will directly start the MainActivity (profile) activity
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish()
            startActivity(Intent(this, MainActivity::class.java))
            return
        }
        editTextUsername = findViewById(R.id.editTextUsername)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        radioGroupGender = findViewById(R.id.radioGender)
        findViewById<View>(R.id.buttonRegister).setOnClickListener { //if user pressed on button register
            //here we will register the user to server
            registerUser()
        }
        findViewById<View>(R.id.textViewLogin).setOnClickListener { //if user pressed on textview that already register open LoginActivity
            finish()
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }
    }

    private fun registerUser() {
        val username = editTextUsername!!.text.toString().trim { it <= ' ' }
        val email = editTextEmail!!.text.toString().trim { it <= ' ' }
        val password = editTextPassword!!.text.toString().trim { it <= ' ' }
        val gender =
            (findViewById<View>(radioGroupGender!!.checkedRadioButtonId) as RadioButton).text.toString()

        //first we will do the validations
        if (TextUtils.isEmpty(username)) {
            editTextUsername!!.error = "Please enter username"
            editTextUsername!!.requestFocus()
            return
        }
        if (TextUtils.isEmpty(email)) {
            editTextEmail!!.error = "Please enter your email"
            editTextEmail!!.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail!!.error = "Enter a valid email"
            editTextEmail!!.requestFocus()
            return
        }
        if (TextUtils.isEmpty(password)) {
            editTextPassword!!.error = "Enter a password"
            editTextPassword!!.requestFocus()
            return
        }
        val stringRequest: StringRequest =
            object : StringRequest(Request.Method.POST, URLs.URL_REGISTER,
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
                        params["email"] = email
                        params["password"] = password
                        params["gender"] = gender
                        return params
                    }
            }
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest)
    }
}
