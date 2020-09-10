object HelloWorld {
  def main(args: Array[String]): Unit = {
    val age: Int = 9
    var isSchoolStarted: Boolean = false
    if(age >= 1 && age <= 7) isSchoolStarted = true
    if(isSchoolStarted) println("幼児です") else println("幼児ではない")
  }
}
