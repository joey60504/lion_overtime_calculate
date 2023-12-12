package com.example.myapplication

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.textView3.setOnClickListener {
            binding.editTextText.setText("")
        }
        binding.textView8.setOnClickListener {
            binding.editTextText.setText("")
            binding.editTextText2.setText("")
            binding.editTextText3.setText("")
            binding.textView2.text = "計算結果："
            val overTimeAdapter = OverTimeAdapter(
                arrayListOf(),
                0.0,
                0.0
            )
            binding.recyclerOverTime.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = overTimeAdapter
            }
        }
        binding.textView.setOnClickListener {
            if (binding.editTextText.text.toString().isBlank() ||
                binding.editTextText2.text.toString().isBlank() ||
                binding.editTextText3.text.toString().isBlank()
            ) {
                AlertDialog.Builder(this@MainActivity)
                    .setMessage("單位不可為空")
                    .setPositiveButton("OK") { _, _ ->
                    }
                    .show()
            } else {
                hideKeyboard(this@MainActivity, binding.root)
                val rawData = binding.editTextText.text
                val modifyDataList = arrayListOf<OvertimeRecord>()
                try {
                    val dataList = rawData.split("結案").dropLast(1)
                    for (i in dataList.indices) {
                        val data = dataList[i].replace("\n", "").replace("\\s+".toRegex(), " ")

                        val dateTimePart =
                            data.substring(data.indexOf("~") - 17, data.indexOf("~") + 17)
                        val dataTimePartModify = dateTimePart.replace(" ", "-").replace("~", "")
                        val modifyData = data.replace(dateTimePart, dataTimePartModify)

                        val dataKindList = modifyData.split(" ").drop(1).dropLast(1)

                        modifyDataList.add(
                            OvertimeRecord(
                                number = dataKindList.getOrNull(0).toString(),
                                department = dataKindList.getOrNull(1).toString(),
                                name = dataKindList.getOrNull(2).toString(),
                                level = dataKindList.getOrNull(3).toString(),
                                date = dataKindList.getOrNull(4).toString(),
                                recognitionDate = dataKindList.getOrNull(5).toString(),
                                hours = dataKindList.getOrNull(6)?.toDouble(),
                                recognitionHours = dataKindList.getOrNull(7)?.toDouble(),
                                workContent = dataKindList.getOrNull(8).toString(),
                                salaryMonth = dataKindList.getOrNull(9).toString(),
                                signingProgress = "結案"
                            )
                        )
                    }
                    val overTime = modifyDataList.sumOf { it.recognitionHours ?: 0.0 }

                    val overTimePrice = modifyDataList.sumOf {
                        if ((it.recognitionHours ?: 0.0) < 2.0) {
                            (it.recognitionHours ?: 0.0) * binding.editTextText2.text.toString()
                                .toDouble()
                        } else {
                            ((it.recognitionHours
                                ?: 0.0) - 2.0) * binding.editTextText3.text.toString()
                                .toDouble() + (2.0 * binding.editTextText2.text.toString()
                                .toDouble())
                        }
                    }
                    binding.textView2.text =
                        "計算結果：\n總加班時數：${overTime}h 總加班金額：${overTimePrice}"

                    val overTimeAdapter = OverTimeAdapter(
                        modifyDataList,
                        binding.editTextText2.text.toString().toDouble(),
                        binding.editTextText3.text.toString().toDouble()
                    )
                    binding.recyclerOverTime.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(this@MainActivity)
                        adapter = overTimeAdapter
                    }
                } catch (e: Exception) {
                    AlertDialog.Builder(this@MainActivity)
                        .setMessage("資料錯誤${e}")
                        .setPositiveButton("OK") { _, _ -> }
                        .show()
                }
            }
        }
        setContentView(binding.root)
    }
}

fun hideKeyboard(context: Context, view: View) {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

data class OvertimeRecord(
    val number: String,
    val department: String,
    val name: String,
    val level: String,
    val date: String,
    val recognitionDate: String,
    val hours: Double?,
    val recognitionHours: Double?,
    val workContent: String,
    val salaryMonth: String,
    val signingProgress: String
)