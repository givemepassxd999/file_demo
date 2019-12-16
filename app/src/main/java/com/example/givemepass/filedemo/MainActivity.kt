package com.example.givemepass.filedemo

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {
    private val fileName = "my_file"
    private val data = "apk file"
    private val tmpData = "apk file tmp"
    private lateinit var fileTmp: File

    private val isExternalStorageWritable: Boolean
        get() {
            val state = Environment.getExternalStorageState()
            return Environment.MEDIA_MOUNTED == state
        }

    private val isExternalStorageReadable: Boolean
        get() {
            val state = Environment.getExternalStorageState()
            return Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        internal_write.setOnClickListener { internalWrite() }
        internal_read.setOnClickListener { internalRead() }
        internal_tmp_write.setOnClickListener { internalTmpWrite() }
        internal_tmp_read.setOnClickListener { internalTmpRead() }
        extelnal_public_folder.setOnClickListener {
            when {
                isExternalStorageWritable -> //可寫
                    extelnalPublicCreateFoler()
                isExternalStorageReadable -> {
                    //可讀
                }
                else -> {
                    //不可寫不可讀
                }
            }
        }
        extelnal_private_folder.setOnClickListener { extelnalPrivateCreateFoler() }

    }

    //內部檔案寫入方式
    private fun internalWrite() {
        try {
            val outputStream = openFileOutput(fileName, Context.MODE_PRIVATE)
            outputStream.write(data.toByteArray())
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    //內部檔案讀取方式
    private fun internalRead() {
        try {
            val inputStream = openFileInput(fileName)
            val bytes = ByteArray(1024)
            val sb = StringBuffer()
            while (inputStream.read(bytes) != -1) {
                sb.append(String(bytes))
            }
            display_text.text = sb.toString()
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    //內部暫存檔案寫入方式
    private fun internalTmpWrite() {
        try {
            fileTmp = File.createTempFile(fileName, null, cacheDir)
            val output = FileOutputStream(fileTmp)
            output.write(tmpData.toByteArray())
            output.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    //內部暫存檔案讀取方式
    private fun internalTmpRead() {
        try {
            val inputStream = FileInputStream(fileTmp)
            val bytes = ByteArray(1024)
            val sb = StringBuffer()
            while (inputStream.read(bytes) != -1) {
                sb.append(String(bytes))
            }
            display_text.text = sb.toString()
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    //外部空間建立公開資料夾
    private fun extelnalPublicCreateFoler() {
        val dir = getExtermalStoragePublicDir("aa")
        val f = File(dir.path, fileName)
        try {
            val outputStream = FileOutputStream(f)
            outputStream.write(data.toByteArray())
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    //外部空間建立私有資料夾
    private fun extelnalPrivateCreateFoler() {
        val dir = getExtermalStoragePrivateDir("bb")
        val f = File(dir, fileName)
        try {
            val outputStream = FileOutputStream(f)
            outputStream.write(data.toByteArray())
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun getExtermalStoragePublicDir(albumName: String): File {
        val file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        if (file.mkdir()) {
            val f = File(file, albumName)
            if (f.mkdir()) {
                return f
            }
        }
        return File(file, albumName)
    }

    private fun getExtermalStoragePrivateDir(albumName: String): File {
        val file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), albumName)
        if (!file.mkdirs()) {
            Log.e("", "Directory not created or exist")
        }
        return file
    }
}
