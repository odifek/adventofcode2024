package util

operator fun Distance.plus(distance: Distance): Distance = Distance(dx = dx + distance.dx, dy = dy + distance.dy)
operator fun Distance.minus(distance: Distance): Distance = Distance(dx = dx - distance.dx, dy = dy - distance.dy)
operator fun Distance.times(num: Int): Distance = Distance(dx = dx * num, dy = dy * num)