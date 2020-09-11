import java.util.Locale
object HelloWorld {
  println("-------------------------")
  def main(args: Array[String]): Unit = {
    for(i <- 1 to 1000) {
      val pw = new scala.util.Random(new java.security.SecureRandom()).alphanumeric.take(5).toList match {
        case List(a, b, c, d, _) => List(a, b, c, d, a).mkString
      }
      println(pw)
    }
  }
}
