package net.snaglobal.trile.wizeye.ui.fragment.login

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_login.*
import net.snaglobal.trile.wizeye.R
import net.snaglobal.trile.wizeye.data.remote.model.LoginResponse
import net.snaglobal.trile.wizeye.utils.KeyboardHelper

/**
 * @author trile
 * @since Sep 27, 2018 at 4:45 PM
 *
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {

    private val loginViewModel by lazy {
        ViewModelProviders.of(this).get(LoginViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Remove auto-focus from all EditTexts
        main_container.requestFocus()

        loginViewModel.loggedInChecker.observe(this, Observer {
            it?.let {
                if (it) findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
            }
        })
        loginViewModel.checkIfLoggedInOnAppOpen()

        // Check if all AutoCompleteTextViews are filled with credential info on Application Opening
        toggleLoginButtonEnabling(
                server_address_input.text.toString(),
                id_input.text.toString(),
                password_input.text.toString()
        )

        server_address_input.afterTextChanged {
            toggleLoginButtonEnabling(it, id_input.text.toString(), password_input.text.toString())
        }

        id_input.afterTextChanged {
            toggleLoginButtonEnabling(server_address_input.text.toString(), it, password_input.text.toString())
        }

        password_input.afterTextChanged {
            toggleLoginButtonEnabling(server_address_input.text.toString(), id_input.text.toString(), it)
        }
        password_input.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                KeyboardHelper.hideSoftKeyboard(activity, true)
                login_button.callOnClick()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        login_button.setOnClickListener {
            if (login_button.isEnabled) {
                KeyboardHelper.hideSoftKeyboard(activity, true)
                attempLogin()
            }
        }

        loginViewModel.loginSuccessful.observe(this, Observer { loginResponse: LoginResponse? ->
            loginResponse?.let {
                Log.d("API", "Token: ${it.token}")
            }
            enableLoginButton()
            findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
        })

        loginViewModel.loginError.observe(this, Observer { throwable: Throwable? ->
            error_message.visibility = View.VISIBLE
            enableLoginButton()

            throwable?.printStackTrace()
        })
    }

    private fun toggleLoginButtonEnabling(serverAddress: String, id: String, password: String) {
        if (serverAddress.isNotEmpty() && id.isNotEmpty() && password.isNotEmpty()) {
            enableLoginButton()
        } else if (login_button.background.constantState != context?.run {
                    ContextCompat.getDrawable(this,
                            R.drawable.login_button_disabled_background)?.constantState
                }) {
            disableLoginButton()
        } else {
            login_button.isEnabled = false
        }

        if (error_message.visibility == View.VISIBLE) {
            error_message.visibility = View.INVISIBLE
        }
    }

    private fun enableLoginButton() {
        login_button.isEnabled = true
        login_button.background = context?.run {
            ContextCompat.getDrawable(this, R.drawable.login_button_enabled_background)
        }
    }

    private fun disableLoginButton() {
        login_button.isEnabled = false
        login_button.background = context?.run {
            ContextCompat.getDrawable(this, R.drawable.login_button_disabled_background)
        }
    }

    private fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) =
            this.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    afterTextChanged.invoke(s.toString())
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }
            })

    private fun attempLogin() {
        disableLoginButton()

        val domain = server_address_input.text.toString()
        val username = id_input.text.toString()
        val password = password_input.text.toString()

        loginViewModel.login(domain, username, password)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment.
         *
         * @return A new instance of fragment LoginFragment.
         */
        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}
