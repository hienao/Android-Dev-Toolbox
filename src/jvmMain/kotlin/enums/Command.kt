package enums

enum class Command(
    val title: String = "",
    val command: String = "",
    val desc: String = "",
    var hasParam: Boolean = false,
    val defaultResult: String = "命令执行已完成"
) {
    DEV_LAYOUT_ENABLE(
        title = "开启查看布局",
        command = "adb shell setprop debug.layout true",
        desc = "查看当前view的布局",
        defaultResult = "执行完成，如无变化请退出当前页面重新进入"
    ),
    DEV_LAYOUT_DISABLE(
        title = "关闭查看布局",
        command = "adb shell setprop debug.layout false",
        desc = "查看当前view的布局",
        defaultResult = "执行完成，如无变化请退出当前页面重新进入"
    ),
    DEV_SEE_DEVICES(
        title = "查看已连接的设备",
        command = "adb devices",
        desc = "查看已连接的设备",
    ),
    DEV_JAVA_VERSION(
        title = "查看java版本",
        command = "java -version",
        desc = "查看java版本",
    ),
    DEV_SEE_CURRENT_ACTIVITY(
        title = "查看栈顶Activity",
        command = "adb shell \"dumpsys activity top | grep ACTIVITY | tail -n 1\"",
        desc = "查看栈顶Activity",
    ),
    DEV_SEE_CURRENT_FRAGMENT(
        title = "查看当前fragment",
        command = "adb shell \"dumpsys activity top | grep '#[0-9]: ' | tail -n 1\"",
        desc = "查看当前fragment",
    ),
    DEV_SEE_ACTIVITY_RECORDS(
        title = "查看Activity任务栈",
        command = "adb shell \"dumpsys activity activities | grep '* ActivityRecord{'\"",
        desc = "查看Activity任务栈",
    ),
    DEV_CLEAR_APP_DATA(
        title = "清除App数据",
        command = "adb shell pm clear @@{DEV_APP_PACKAGE}",
        desc = "清除App的数据，但不卸载App",
        hasParam = true
    )
}