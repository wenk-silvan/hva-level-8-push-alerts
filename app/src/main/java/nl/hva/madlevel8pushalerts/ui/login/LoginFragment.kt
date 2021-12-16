package nl.hva.madlevel8pushalerts.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.ktx.Firebase
import nl.hva.madlevel8pushalerts.R

import com.google.firebase.auth.ktx.auth
import nl.hva.madlevel8pushalerts.MainActivity

import nl.hva.madlevel8pushalerts.databinding.FragmentLoginBinding

class EmailFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var passwordVisible = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {
            authenticate()
        }
        binding.ibPasswordVisibility.setOnClickListener {
            passwordVisible = !passwordVisible
            obscurePassword()
        }
        obscurePassword()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun authenticate() {
        val email = binding.tilEmail.editText?.text.toString().trim { it <= ' ' }
        val pw = binding.tilPassword.editText?.text.toString().trim { it <= ' ' }
        if (email.isEmpty() || pw.isEmpty()) {
            Toast.makeText(context, getString(R.string.fill_credentials), Toast.LENGTH_SHORT).show()
            return
        }

        Firebase.auth
            .signInWithEmailAndPassword(email, pw)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        context,
                        getString(R.string.login_successful),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    navigateToTaskScreen()
                } else {
                    Toast.makeText(context, (task.exception?.message), Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun navigateToTaskScreen() {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun obscurePassword() {
        binding.tietPassword.transformationMethod =
            if (passwordVisible) HideReturnsTransformationMethod.getInstance()
            else PasswordTransformationMethod.getInstance()
    }
}