package main.onetoten

import util.readAsResource
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalTime
import java.time.ZoneId
import java.util.stream.Collectors

fun main(args: Array<String>) {
    puzzles()
}

private fun puzzles() {
    val lines = readAsResource("day4")

    var lastGuard = ""
    var firstAsleep: LocalTime? = null
    val guards: MutableMap<String, Guard> = HashMap()

    val sortedLines = lines.stream()
        .map { line ->
            val datePart = line.substringAfter('[').substringBefore(']')
            val date = SimpleDateFormat("yyyy-MM-dd HH:mm").parse(datePart).toInstant()

            Pair(date, line)
        }
        .sorted { l, r -> l.first.compareTo(r.first) }
        .collect(Collectors.toList())

    val guardLines = sortedLines
        .map {pair ->
            val date = pair.first
            val line = pair.second
            val guardId = line.substringAfter('#').substringBefore(' ')
            if (line.contains("#")) {
                lastGuard = guardId
            }

            if (line.contains("asleep")) {
                firstAsleep = LocalTime.ofInstant(date, ZoneId.of("UTC"))
            }
            if (line.contains("wakes")) {
                val guard = guards.getOrDefault(lastGuard, Guard(lastGuard))
                val lastDate = LocalTime.ofInstant(date, ZoneId.of("UTC"))
                guard.addSleep(firstAsleep!!, Duration.between(firstAsleep, lastDate))
                guards[lastGuard] = guard
            }

        }

    val mostAsleep = guards.values.stream()
        .max(Comparator.comparingInt { g -> g.asleep.values.sum() })

    val maxAsleepMinute = mostAsleep.get().asleep.maxBy { e -> e.value }
    println( "${mostAsleep.get().name} slept most at minute ${maxAsleepMinute!!.key}" )

    val guard = guards.values.stream()
        .max(Comparator.comparingInt { g -> g.asleep.values.max()!!})
    val guardMax = guard.get().asleep.maxBy { e -> e.value }
    println( "${guard.get().name} slept most at minute $guardMax")
}

private class Guard(
    val name: String
) {

    var asleep: MutableMap<LocalTime, Int> = HashMap()
    fun addSleep(time: LocalTime, duration: Duration) {
        val minutes = duration.toMinutes()

        for (minute in 0..minutes) {
            val currentMoment = time.plusMinutes(minute)
            asleep.putIfAbsent(currentMoment, 0)
            asleep.computeIfPresent(currentMoment) { _, u -> u + 1 }
        }
    }

    override fun toString(): String {
        val bld = StringBuilder()
        asleep.forEach { localTime, int ->
            bld.append(localTime.toString())
                . append(" amounts asleep $int")
                .append('\n')
        }
        return "name $name " + bld.toString()
    }
}