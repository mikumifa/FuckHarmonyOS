package com.example.chatdiary2.ui.view.main.table

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.util.lerp
import com.example.chatdiary2.R
import com.example.chatdiary2.data.HappyDateWithType
import com.example.chatdiary2.data.HappyType
import com.example.chatdiary2.data.HappyValue
import com.example.chatdiary2.data.HappyWeakData
import com.example.chatdiary2.ui.view.main.table.TimeGraphScope.timeGraphBar
import eu.kanade.presentation.theme.colorscheme.md_theme_dark_onPrimaryContainer
import eu.kanade.presentation.theme.colorscheme.md_theme_light_outlineVariant
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.util.Locale
import kotlin.math.roundToInt

private val lineThickness = 2.dp
private val barHeight = 24.dp
private const val animationDuration = 500
private val textPadding = 4.dp
private val expandedLength = HappyType.values().size * barHeight

@Composable
@Preview
fun DayLabelPreview() {
    val dayLabels = @Composable { repeat(10) { DayLabel("date") } }
    dayLabels()
}

@Composable
private fun DayLabel(dateInfo: String) {
    Text(
        text = dateInfo,
        modifier = Modifier
            .height(24.dp)
            .padding(start = 8.dp, end = 24.dp),
        style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.onSecondaryContainer),
        textAlign = TextAlign.Center,
    )
}


@Composable
fun WeekHeader(week: List<String>) {
    Row(
        Modifier
            .padding(bottom = 16.dp)
            .drawBehind {
                val brush = Brush.linearGradient(
                    listOf(
                        md_theme_dark_onPrimaryContainer, md_theme_light_outlineVariant
                    )
                )

                drawRoundRect(
                    brush,
                    cornerRadius = CornerRadius(10.dp.toPx(), 10.dp.toPx()),
                )
            }) {
        week.forEach {
            Text(
                text = it,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .width(50.dp)
                    .padding(vertical = 4.dp),
                style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onSecondaryContainer)
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HappyBar(
    happyWeakData: HappyWeakData,
    modifier: Modifier = Modifier,
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val transition = updateTransition(targetState = isExpanded, label = "expanded")
    Column(modifier = modifier.clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        isExpanded = !isExpanded
    }) {
        HappyRoundedBar(
            happyWeakData, transition
        )
        transition.AnimatedVisibility(enter = fadeIn(animationSpec = tween(animationDuration)) + expandVertically(
            animationSpec = tween(animationDuration)
        ), exit = fadeOut(animationSpec = tween(animationDuration)) + shrinkVertically(
            animationSpec = tween(animationDuration)
        ), content = {
            DetailLegend()
        }, visible = { it })
    }
}

private val happyGradientStops: List<Pair<Float, Color>> = HappyType.values().map {
    Pair(
        when (it) {
            HappyType.VeryHappy -> 1f / 7f
            HappyType.Happy -> 2f / 7f
            HappyType.JustSoSo -> 3f / 7f
            HappyType.Sad -> 4f / 7f
            HappyType.WantToDie -> 5f / 7f
            HappyType.Empty -> 7f / 7f
        }, it.color
    )
}

private fun HappyType.heightPos(): Float {
    return when (this) {
        HappyType.VeryHappy -> 1f / 7f
        HappyType.Happy -> 2f / 7f
        HappyType.JustSoSo -> 3f / 7f
        HappyType.Sad -> 4f / 7f
        HappyType.WantToDie -> 5f / 7f
        HappyType.Empty -> 7f / 7f

    }
}

@Preview
@Composable
private fun DetailLegend() {
    Column {
        Row(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            HappyType.values().take(3).forEach {
                LegendItem(it)
            }
        }

        Row(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            HappyType.values().takeLast(3).forEach {
                LegendItem(it)
            }
        }
    }
}

@Composable
private fun LegendItem(happyType: HappyType) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color = happyType.color)
        )
        Text(
            stringResource(id = happyType.titleResource),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

@Composable
private fun HappyRoundedBar(
    happyWeakData: HappyWeakData,
    transition: Transition<Boolean>,
) {

    val textMeasurer = rememberTextMeasurer()

    val height by transition.animateDp(label = "height", transitionSpec = {
        spring(
            dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow
        )
    }) { targetExpanded ->
        if (targetExpanded) expandedLength else barHeight
    }
    val animationProgress by transition.animateFloat(label = "progress", transitionSpec = {
        spring(
            dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow
        )
    }) { target ->
        if (target) 1f else 0f
    }

    Spacer(modifier = Modifier
        .height(height)
        .fillMaxWidth()
        .drawWithCache {
            val cornerRadiusStartPx = 2.dp.toPx()
            val collapsedCornerRadiusPx = 10.dp.toPx()
            val animatedCornerRadius = CornerRadius(
                lerp(cornerRadiusStartPx, collapsedCornerRadiusPx, (1 - animationProgress))
            )

            val lineThicknessPx = lineThickness.toPx()
            val roundedRectPath = Path()
            roundedRectPath.addRoundRect(
                RoundRect(
                    rect = Rect(
                        Offset(x = 0f, y = -lineThicknessPx / 2f), Size(
                            this.size.width + lineThicknessPx * 2,
                            this.size.height + lineThicknessPx
                        )
                    ), cornerRadius = animatedCornerRadius
                )
            )
            val roundedCornerStroke = Stroke(
                lineThicknessPx,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round,
                pathEffect = PathEffect.cornerPathEffect(
                    cornerRadiusStartPx * animationProgress
                )
            )
            val happyPath = generateWeakPath(
                canvasSize = this.size,
                happyWeakData = happyWeakData,
                width = this.size.width,
                barHeightPx = barHeight.toPx(),
                heightAnimation = animationProgress,
                lineThicknessPx = lineThickness.toPx() / 2f
            )
            val gradientBrush = Brush.verticalGradient(
                colorStops = happyGradientStops.toTypedArray(),
                startY = 0f,
                endY = HappyType.values().size * barHeight.toPx()
            )
            onDrawBehind {
                drawHappyBar(
                    roundedRectPath = roundedRectPath,
                    happyPath = happyPath,
                    gradientBrush = gradientBrush,
                    roundedCornerStroke = roundedCornerStroke,
                    animationProgress = animationProgress,
                    textResult = textMeasurer.measure(AnnotatedString(happyWeakData.ScoreEmoji)),
                    cornerRadiusStartPx = cornerRadiusStartPx
                )
            }
        })

}

/**
 * Generate the path for the different sleep periods.
 */
private fun generateWeakPath(
    canvasSize: Size,
    happyWeakData: HappyWeakData,
    width: Float,
    barHeightPx: Float,
    heightAnimation: Float,
    lineThicknessPx: Float,
): Path {
    val path = Path()
    var previousHappyValue: HappyDateWithType? = null
    path.moveTo(0f, 0f)
    val newWeekDate = ArrayList<HappyDateWithType>()
    for (i in 0 until happyWeakData.total) {
        val nowDate = happyWeakData.start.plusDays(i)
        newWeekDate.add(
            HappyDateWithType(
                startDate = nowDate,
                value = happyWeakData.happyValues.find { it.startDate == nowDate }?.value ?: -1
            )
        )
    }
    newWeekDate.forEach {
        val percentageOfTotal = happyWeakData.fractionOfTotalTime
        val periodWidth = percentageOfTotal * width
        val startOffsetPercentage = happyWeakData.daysAfterDiaryStart(it) / 7f
        val halfBarHeight = canvasSize.height / HappyType.values().size / 2f
        val offset = if (previousHappyValue == null) {
            0f
        } else {
            halfBarHeight
        }
        val offsetY = lerp(
            0f, it.type.heightPos() * canvasSize.height, heightAnimation
        )
        // step 1 - draw a line from previous sleep period to current
        if (previousHappyValue != null) {
            path.lineTo(
                x = startOffsetPercentage * width + lineThicknessPx, y = offsetY + offset
            )
        }
        // step 2 - add the current sleep period as rectangle to path
        path.addRect(
            rect = Rect(
                offset = Offset(x = startOffsetPercentage * width + lineThicknessPx, y = offsetY),
                size = canvasSize.copy(width = periodWidth, height = barHeightPx)
            )
        )
        // step 3 - 移动到末梢
        path.moveTo(
            x = startOffsetPercentage * width + periodWidth + lineThicknessPx,
            y = offsetY + halfBarHeight
        )

        previousHappyValue = it
    }
    return path
}

private fun DrawScope.drawHappyBar(
    roundedRectPath: Path,
    happyPath: Path,
    gradientBrush: Brush,
    roundedCornerStroke: Stroke,
    animationProgress: Float,
    textResult: TextLayoutResult,
    cornerRadiusStartPx: Float,
) {
    clipPath(roundedRectPath) {
        drawPath(happyPath, brush = gradientBrush)
        drawPath(
            happyPath, style = roundedCornerStroke, brush = gradientBrush
        )
    }

    translate(left = -animationProgress * (textResult.size.width + textPadding.toPx())) {
        drawText(
            textResult, topLeft = Offset(textPadding.toPx(), cornerRadiusStartPx)
        )
    }
}

private fun splitIntoHappyWeakData(happyValues: List<HappyDateWithType>): List<HappyWeakData> {
    if (happyValues.isEmpty()) return emptyList()
    val result = mutableListOf<HappyWeakData>()

    val groupedByWeek = happyValues.groupBy {
        it.startDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
    }

    for ((weekStartDate, values) in groupedByWeek) {
        val weekDays = mutableListOf<LocalDate>()
        var currentDay = weekStartDate
        for (i in 0 until 7) {
            weekDays.add(currentDay)
            currentDay = currentDay.plusDays(1)
        }

        val happyWeakData =
            HappyWeakData(startDate = weekStartDate, happyValues = weekDays.map { day ->
                values.find { it.startDate == day } ?: HappyDateWithType(
                    startDate = day, value = -1
                )
            })
        result.add(happyWeakData)
    }

    return result
}

@Composable
fun TimeGraph(
    happyDataOfDay: List<HappyValue>,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val weeks = context.resources.getStringArray(R.array.week_days).toList()
    val happyDateWithTypeOfType =
        happyDataOfDay.map { day -> HappyDateWithType(day.startDate, day.value) }
    val happyData = splitIntoHappyWeakData(happyDateWithTypeOfType)
    val dayLabels = @Composable {
        happyData.forEach {
            val formatter = DateTimeFormatter.ofPattern("MMM dd", Locale.getDefault())
            DayLabel(dateInfo = it.startDate.format(formatter))
        }
    }
    val weekHeader = @Composable { WeekHeader(weeks) }

    val bars = @Composable {
        happyData.forEach {
            HappyBar(
                happyWeakData = it, modifier = Modifier
                    .padding(bottom = 8.dp)
                    //设置data, 这样后面测量时候用到这些data来布局
                    .timeGraphBar(
                        start = it.start,
                        end = it.end,
                        startTime = it.startDate,
                    )
            )
        }
    }
    // MultiContentMeasurePolicy
    // 测量子类的长度来确定父类的尺寸
    Layout(
        contents = listOf(weekHeader, dayLabels, bars), modifier = modifier.padding(bottom = 32.dp)
    ) {
            (weakMeasurables, dayLabelMeasurables, barMeasureables),
            constraints,
        ->
        require(weakMeasurables.size == 1) {
            "hoursHeader should only emit one composable"
        }
        //第1步，测量所有子项
        val weekHeaderPlaceables = weakMeasurables.first().measure(constraints)

        val dayLabelPlaceables = dayLabelMeasurables.map { measurable ->
            val placeable = measurable.measure(constraints)
            placeable
        }
        //计算高度
        var totalHeight = weekHeaderPlaceables.height

        val barPlaceables = barMeasureables.map { measurable ->
            val barWidth = weekHeaderPlaceables.width

            val barPlaceable = measurable.measure(
                constraints.copy(
                    minWidth = barWidth, maxWidth = barWidth
                )
            )
            totalHeight += barPlaceable.height
            barPlaceable
        }
        //计算宽度
        val totalWidth = dayLabelPlaceables.first().width + weekHeaderPlaceables.width

        layout(totalWidth, totalHeight) {
            //设置bar的
            val xPosition = dayLabelPlaceables.first().width
            var yPosition = weekHeaderPlaceables.height

            weekHeaderPlaceables.place(xPosition, 0)

            barPlaceables.forEachIndexed { index, barPlaceable ->
                //得到parentData
                val barParentData = barPlaceable.parentData as TimeGraphParentData
                val barOffset = (barParentData.offset * weekHeaderPlaceables.width).roundToInt()
                barPlaceable.place(xPosition + barOffset, yPosition)
                val dayLabelPlaceable = dayLabelPlaceables[index]
                dayLabelPlaceable.place(x = 0, y = yPosition)

                yPosition += barPlaceable.height
            }
        }
    }
}

@LayoutScopeMarker
@Immutable
object TimeGraphScope {
    @Stable
    fun Modifier.timeGraphBar(
        start: LocalDate, end: LocalDate, startTime: LocalDate
    ): Modifier {
        return then(
            TimeGraphParentData(
                offset = ChronoUnit.DAYS.between(startTime, start).toFloat() / 7f
            )
        )
    }
}

class TimeGraphParentData(
    val offset: Float,
) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?) = this@TimeGraphParentData
}
