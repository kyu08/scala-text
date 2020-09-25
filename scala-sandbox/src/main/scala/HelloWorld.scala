object HelloWorld {
  def main(args: Array[String]): Unit = {
    println("🥺🥺🥺🥺🥺🥺🥺🥺🥺🥺🥺🥺🥺🥺🥺🥺🥺🥺🥺")
    val list = List(1,2,4)

    list match {
      case 1 :: b :: c :: _ =>
        println("b = " + b)
        println("c = " + c)
      case _ =>
        println("no result")
    }

    println("🥺🥺🥺🥺🥺🥺🥺🥺🥺🥺🥺🥺🥺🥺🥺🥺🥺🥺🥺")
  }
}

