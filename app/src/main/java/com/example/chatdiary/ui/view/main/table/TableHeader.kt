import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.chatdiary.R
import com.example.chatdiary.data.HappyDateWithType
import com.example.chatdiary.ui.view.common.PieChart
import com.example.chatdiary.ui.view.main.table.HappyValueViewModel


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun HappyDaySummary(modifier: Modifier = Modifier, happyValueViewModel: HappyValueViewModel) {

    val happyValueState by happyValueViewModel.happyValueListState.collectAsState()
    val data = happyValueState.data.filter { day -> day.value != -1 }.map { day ->
        HappyDateWithType(day.startDate, day.value)
    }.groupBy { it.type.titleResource }.mapValues { it.value.size }

    val filterValues = mapOf(
        getPair(R.string.VeryHappy, data),
        getPair(R.string.Happy, data),
        getPair(R.string.JustSoSo, data),
        getPair(R.string.Sad, data),
        getPair(R.string.WantToDie, data),
    ).filterValues { it != null }
        .map { it.key to it.value!! }
        .toMap()
    PieChart(
        data = filterValues, modifier = modifier
    )
}

@Composable
private fun getPair(resId: Int, data: Map<Int, Int>): Pair<String, Int?> {
    return if (data.containsKey(resId)) {
        Pair(stringResource(id = resId), data[resId]);
    } else {
        Pair(stringResource(id = resId), null);

    }
}

