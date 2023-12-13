package com.example.chatdiary2.data;

import androidx.compose.ui.graphics.Color
import com.example.chatdiary2.ui.theme.HappyColor
import java.time.LocalDate
import java.time.temporal.ChronoUnit

val happyData = listOf(
    HappyWeakData(
        startDate = LocalDate.of(2023, 1, 1), happyValues = listOf(
            HappyValue(startDate = LocalDate.of(2023, 1, 2), value = 0),
            HappyValue(startDate = LocalDate.of(2023, 1, 3), value = 29),
            HappyValue(startDate = LocalDate.of(2023, 1, 5), value = 69),
        )
    ), HappyWeakData(
        startDate = LocalDate.of(2023, 1, 11), happyValues = listOf(
            HappyValue(startDate = LocalDate.of(2023, 1, 11), value = 99),
            HappyValue(startDate = LocalDate.of(2023, 1, 12), value = 19),
            HappyValue(startDate = LocalDate.of(2023, 1, 14), value = 49),
            HappyValue(startDate = LocalDate.of(2023, 1, 16), value = 29),
        )
    )
)

data class HappyWeakData(
    val startDate: LocalDate,
    val happyValues: List<HappyValue>,
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
        happyValues.sortedBy(HappyValue::startDate).first().startDate
    }
    val end: LocalDate by lazy {
        happyValues.sortedBy(HappyValue::startDate).last().startDate
    }
    val total: Long by lazy {
        ChronoUnit.DAYS.between(start, end) + 1
    }

    val fractionOfTotalTime: Float by lazy {
        1f / 7f
    }

    fun daysAfterDiaryStart(happyValue: HappyValue): Long {
        return ChronoUnit.DAYS.between(start, happyValue.startDate)
    }
}

data class HappyValue(
    val startDate: LocalDate,
    val value: Int,
) {

    val type: HappyType by lazy {
        when (value) {
            in 0..20 -> HappyType.WantToDie
            in 21..40 -> HappyType.Sad
            in 41..60 -> HappyType.JustSoSo
            in 61..80 -> HappyType.Happy
            in 81..100 -> HappyType.VeryHappy
            else -> {
                HappyType.Empty
            }
        }
    }
}

enum class HappyType(val title: String, var color: Color) {
    VeryHappy(
        "Very Happy", HappyColor.VeryHappy
    ),
    Happy(
        "Happy", HappyColor.Happy
    ),
    JustSoSo(
        "Just So So", HappyColor.JustSoSo
    ),
    Sad(
        "Sad", HappyColor.Sad
    ),
    WantToDie(
        "Want to Die", HappyColor.WantToDie
    ),
    Empty(
        "No Diaries", HappyColor.Empty
    )
}
