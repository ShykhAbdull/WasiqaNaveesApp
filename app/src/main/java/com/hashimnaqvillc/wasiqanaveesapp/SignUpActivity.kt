package com.hashimnaqvillc.wasiqanaveesapp

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.hashimnaqvillc.wasiqanaveesapp.databinding.ActivitySignupBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.signupButton.setOnClickListener {
            val email = binding.signupEmailInput.text.toString()
            val pass = binding.signupPasswordInput.text.toString()
            val cnfrmPass = binding.signupCnfrmpasswordInput.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && cnfrmPass.isNotEmpty()) {
                if (pass == cnfrmPass) {
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else{
                    Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(this, "Empty fields are not allowed", Toast.LENGTH_SHORT).show()
            }
        }


//        Password visibility toggle logic
        val passwordInputLayout = binding.signupPasswordInputLayout
        val passwordInput = binding.signupPasswordInput

        passwordInputLayout.setEndIconOnClickListener {
            if (passwordInput.inputType == (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                // Show password
                passwordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                passwordInputLayout.endIconDrawable = ContextCompat.getDrawable(this, R.drawable.eye_open)
            } else {
                // Hide password
                passwordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                passwordInputLayout.endIconDrawable = ContextCompat.getDrawable(this, R.drawable.eye_close)
            }
            passwordInput.setSelection(passwordInput.text?.length ?: 0) // Maintain cursor position
        }



        val  cnfrmPasswordInputLayout = binding.signupCnfirmpasswordInputLayout
        val  cnfrmPasswordInput = binding.signupCnfrmpasswordInput

        cnfrmPasswordInputLayout.setEndIconOnClickListener {
            if (cnfrmPasswordInput.inputType == (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                // Show password
                cnfrmPasswordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                cnfrmPasswordInputLayout.endIconDrawable = ContextCompat.getDrawable(this, R.drawable.eye_open)
            } else {
                // Hide password
                cnfrmPasswordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                cnfrmPasswordInputLayout.endIconDrawable = ContextCompat.getDrawable(this, R.drawable.eye_close)
            }
            cnfrmPasswordInput.setSelection(cnfrmPasswordInput.text?.length ?: 0) // Maintain cursor position
        }


        binding.alreadyHaveAnAccount.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        }





    }

}