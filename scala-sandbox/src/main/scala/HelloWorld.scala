import scala.concurrent.{Await, Future, Promise}

object HelloWorld {
  println("🥺🥺🥺🥺🥺🥺🥺🥺🥺🥺🥺🥺🥺🥺🥺🥺🥺🥺🥺")
  def main(args: Array[String]): Unit = {
    val promiseGetInt: Promise[Int] = Promise[Int]
    val futureByPromise: Future[Int] = promiseGetInt.future // PromiseからFutureを作ることが出来る

    // Promiseが解決されたときに実行される処理をFutureを使って書くことが出来る
    val mappedFuture = futureByPromise.map { i =>
      println(s"Success! i: ${i}")
    }

    // 別スレッドで何か重い処理をして、終わったらPromiseに値を渡す
    Future {
      Thread.sleep(300)
      promiseGetInt.success(1)
    }

    Await.ready(mappedFuture, 5000.millisecond)

  }

  println("🥺🥺🥺🥺🥺🥺🥺🥺🥺🥺🥺🥺🥺🥺🥺🥺🥺🥺🥺")
}

