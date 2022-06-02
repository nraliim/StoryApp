package com.nraliim.storyapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.nraliim.storyapp.R
import com.nraliim.storyapp.databinding.ActivityRegisterBinding
import com.nraliim.storyapp.utils.Utils
import com.nraliim.storyapp.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerBinding: ActivityRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerBinding.root)
        supportActionBar?.hide()

        textListener()
        setButtonEnable()
        buttonListener()
        showLoading()
    }

    private fun textListener() {
        registerBinding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        registerBinding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        registerBinding.tvUser.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }
    }

    private fun setButtonEnable() {
        registerBinding.btnRegister.isEnabled =
            registerBinding.etEmail.text.toString().isNotEmpty() &&
                    registerBinding.etPassword.text.toString().isNotEmpty() &&
                    registerBinding.etPassword.text.toString().length >= 6 &&
                    Utils.isEmailValid(registerBinding.etEmail.text.toString())
    }

    private fun buttonListener() {
        registerBinding.btnRegister.setOnClickListener {
            val name = registerBinding.etName.text.toString()
            val email = registerBinding.etEmail.text.toString()
            val password = registerBinding.etPassword.text.toString()

            registerViewModel.register(name, email, password, object : Utils.ApiCallback {
                override fun onResponse(success: Boolean, message: String) {
                    showAlertDialog(success, message)
                }
            })
        }
    }

    private fun showAlertDialog(param: Boolean, message: String) {
        if (param) {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.information))
                setMessage(getString(R.string.register_success))
                setPositiveButton(getString(R.string.oke)) { _, _ ->
                    val intent = Intent(context, LoginActivity::class.java)
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
                setMessage(getString(R.string.register_failed) + ", $message")
                setPositiveButton(getString(R.string.oke)) { _, _ ->
                    registerBinding.progressBar.visibility = View.GONE
                }
                create()
                show()
            }
        }
    }

    private fun showLoading() {
        registerViewModel.isLoading.observe(this) {
            registerBinding.apply {
                if (it) progressBar.visibility = View.VISIBLE
                else progressBar.visibility = View.GONE
            }
        }
    }
}