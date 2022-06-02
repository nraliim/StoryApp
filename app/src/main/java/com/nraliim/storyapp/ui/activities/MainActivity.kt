package com.nraliim.storyapp.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.nraliim.storyapp.R
import com.nraliim.storyapp.databinding.ActivityMainBinding
import com.nraliim.storyapp.model.User
import com.nraliim.storyapp.preferences.UserPreferences
import com.nraliim.storyapp.viewmodel.MainViewModel
import com.nraliim.storyapp.viewmodel.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

class MainActivity : AppCompatActivity() {

    private lateinit var user: User
    private lateinit var mainViewModel: MainViewModel
    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        buttonListener()
        setupViewModel()
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[MainViewModel::class.java]

        mainViewModel.getUser().observe(this) {
            user = User(
                it.name,
                it.email,
                it.password,
                it.userId,
                it.token,
                true
            )
            mainBinding.tvGreeting.text = getString(R.string.greeting, user.name)
        }
    }

    private fun buttonListener() {
        mainBinding.btnNext.setOnClickListener {
            val intent = Intent(this@MainActivity, ListStoryActivity::class.java)
            intent.putExtra(ListStoryActivity.EXTRA_USER, user)
            startActivity(intent)
        }
        mainBinding.btnLogout.setOnClickListener {
            mainViewModel.logout()
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.information))
                setMessage(getString(R.string.logout_success))
                setPositiveButton(getString(R.string.oke)) { _, _ ->
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    finish()
                }
                create()
                show()
            }
        }
    }

}