import scala.io.Source
object HelloWorld {
  def main(args: Array[String]): Unit = {
  }
  println("-------------------------")

  def withFile[A](filename: String)(f: Source => A): A = {
    val s = Source.fromFile(filename)
    try {
      f(s)
    } finally {
      s.close()
    }
  }

  def printFile(filename: String): Unit = withFile[String]("fileN"){ file =>
    file.getLines.foreach(println)
  }


  println("-------------------------")
}
