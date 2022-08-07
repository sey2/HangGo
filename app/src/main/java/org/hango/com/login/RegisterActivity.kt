package org.hango.com.login

import RegisterRequest
import ValidateRequest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import org.hango.com.databinding.ActivityRegisterBinding
import org.json.JSONException
import org.json.JSONObject


class RegisterActivity : AppCompatActivity() {
    private var dialog: AlertDialog? = null
    private var validate = false


    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.validateButton.setOnClickListener(
            View.OnClickListener
            //id중복체크
            {
                val userID = binding.etId.getText().toString()
                if (validate) {
                    return@OnClickListener
                }
                if (userID == "") {
                    val builder = AlertDialog.Builder(this@RegisterActivity)
                    dialog = builder.setMessage("아이디는 빈 칸일 수 없습니다")
                        .setPositiveButton("확인", null)
                        .create()
                    dialog!!.show()
                    return@OnClickListener
                }
                val responseListener =
                    Response.Listener<String?> { response ->
                        try {
                            val jsonResponse = JSONObject(response)
                            val success = jsonResponse.getBoolean("success")
                            if (success) {
                                val builder = AlertDialog.Builder(this@RegisterActivity)
                                dialog = builder.setMessage("사용할 수 있는 아이디입니다.")
                                    .setPositiveButton("확인", null)
                                    .create()
                                dialog!!.show()
                                binding.etId.setEnabled(false)
                                validate = true
                                binding.validateButton.setText("확인")
                            } else {
                                val builder = AlertDialog.Builder(this@RegisterActivity)
                                dialog = builder.setMessage("사용할 수 없는 아이디입니다.")
                                    .setNegativeButton("확인", null)
                                    .create()
                                dialog!!.show()
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                val error = Response.ErrorListener { error ->
                    Log.d("ERROR", "서버 Response 가져오기 실패: $error")}
                val validateRequest = ValidateRequest(userID, responseListener,error)
                val queue = Volley.newRequestQueue(this@RegisterActivity)
                queue.add(validateRequest)
            })

        binding.btnRegister.setOnClickListener(View.OnClickListener { //editText에 입력되어있는 값을 get(가져온다)해온다
            val userID = binding.etId.getText().toString()
            val userPass = binding.etPass.getText().toString()
            val userName = binding.etName.getText().toString()
            val PassCk = binding.etPassck.getText().toString()

            if(!validate)
                Toast.makeText(applicationContext, "아이디 중복 확인을 해주세요", Toast.LENGTH_LONG).show()
            else if(checkPass(binding)){
                Log.e("ERROR", "Password 입력 오류")
            }
            else {
                val responseListener =
                    Response.Listener<String?> { response ->

                        //volley
                        try {
                            val jasonObject = JSONObject(response) //Register2 php에 response
                            val success = jasonObject.getBoolean("success") //Register2 php에 sucess
                            if (userPass == PassCk) {
                                if (success) { //회원등록 성공한 경우
                                    Toast.makeText(
                                        applicationContext,
                                        "회원 등록 성공",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                    val intent = Intent(
                                        this@RegisterActivity,
                                        LoginActivity::class.java
                                    )
                                    startActivity(intent)
                                }
                            } else { //회원등록 실패한 경우
                                Toast.makeText(
                                    applicationContext,
                                    "비밀번호를 확인해 주세요",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                return@Listener
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                //서버로 volley를 이용해서 요청을 함
                val registerRequest = RegisterRequest(
                    userID,
                    userPass,
                    userName,
                    responseListener
                )
                val queue = Volley.newRequestQueue(this@RegisterActivity)
                queue.add(registerRequest)
            }
        })
    }

    private fun checkPass(binding : ActivityRegisterBinding) : Boolean{
        val pass = binding.etPass.getText()
        val passCk = binding.etPassck.getText()

        if(pass.length >=8 && passCk.length >= 8 && pass.equals(passCk)) return true
        else if(pass.length < 8)
            Toast.makeText(applicationContext, "비밀번호는 8자 이상이여야 합니다.", Toast.LENGTH_LONG).show()
        else if(passCk.length < 8)
            Toast.makeText(applicationContext,"비밀번호 확인칸을 다시 확인해주세요.", Toast.LENGTH_LONG).show()
         else if(pass.equals(passCk))
            Toast.makeText(applicationContext, "입력하신 비밀번호와 비밀번호 확인칸이 일치 하지 않습니다.",Toast.LENGTH_LONG).show()

        return false

    }
}