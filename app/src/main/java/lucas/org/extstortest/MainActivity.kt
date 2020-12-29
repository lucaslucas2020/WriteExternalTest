package lucas.org.extstortest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.io.*
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {
    private val filepath = "MyFileStorage"
    private var myExternalFile: File? = null
    private val isExternalStorageReadOnly: Boolean
        get() {
            val extStorageState = Environment.getExternalStorageState()
            return Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)
        }

    private val isExternalStorageAvailable: Boolean
        get() {
            val extStorage = Environment.getExternalStorageState()
            return Environment.MEDIA_MOUNTED.equals(extStorage)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fileName = findViewById<EditText>(R.id.editTextFile)
        val fileData = findViewById<EditText>(R.id.editTextData)
        val saveButton = findViewById<Button>(R.id.button_save) as Button
        val viewButton = findViewById<Button>(R.id.button_view) as Button

        saveButton.setOnClickListener {
            myExternalFile = File(getExternalFilesDir(filepath), fileName.text.toString())
            try {
                val fileOutPutStream = FileOutputStream(myExternalFile)
                fileOutPutStream.write(fileData.text.toString().toByteArray())
                fileOutPutStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            Toast.makeText(applicationContext, "data save", Toast.LENGTH_SHORT).show()

        }

        viewButton.setOnClickListener {
            myExternalFile = File(getExternalFilesDir(filepath), fileName.text.toString())

            val fileName = fileName.text.toString()
            myExternalFile = File(getExternalFilesDir(filepath), fileName)
            if (fileName.toString() != null && fileName.toString().trim() != "") {
                var fileInputStream = FileInputStream(myExternalFile)
                var inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
                val bufferReader: BufferedReader = BufferedReader(inputStreamReader)
                val stringBuilder: StringBuilder = StringBuilder()
                var text: String? = null
                while ({ text = bufferReader.readLine();text }
                        () != null) {
                    stringBuilder.append(text)
                }
                fileInputStream.close()
                Toast.makeText(applicationContext, stringBuilder.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        }

        if (!isExternalStorageAvailable || isExternalStorageReadOnly) {
                saveButton.isEnabled = false
            }
        }
    }