import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatdiary.R
import com.example.chatdiary.ui.view.common.AnimatedPreloader
import com.example.chatdiary.ui.view.common.FlipCard
import com.example.chatdiary.ui.view.main.table.HappyValueViewModel
import com.example.chatdiary.ui.view.main.table.TimeGraph


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview(showBackground = true)
@Preview(device = Devices.FOLDABLE, showBackground = true)
@Composable
fun HappyValueScreen(
    modifier: Modifier = Modifier,
    onDrawerClicked: () -> Unit = {},
    happyValueViewModel: HappyValueViewModel = hiltViewModel()
) {
    val happyValueState by happyValueViewModel.happyValueListState.collectAsState()
    if (happyValueState.isOK && happyValueState.data.isNotEmpty()) {

        FlipCard(modifier = modifier
            .fillMaxSize()
            .padding(12.dp), first = {
            HappyDaySummary(
                modifier = Modifier, happyValueViewModel = happyValueViewModel
            )

        }, second = {
            val scrollState = rememberScrollState()
            Column(
                verticalArrangement = Arrangement.Top,
            ) {
                AnimatedPreloader(
                    modifier = Modifier.height(300.dp), lottieSource = R.raw.analysis
                )
                TimeGraph(
                    happyDataOfDay = happyValueState.data,
                    modifier = Modifier
                        .horizontalScroll(scrollState)
                        .padding(top = 18.dp, bottom = 18.dp, end = 4.dp)
                )
            }


        })
    } else {
        AnimatedPreloader(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), lottieSource = R.raw.no_data
        )
    }
}

