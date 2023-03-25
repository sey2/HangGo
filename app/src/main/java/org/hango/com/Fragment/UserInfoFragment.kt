package org.hango.com.Fragment

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_home.*
import org.hango.com.Data.TravelViewModel
import org.hango.com.databinding.FragmentUserInfoBinding
import java.io.InputStream


class UserInfoFragment : Fragment() {
    private lateinit var userModel: TravelViewModel
    private lateinit var binding: FragmentUserInfoBinding

     override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         binding = FragmentUserInfoBinding.inflate(layoutInflater, container, false)

        userModel = ViewModelProvider(requireActivity()).get(TravelViewModel::class.java)

        // 사용자 프로필을 불러온다.
        Glide.with(requireActivity()).load(userModel!!.userinfo!!.value!!.userProfile)
            .into(binding.homeProfile)


        // 사용자 Mbti, 이름 설정
        binding.mbtiTextView!!.text = userModel!!.userinfo!!.value!!.userMbti
        binding.nameTextView!!.text = userModel!!.userinfo!!.value!!.userName + "님"

        binding.changeImageBtn.setOnClickListener { v ->
            val intent = Intent()
            intent.type = "image/*"
            intent.putExtra("crop", true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, 104)
        }
        binding.expBar!!.progress = 75

        setProfileFromCloud()

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        when (requestCode) {
            104 -> if (resultCode == Activity.RESULT_OK) {
                val imgUri: Uri? = intent?.data

                cloudUploadImg(imgUri) // 클라우드에 이미지 저장
                userModel!!.userinfo!!.value!!.profileUri = imgUri

                CropImage.activity(imgUri).setGuidelines(CropImageView.Guidelines.ON)
                    .start(requireActivity(), this)
            }

            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result: CropImage.ActivityResult = CropImage.getActivityResult(intent)

                result?.let{
                    val resultUri: Uri = result.getUri()
                    val resolver: ContentResolver = requireActivity().getContentResolver()
                    try {
                        val instream: InputStream? = resolver.openInputStream(resultUri)
                        val resultPhotoBitmap = BitmapFactory.decodeStream(instream)

                        //resultPhotoBitmap = getRoundedCornerBitmap(resultPhotoBitmap, 20);
                        profile.setImageBitmap(resultPhotoBitmap)
                        instream?.close()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                   }
                }
            }
        }

    fun cloudUploadImg(imgUri: Uri?) {
        val storage = FirebaseStorage.getInstance()
        val fileName = userModel!!.userinfo!!.value!!.userID + "profile.png"

        val storageRef = storage.getReference("profile/$fileName")
        val uploadTask = storageRef.putFile(imgUri!!)

        uploadTask.addOnSuccessListener { Log.d("test", "sucess") }
    }

    private fun setProfileFromCloud() {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val imgRef =
            storageRef.child("profile/" + userModel!!.userinfo!!.value!!.userID + "profile.png")
        if (imgRef != null) {
            imgRef.downloadUrl.addOnSuccessListener { uri ->
                Glide.with(requireActivity()).load(uri).into(binding.homeProfile)
            }.addOnFailureListener { e -> Log.d("test", e.toString()) }
        }
    }

    companion object {
        private const val REQUEST_CODE = 0
    }
}