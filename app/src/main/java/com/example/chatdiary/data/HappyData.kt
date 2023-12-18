package com.example.chatdiary.data;

import androidx.compose.ui.graphics.Color
import com.example.chatdiary.R
import com.example.chatdiary.ui.theme.HappyColor
import com.example.chatdiary.ui.theme.HappyColorShow
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class HappyWeakData(
    val startDate: LocalDate,
    val happyValues: List<HappyDateWithType>,
) {
    val ScoreEmoji: String by lazy {
        val sum = happyValues.sumOf { it.value }
        when (sum / happyValues.size) {
            in 0..40 -> "😖"
            in 41..60 -> "😏"
            in 60..70 -> "😴"
            in 71..100 -> "😃"
            else -> "🤷‍"
        }
    }


    val start: LocalDate by lazy {
        happyValues.sortedBy(HappyDateWithType::startDate).first().startDate
    }
    val end: LocalDate by lazy {
        happyValues.sortedBy(HappyDateWithType::startDate).last().startDate
    }
    val total: Long by lazy {
        ChronoUnit.DAYS.between(start, end) + 1
    }

    val fractionOfTotalTime: Float by lazy {
        1f / 7f
    }

    fun daysAfterDiaryStart(happyValue: HappyDateWithType): Long {
        return ChronoUnit.DAYS.between(start, happyValue.startDate)
    }
}

data class HappyValue(
    val startDate: LocalDate,
    val value: Int,
) {}

data class HappyDateWithType(
    val startDate: LocalDate,
    val value: Int,
    val type: HappyType,
) {
    constructor(
        startDate: LocalDate,
        value: Int,
    ) : this(
        startDate, value, when (value) {
            in 0..20 -> HappyType.WantToDie
            in 21..40 -> HappyType.Sad
            in 41..60 -> HappyType.JustSoSo
            in 61..80 -> HappyType.Happy
            in 81..100 -> HappyType.VeryHappy
            else -> {
                HappyType.Empty
            }
        }
    )
}

enum class HappyType(val titleResource: Int, var color: Color) {
    VeryHappy(
        R.string.VeryHappy, HappyColor.VeryHappy
    ),
    Happy(
        R.string.Happy, HappyColor.Happy
    ),
    JustSoSo(
        R.string.JustSoSo, HappyColor.JustSoSo
    ),
    Sad(
        R.string.Sad, HappyColor.Sad
    ),
    WantToDie(
        R.string.WantToDie, HappyColor.WantToDie
    ),
    Empty(
        R.string.Empty, HappyColor.Empty
    )
}

var typeColorToShowColor = mapOf<Color, Color>(
    Pair(HappyType.VeryHappy.color, HappyColorShow.VeryHappy),
    Pair(HappyType.Happy.color, HappyColorShow.Happy),
    Pair(HappyType.JustSoSo.color, HappyColorShow.JustSoSo),
    Pair(HappyType.Sad.color, HappyColorShow.Sad),
    Pair(HappyType.WantToDie.color, HappyColorShow.WantToDie),
    Pair(HappyType.Empty.color, HappyColorShow.Empty),
)

val happyGradientStops: List<Pair<Float, Color>> = HappyType.values().map {
    Pair(
        when (it) {
            HappyType.VeryHappy -> 0f / 4f
            HappyType.Happy -> 1f / 4f
            HappyType.JustSoSo -> 2f / 4f
            HappyType.Sad -> 3f / 4f
            HappyType.WantToDie -> 4f / 4f
            HappyType.Empty -> 6f / 4f

        }, it.color
    )
}

fun HappyType.heightPos(): Float {
    return when (this) {
        HappyType.VeryHappy -> 0f / 5f
        HappyType.Happy -> 1f / 5f
        HappyType.JustSoSo -> 2f / 5f
        HappyType.Sad -> 3f / 5f
        HappyType.WantToDie -> 4f / 5f
        HappyType.Empty -> 6f / 5f
    }
}