import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest

class ValidateRequest(userID: String, listener: Response.Listener<String?>?, error: Response.ErrorListener) :
    StringRequest(Method.POST, URL, listener, error) {
    private val map: MutableMap<String, String>

    @Throws(AuthFailureError::class)
    override fun getParams(): Map<String, String>? {
        return map
    }

    companion object {
        //서버 url 설정(php파일 연동)
        private const val URL = "http://casio2978.dothome.co.kr/UserValidate.php"
    }

    init {
        map = HashMap()
        map["userID"] = userID
    }
}