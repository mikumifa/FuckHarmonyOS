import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chatdiary2.data.table.happyData
import com.example.chatdiary2.ui.view.table.TimeGraph
import com.example.chatdiary2.ui.view.table.WeekHeader


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview(showBackground = true)
@Preview(device = Devices.FOLDABLE, showBackground = true)
@Composable
fun TableScreen(
    modifier: Modifier = Modifier, onDrawerClicked: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .customBackground(MaterialTheme.colorScheme),
    ) {
        Column(
            modifier = Modifier
                .height(200.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            HappyDaySummary(modifier = Modifier)
        }
        Spacer(modifier = Modifier.height(16.dp))
        val happyDataState by remember {
            mutableStateOf(happyData)
        }
        val scrollState = rememberScrollState()
        Box(
            modifier = Modifier.padding(4.dp).background(MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(18.dp))
        ) {
            TimeGraph(
                happyData = happyDataState,
                modifier = Modifier
                    .horizontalScroll(scrollState)
                    .fillMaxSize()
                    .padding(top = 18.dp, bottom = 18.dp, end = 4.dp)
            )
        }
    }
}