package util

import java.io.File

fun readFileLines(fileLoc : String) : List<String> = File(fileLoc).useLines { it.toList() }

fun readAsResource(res: String, delimiter : String = "\n") : List<String> {
    val text = ClassLoader.getSystemClassLoader().getResource(res).readText(Charsets.UTF_8)
    return text.split(delimiter)
}