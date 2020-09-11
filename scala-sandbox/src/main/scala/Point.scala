class Point(val x: Int, val y: Int) {
  def +(p: Point): Point = {
    new Point(x + p.x, y + p.y)
  }

  override def toString(): String = "(" + x + "," + y + ")"
}

val p1 = new Point(1,1)
println(p1)

val p2 = new Point(2,2)
println(p2)

println((p1 + p2))