package com.LeeHe.interstellartime

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // 物理常数
    private val G = 6.67430e-11
    private val C = 299792458.0
    private val M = 1.5e39 // Gargantua质量
    private val R = 1.48e11 // 米勒星球轨道半径

    // 电影中的时间膨胀比 (1小时米勒时间 = 7地球年)
    private val MOVIE_RATIO = 1.0 / (7.0 * 365.0 * 24.0)

    // 声明视图变量
    private lateinit var etInputTime: EditText
    private lateinit var rgCalculationType: RadioGroup
    private lateinit var btnCalculate: Button
    private lateinit var tvResult: TextView
    private lateinit var rbEarthToMiller: RadioButton
    private lateinit var rbMillerToEarth: RadioButton

    // 添加一个标志来跟踪布局是否已经创建
    private var isLayoutCreated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 检查布局是否已经创建，避免重复创建
        if (!isLayoutCreated) {
            // 完全通过代码创建界面
            createLayoutProgrammatically()
            isLayoutCreated = true
        }

        setupListeners()
    }

    @SuppressLint("UseKtx")
    private fun createLayoutProgrammatically() {
        // 创建主滚动视图
        val scrollView = ScrollView(this).apply {
            setPadding(50, 50, 50, 50)
            setBackgroundColor(Color.parseColor("#0C0032"))
        }

        // 创建主垂直布局
        val mainLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        // 标题
        val title = TextView(this).apply {
            text = "星际穿越时间计算器"
            textSize = 24f
            setTextColor(Color.WHITE)
            typeface = Typeface.DEFAULT_BOLD
            gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 50
            }
        }

        // 输入标签
        val inputLabel = TextView(this).apply {
            text = "输入时间（年）："
            textSize = 16f
            setTextColor(Color.WHITE)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 20
            }
        }

        // 输入框
        etInputTime = EditText(this).apply {
            hint = "请输入时间数值"
            setTextColor(Color.WHITE)
            setHintTextColor(Color.parseColor("#CCCCCC"))
            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            setPadding(30, 20, 30, 20)
            background = createEditTextBackground()
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        // 计算类型标签
        val typeLabel = TextView(this).apply {
            text = "计算类型："
            textSize = 16f
            setTextColor(Color.WHITE)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 40
                bottomMargin = 20
            }
        }

        // 单选按钮组 - 先创建 RadioGroup
        rgCalculationType = RadioGroup(this).apply {
            orientation = RadioGroup.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        // 第一个单选按钮
        rbEarthToMiller = RadioButton(this).apply {
            id = 1001
                text = "地球时间 → 米勒星球时间"
            setTextColor(Color.WHITE)
            isChecked = true
            layoutParams = RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.MATCH_PARENT,
                RadioGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 10
            }
        }

        // 第二个单选按钮
        rbMillerToEarth = RadioButton(this).apply {
            id = 1002
            text = "米勒星球时间 → 地球时间"
            setTextColor(Color.WHITE)
            layoutParams = RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.MATCH_PARENT,
                RadioGroup.LayoutParams.WRAP_CONTENT
            )
        }

        // 将 RadioButton 添加到 RadioGroup
        rgCalculationType.addView(rbEarthToMiller)
        rgCalculationType.addView(rbMillerToEarth)

        // 计算按钮
        btnCalculate = Button(this).apply {
            text = "计算时间膨胀"
            setBackgroundColor(Color.parseColor("#4A90E2"))
            setTextColor(Color.WHITE)
            textSize = 16f
            setPadding(0, 25, 0, 25)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 60
                bottomMargin = 60
            }
        }

        // 结果标签
        val resultLabel = TextView(this).apply {
            text = "计算结果："
            textSize = 16f
            setTextColor(Color.WHITE)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 20
            }
        }

        // 结果显示框
        tvResult = TextView(this).apply {
            text = "请输入数值并点击计算..."
            textSize = 14f
            setTextColor(Color.WHITE)
            setPadding(40, 40, 40, 40)
            background = createResultBackground()
            typeface = Typeface.DEFAULT_BOLD
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        // 添加所有视图到主布局
        mainLayout.addView(title)
        mainLayout.addView(inputLabel)
        mainLayout.addView(etInputTime)
        mainLayout.addView(typeLabel)
        mainLayout.addView(rgCalculationType)  // 这里添加的是已经包含 RadioButton 的 RadioGroup
        mainLayout.addView(btnCalculate)
        mainLayout.addView(resultLabel)
        mainLayout.addView(tvResult)

        scrollView.addView(mainLayout)
        setContentView(scrollView)
    }

    /**
     * 创建输入框背景
     */
    @SuppressLint("UseKtx")
    private fun createEditTextBackground(): android.graphics.drawable.GradientDrawable {
        return android.graphics.drawable.GradientDrawable().apply {
            setColor(Color.parseColor("#1AFFFFFF"))
            cornerRadius = 16f
            setStroke(2, Color.parseColor("#4A90E2"))
        }
    }

    /**
     * 创建结果框背景
     */
    @SuppressLint("UseKtx")
    private fun createResultBackground(): android.graphics.drawable.GradientDrawable {
        return android.graphics.drawable.GradientDrawable().apply {
            setColor(Color.parseColor("#2A1E50"))
            cornerRadius = 16f
            setStroke(2, Color.parseColor("#4A90E2"))
        }
    }

    private fun setupListeners() {
        btnCalculate.setOnClickListener {
            calculateTimeDilation()
        }
    }

    @SuppressLint("DefaultLocale")
    private fun calculateTimeDilation() {
        val inputStr = etInputTime.text.toString().trim()

        if (inputStr.isEmpty()) {
            showToast("请输入时间值")
            return
        }

        try {
            val inputTime = inputStr.toDouble()
            val result: String

            // 电影设定：1小时米勒时间 = 7地球年
            val earthYearsPerMillerHour = 7.0

            if (rbEarthToMiller.isChecked) {
                // 地球时间 → 米勒星球时间
                // 例如：输入1地球年，米勒时间 = 1 / (7*365*24) 年
                val millerYears = (inputTime * 365 * 24) / (earthYearsPerMillerHour * 365 * 24)
                val millerDays = millerYears * 365
                val millerHours = millerDays * 24
                val millerMinutes = millerHours * 60

                result = String.format(
                    "在地球度过 %.3f 年 ≈ 在米勒星球：\n%.6f 年\n%.4f 天\n%.2f 小时\n%.1f 分钟",
                    inputTime, millerYears, millerDays, millerHours, millerMinutes
                )
            } else {
                // 米勒星球时间 → 地球时间
                // 例如：输入1米勒年，地球时间 = 1 * (7*365*24) 年
                val earthYears = inputTime * earthYearsPerMillerHour
                val earthDays = earthYears * 365
                val earthHours = earthDays * 24

                result = String.format(
                    "在米勒星球度过 %.3f 年 ≈ 在地球：\n%.2f 年\n%.0f 天\n%.0f 小时",
                    inputTime, earthYears, earthDays, earthHours
                )
            }

            tvResult.text = result

        } catch (_: NumberFormatException) {
            showToast("请输入有效的时间数值")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
