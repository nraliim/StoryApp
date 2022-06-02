package com.nraliim.storyapp.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.nraliim.storyapp.R
import com.nraliim.storyapp.databinding.ActivityLoginBinding
import com.nraliim.storyapp.preferences.UserPreferences
import com.nraliim.storyapp.utils.Utils
import com.nraliim.storyapp.viewmodel.LoginViewModel
import com.nraliim.storyapp.viewmodel.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var loginBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)
        supportActionBar?.hide()

        setupViewModel()
        textListener()
        setButtonEnable()
        buttonListener()
        showLoading()
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[LoginViewModel::class.java]
    }

    private fun textListener() {
        loginBinding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        loginBinding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        loginBinding.tvNotUser.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            finish()
        }
    }

    private fun setButtonEnable() {
        val resultPass = loginBinding.etPassword.text
        val resultEmail = loginBinding.etEmail.text

        loginBinding.btnLogin.isEnabled = resultPass != null && resultEmail != null &&
                loginBinding.etPassword.text.toString().length >= 6 &&
                Utils.isEmailValid(loginBinding.etEmail.text.toString())
    }

    private fun buttonListener() {
        loginBinding.btnLogin.setOnClickListener {
            val email = loginBinding.etEmail.text.toString()
            val pass = loginBinding.etPassword.text.toString()

            loginViewModel.login(email, pass, object : Utils.ApiCallback {
                override fun onResponse(success: Boolean, message: String) {
                    showAlertDialog(success, message)
                }
            })
        }

    }

    private fun showLoading() {
        loginViewModel.isLoading.observe(this) {
            loginBinding.apply {
                if (it) progressBar.visibility = View.VISIBLE
                else progressBar.visibility = View.GONE
            }
        }
    }

    private fun showAlertDialog(param: Boolean, message: String) {
        if (param) {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.information))
                setMessage(getString(R.string.login_success))
                setPositiveButton(getString(R.string.oke)) { _, _ ->
                    val intent = Intent(context, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        } else {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.information))
                setMessage(getString(R.string.login_failed) + ", $message")
                setPositiveButton(getString(R.string.oke)) { _, _ ->
                    loginBinding.progressBar.visibility = View.GONE
                }
                create()
                show()

            }
        }
    }

}