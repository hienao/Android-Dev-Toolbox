import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import enums.Command
import java.awt.FlowLayout


@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterialApi::class)
@Composable
@Preview
fun App() {
    var choosedCommand by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("") }
    var tableState by remember { mutableStateOf(0) }
    var paramsMap by remember { mutableStateOf(mutableStateMapOf<String,String>(Pair("DEV_APP_PACKAGE","com.qima.kdt"))) }
    val tabTitles = listOf("常用", "调试")
    val commandList =
        listOf(Command.DEV_SEE_DEVICES,
            Command.DEV_LAYOUT_ENABLE,
            Command.DEV_LAYOUT_DISABLE,
            Command.DEV_SEE_CURRENT_ACTIVITY,
            Command.DEV_SEE_CURRENT_FRAGMENT,
            Command.DEV_CLEAR_APP_DATA,
            Command.DEV_SEE_ACTIVITY_RECORDS)
    MaterialTheme {
        Column(modifier = Modifier) {
            TabRow(selectedTabIndex = tableState) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(text = { Text(text = title) },
                        selected = (tableState == index),
                        onClick = {
                            tableState = index
                        })
                }
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp), text = "${choosedCommand}运行结果:"
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .background(Color.White)
                    .heightIn(min = 100.dp, max = 300.dp), text = resultText
            )
            TextField(
                value = paramsMap["DEV_APP_PACKAGE"]?:"",
                onValueChange = {
                    paramsMap["DEV_APP_PACKAGE"]=it
                } ,
                placeholder = @Composable { Text(text = "This is placeholder") }
            )
            FlowRow(modifier = Modifier.padding(8.dp)) {
                commandList.forEach { item ->
                    Chip(modifier = Modifier.padding(horizontal = 5.dp, vertical = 0.dp), onClick = {
                        choosedCommand = ""
                        val result = ShellUtils.runCommand(getFinalCommand(item,paramsMap),item.defaultResult)
                        choosedCommand = item.title + "--"
                        resultText = result.joinToString("\n")
                    }){
                        Text(item.title)
                    }
                }

            }
        }

    }
}
fun getFinalCommand(commandTemplate:Command,params: SnapshotStateMap<String,String>):String{
    var cmd=commandTemplate.command
    if (commandTemplate.hasParam){
        params.forEach { (t, u) ->
            if (cmd.contains("@@{${t}}")){
                cmd=cmd.replace("@@{${t}}",u)
            }
        }
    }
    return cmd
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Android Dev Toolbox") {
        App()
    }
}
