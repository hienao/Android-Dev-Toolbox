import enums.Command
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


object ShellUtils {
    fun runCommand(command: Command):List<String>{
       val result= runShell(command.command)
        if (result.isEmpty()){
            return arrayListOf<String>(command.defaultResult)
        }
        return result
    }
    fun runCommand(command: String,defaultResult:String):List<String>{
        val result= runShell(command)
        if (result.isEmpty()){
            return arrayListOf<String>(defaultResult)
        }
        return result
    }
    private fun execShell(shell: String?): String? {
        val result = StringBuilder()
        var process: Process? = null
        var bufferedReaderInfo: BufferedReader? = null
        var bufferedReaderError: BufferedReader? = null
        try {
            // 执行shell命令，返回了一个进程
            process = Runtime.getRuntime().exec(shell)
            // 等待命令执行完成
            process.waitFor()
            // 获取结果，正常返回是第一个，错误返回是第二个，返回结果只能有一个，要么正常执行，要么执行错误
            bufferedReaderInfo = BufferedReader(InputStreamReader(process.inputStream))
            bufferedReaderError = BufferedReader(InputStreamReader(process.errorStream))
            var line: String
            // 将返回结果存到一个stringbuilder里面，因为StringBuilder是可扩展的，所以不用String
            while (bufferedReaderInfo.readLine() != null || bufferedReaderError.readLine() != null) {
                line = if (bufferedReaderInfo.readLine() != null) {
                    bufferedReaderInfo.readLine()
                } else {
                    bufferedReaderError.readLine()
                }
                result.append(line + "\n")
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            // 释放资源
            if (bufferedReaderError != null) {
                try {
                    bufferedReaderError.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            if (bufferedReaderInfo != null) {
                try {
                    bufferedReaderInfo.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            process?.destroy()
        }
        return result.toString()
    }

    /**
     * 运行shell并获得结果，注意：如果sh中含有awk,一定要按new String[]{"/bin/sh","-c",shStr}写,才可以获得流
     *
     * @param shStr
     * 需要执行的shell
     * @return
     */
    private fun runShell(shStr: String): List<String> {
        val strList: MutableList<String> = ArrayList()
        var input:BufferedReader?=null
        var err:BufferedReader?=null
        try {
            val process = Runtime.getRuntime().exec(arrayOf("/bin/sh", "-c", shStr), null, null)
            val ir = InputStreamReader(process.inputStream)
            val er = InputStreamReader(process.errorStream)
            input = BufferedReader(ir)
            err = BufferedReader(er)
            var line: String?=null
            process.waitFor()
            while (input.readLine().also { line = it } != null) {
                strList.add(line!!)
            }
            while (err.readLine().also { line = it } != null) {
                strList.add(line!!)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }finally {
            input?.close()
            err?.close()
        }
        return strList
    }
}