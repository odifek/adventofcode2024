package util

import kotlin.math.abs

operator fun Point.minus(distance: Distance): Point = Point(x = x - distance.dx, y = y - distance.dy)

operator fun Point.plus(distance: Distance): Point = Point(x = x + distance.dx, y = y + distance.dy)

fun Point.distanceFrom(point: Point): Distance = Distance(dx = x - point.x, dy = y - point.y)
fun Point.distanceAbsFrom(point: Point): Distance = Distance(dx = abs(x - point.x), dy = abs(y - point.y))

fun Point.isWithinBounds(boardSize: Size): Boolean = x in 0..<boardSize.width && y in 0..<boardSize.height