package com.example.wydemo

import android.R.interpolator.linear
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_publish.*
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.image_dialog.view.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException


class PublishActivity : AppCompatActivity() {
    private lateinit var imgDialog: Dialog
    private val args = HashMap<String, String>()
    private val type:String="lostProperty"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publish)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
        }
        addImage.setOnClickListener {
            imgDialog = Dialog(this, R.style.JumpDialog)
            val layout = LayoutInflater.from(this)
                .inflate(R.layout.image_dialog, null, false)
            val camera = layout.text_camera
            val dcim = layout.text_dcim
            val cancel = layout.text_cancel
            camera.setOnClickListener {
                Log.d("sgsg", "camera")
                getImage.getCamera(this)
                imgDialog.dismiss()
            }
            dcim.setOnClickListener {
                Log.d("sgsg", "dcim")
                getImage.getDcim(this)
                imgDialog.dismiss()
            }
            cancel.setOnClickListener {
                Log.d("sgsg", "cancel")
                imgDialog.dismiss()
            }
            imgDialog.setContentView(layout) //将视图加入容器
            val dialogWindow: Window? = imgDialog.getWindow() //获得窗口
            dialogWindow?.setGravity(Gravity.BOTTOM) //放置在底部

            val lp: WindowManager.LayoutParams? = dialogWindow?.getAttributes() // 获取对话框当前的参数值

            lp?.x = 0 // 新位置X坐标
            lp?.y = 0 // 新位置Y坐标
            lp?.width = this.getResources().getDisplayMetrics().widthPixels // 宽度
            layout.measure(0, 0)
            lp?.height = layout.getMeasuredHeight()
            lp?.alpha = 1f // 透明度

            dialogWindow?.setAttributes(lp)
            imgDialog.setCancelable(false)
            imgDialog.show()
        }
        selectType.setOnClickListener {
            // TODO: 跳转选择界面
        }
        publish.setOnClickListener {
            args["amount"] = "1"
            args["contactNumber"] = "1"
            args["content"] = "1"
            args["gender"] = "1"
            args["location"] = "1"
            args["openid"] = "sgsg"
            args["picture"] = "1"
            args["tags"] = "tag"
            args["title"] = "1"
            Task.createTask(type, args)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            getImage.takePhoto -> {
                if (resultCode == Activity.RESULT_OK) {
                    // 将拍摄的照片显示出来
                    val bitmap =
                        BitmapFactory.decodeStream(contentResolver.openInputStream(getImage.imageUri))
//                    image1.setImageBitmap(rotateIfRequired(bitmap))
                    //压缩图片
                    val path = getImage.outputImage.path
                    Task.compressPicture(bitmap, path)
                    //上传图片
//                    Log.d("sgsg", path)
                    User.id?.let {
                        Task.uploadImg(it, path, object : Callback {
                            override fun onFailure(call: Call, e: IOException) {
                                e.printStackTrace()
                            }

                            override fun onResponse(call: Call, response: Response) {
                                val responseData: String? = response.body()?.string()
                                if (responseData != null) {
                                    Log.d("sgsg", responseData)
                                }
                            }
                        })
                    }

                }
            }
            getImage.fromAlbum -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    data.data?.let { uri ->
                        // 将选择的图片显示
                        val bitmap = getBitmapFromUri(uri)
//                        image1.setImageBitmap(bitmap)
                        val path = UriUtils.getFileAbsolutePath(this, uri)
                        //压缩图片
                        if (bitmap != null) {
                            if (path != null) {
                                Task.compressPicture(bitmap, path)
                            }
                        }
                        //上传图片
                        if (path != null) {
                            User.id?.let {
                                Task.uploadImg(it, path, object : Callback {
                                    override fun onFailure(call: Call, e: IOException) {
                                        e.printStackTrace()
                                    }

                                    override fun onResponse(call: Call, response: Response) {
                                        val responseData: String? = response.body()?.string()
                                        if (responseData != null) {
                                            Log.d("sgsg", responseData)
                                        }
                                    }
                                })
                            }
                        } else Log.d("sgsg", "path_error")

                    }
                }

            }
        }
    }

    private fun getBitmapFromUri(uri: Uri) = contentResolver
        .openFileDescriptor(uri, "r")?.use {
            BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
        }


    private fun rotateIfRequired(bitmap: Bitmap): Bitmap {
        val exif = ExifInterface(getImage.outputImage.path)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL)
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270)
            else -> bitmap
        }
    }

    private fun rotateBitmap(bitmap: Bitmap, degree: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height,
            matrix, true)
        bitmap.recycle() // 将不再需要的Bitmap对象回收
        return rotatedBitmap
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }
}