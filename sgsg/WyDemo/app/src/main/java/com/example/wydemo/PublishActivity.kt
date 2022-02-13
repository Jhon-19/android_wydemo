package com.example.wydemo

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
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_lecture_content.*
import kotlinx.android.synthetic.main.activity_publish.*
import kotlinx.android.synthetic.main.image_dialog.view.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException


class PublishActivity : AppCompatActivity() {
    private lateinit var imgDialog: Dialog

    //图片放大显示
    private lateinit var dialog: Dialog
    private lateinit var image: ImageView

    //创建任务请求
    private val args = HashMap<String, String>()
    private val type: String = "lostProperty"

    //recyclerView的数据
    private val bitmaps = ArrayList<UploadImage>()

    //上传图片的url列表
    private val imgUrls = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publish)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
        }
        init()
        //展示上传的图片
        val layoutManager = LinearLayoutManager(applicationContext)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        imgRecyclerView.layoutManager = layoutManager
        val adapter = UploadImageAdapter(bitmaps, object : OnClickCallback2 {
            override fun onClick(view: View, position: Int, type: Int) {
                if (type == 0) {
                    //放大
                    image.setImageBitmap(bitmaps[position].bitmap)
                    dialog.show()
                } else {
                    //remove
                    bitmaps.removeAt(position)
                    imgUrls.removeAt(position)
                    imgRecyclerView.adapter?.notifyDataSetChanged()
                }
            }
        })
        imgRecyclerView.adapter = adapter

        //上传图片按钮
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
        //选择任务类型
        selectType.setOnClickListener {
            // TODO: 跳转选择界面
        }
        //发布任务
        publish.setOnClickListener {
            args["amount"] = "1"
            args["contactNumber"] = "1"
            val content = content.text.toString()
            if (content == "") {
                Toast.makeText(this, "创建失败,内容不能为空", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            args["content"] = content
            args["gender"] = "1"
            args["location"] = "1"
            args["openid"] = User.id!!
            args["picture"] = ","
            for (i in imgUrls) {
                args["picture"] = args["picture"] + i + ","
            }
            args["picture"]?.let { it1 -> Log.d("sgsg", it1) }
            args["tags"] = "tag"
            args["title"] = "1"
            Task.createTask(type, args)
            Toast.makeText(this, "创建成功", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            getImage.takePhoto -> {
                if (resultCode == Activity.RESULT_OK) {
                    // 将拍摄的照片显示出来
                    var bitmap =
                        BitmapFactory.decodeStream(contentResolver.openInputStream(getImage.imageUri))
                    bitmap = rotateIfRequired(bitmap)
                    bitmaps.add(UploadImage(bitmap))
                    imgRecyclerView.adapter?.notifyDataSetChanged()
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
                                    val jsonObj = JSONObject(responseData)
                                    val url = jsonObj.getString("data")
                                    Log.d("sgsg", url)
                                    imgUrls.add(url)
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
                        var bitmap = getBitmapFromUri(uri)
//                        bitmap = rotateIfRequired(bitmap!!)
                        bitmap?.let { UploadImage(it) }?.let { bitmaps.add(it) }
                        imgRecyclerView.adapter?.notifyDataSetChanged()
                        val path = UriUtils.getFileAbsolutePath(this, uri)
                        //压缩图片
                        if (bitmap != null) {
                            if (path != null) {
                                Task.compressPicture(bitmap!!, path)
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
                                            val jsonObj = JSONObject(responseData)
                                            val url = jsonObj.getString("data")
                                            Log.d("sgsg", url)
                                            imgUrls.add(url)
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

    //放大
    private fun init() {
        //展示在dialog上面的大图
        dialog = Dialog(this, R.style.FullActivity)
        image = ImageView(this)
        //示例图
        image.setImageResource(R.drawable.lecture_image)
        dialog.setContentView(image)
        //点击消失
        image.setOnClickListener { dialog.dismiss() }
    }
}