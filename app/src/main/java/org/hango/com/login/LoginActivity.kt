package org.hango.com.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import org.hango.com.MainActivity
import org.hango.com.databinding.ActivityLoginBinding
import org.json.JSONException
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            error?.let {
                Toast.makeText(this, "토큰 정보 보기 실패", Toast.LENGTH_SHORT).show()
            }

            tokenInfo?.let{
                Toast.makeText(this, "토큰 정보 보기 성공", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
        }
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            error?.let{
                Log.e("KakaoLogin", "카카오계정으로 로그인 실패", error)
            }

            token?.let{
                Toast.makeText(this, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
        }
         // 카카오 로그인 버튼
        binding.kakaoLoginButton.setOnClickListener {

            if(UserApiClient.instance.isKakaoTalkLoginAvailable(this))
                UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
            else
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }


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
                            val userName = jasonObject.getString("userName")
                            val userProfile = jasonObject.getString("userProfile")
                            val userMbti = jasonObject.getString("userMbti")

                            Toast.makeText(applicationContext, "로그인 성공", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this@LoginActivity, MainActivity::class.java)

                            intent.putExtra("log", "User")
                            intent.putExtra("userID", userID)
                            intent.putExtra("userName", userName)
                            intent.putExtra("userProfile", userProfile)
                            intent.putExtra("userMbti", userMbti)


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