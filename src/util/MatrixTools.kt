package util

fun printMatrix(field: Array<IntArray>) {
    for (y in 0 until field.size) {
        for (x in 0 until field[y].size) {
            val value = field[x][y].toString()
            print("$value ")
        }
        print("\n")
    }
}