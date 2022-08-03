package org.hango.com.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import org.hango.com.MainActivity
import org.hango.com.databinding.ActivityLoginBinding
import org.json.JSONException
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener(
            View.OnClickListener
            //회원가입 버튼을 클릭시 수행
            {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            })

        binding.btnLogin.setOnClickListener(View.OnClickListener {
            val userID = binding.etId.getText().toString()
            val userPass = binding.etPass.getText().toString()
            val responseListener =
                Response.Listener<String?> { response ->
                    try {
                        val jasonObject = JSONObject(response)
                        val success = jasonObject.getBoolean("success")
                        if (success) { //회원등록 성공한 경우
                            val userID = jasonObject.getString("userID")
                            val userPass = jasonObject.getString("userPassword")
                            Toast.makeText(applicationContext, "로그인 성공", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.putExtra("log", "User")
                            intent.putExtra("userID", userID)
                            startActivity(intent)
                        } else { //회원등록 실패한 경우
                            Toast.makeText(applicationContext, "로그인 실패", Toast.LENGTH_SHORT).show()
                            return@Listener
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

            val errorListener = Response.ErrorListener {
                Toast.makeText(applicationContext, "회원가입 처리시 에러발생!", Toast.LENGTH_SHORT).show()
                return@ErrorListener
            }
            val loginRequest = LoginRequest(userID, userPass, responseListener)
            val queue = Volley.newRequestQueue(this@LoginActivity)
            queue.add(loginRequest)
        })
    }
}