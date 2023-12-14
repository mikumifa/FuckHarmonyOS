import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chatdiary2.R
import com.example.chatdiary2.data.HappyDateWithType
import com.example.chatdiary2.ui.view.common.PieChart
import com.example.chatdiary2.ui.view.main.table.HappyValueViewModel


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun HappyDaySummary(modifier: Modifier = Modifier, happyValueViewModel: HappyValueViewModel) {

    val happyValueState by happyValueViewModel.happyValueListState.collectAsState()
    val data = happyValueState.data.map { day ->
        HappyDateWithType(day.startDate, day.value)
    }.groupBy { it.type.titleResource }.mapValues { it.value.size }

    var filterValues = mapOf(
        getPair(R.string.VeryHappy, data),
        getPair(R.string.Happy, data),
        getPair(R.string.JustSoSo, data),
        getPair(R.string.WantToDie, data),
        getPair(R.string.Empty, data),
    ).filterValues { it != null }
        .map { it.key to it.value!! }
        .toMap()
    PieChart(
        data = filterValues, modifier = modifier
    )
}

@Composable
private fun getPair(resId: Int, data: Map<Int, Int>): Pair<String, Int?> {
    return Pair(stringResource(id = resId), data[resId]);
}

