package com.hashimnaqvillc.wasiqanaveesapp

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.hashimnaqvillc.wasiqanaveesapp.databinding.ActivityLoginBinding

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {
private lateinit var binding: ActivityLoginBinding
private lateinit var firebaseAuth: FirebaseAuth
private lateinit var mGoogleSignInClient: GoogleSignInClient
private val reqCode:Int=123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

binding.loginButton.setOnClickListener {
    val email = binding.emailInput.text.toString()
    val pass = binding.passwordInput.text.toString()

    if(email.isNotEmpty() && pass.isNotEmpty()) {
      firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
          if(it.isSuccessful){
              val intent = Intent(this, Page1Activity::class.java)
              startActivity(intent)
              finish()
          }
          else{
              Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT).show()
          }
      }
    }
    else{
        Toast.makeText(this,"Fields cannot be empty",Toast.LENGTH_SHORT).show()
    }
}



    binding.signupText.setOnClickListener {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }


//        Password visibility toggle logic

        val passwordInputLayout = binding.passwordInputLayout
        val passwordInput = binding.passwordInput

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




//        Sign up using next page shifter
        binding.orSignUpUsingText.setOnClickListener {
            val intent = Intent(this, Page1Activity::class.java)
            startActivity(intent)
        }








        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso)

        binding.googleSignInButton.setOnClickListener{
            Toast.makeText(this,"Logging In", Toast.LENGTH_SHORT).show()
            signInWithGoogle()
        }
    }

    private  fun signInWithGoogle(){
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent,reqCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==reqCode){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }


    private fun handleResult(completedTask: Task<GoogleSignInAccount>){
        try {
            val account: GoogleSignInAccount? =completedTask.getResult(ApiException::class.java)
            if (account != null) {
                updateUI(account)
            }
        } catch (e: ApiException){
            Toast.makeText(this,e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount){
        val credential= GoogleAuthProvider.getCredential(account.idToken,null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {task->
            if(task.isSuccessful) {
                //account.email.toString() <--- the user's email
                //account.displayName.toString()) <--- the user's name
                val intent = Intent(this, Page1Activity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
            startActivity(Intent(this, Page1Activity::class.java))
            finish()
        }
    }

}