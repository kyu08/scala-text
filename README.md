scala-text(https://scala-text.github.io/scala_text/)

# Scala の基本
val は再代入不可
var は再代入可能
```
val x = 1
var x = 3
```
のように再宣言できる

# sbtでプログラムをコンパイル・実行する
REPL を抜けるとき
```
> :quit
```

今のプロセス自体を終了させる汎用的なメソッド
`sys.exit`

`build.sbt`にいろいろオプションをつけとくとコンパイラのメッセージが親切になるよ

`sbt`の中に入って`run`コマンドを実行すると`main`メソッドを持っているオブジェクトを探して実行してくれる。

# 制御構文
文は評価しても値を持たない。
```
val i = 1
```

## ブロック式
`Unit`型は`void`のようなもので唯一の値`()`をもつ

## if式
## while式
## return式
## for式
- to
- until
- yield
## match式
```
val name = "taro"
name match {
	case "taro" => "Male"
	case "hana" => "Female"
	case _ => "other"
}
`_`は`default`的なやつ(ワイルドカードパターン)
`=>`の右辺は複数行で書くこともできる
```
## パターンマッチによる値の取り出し
以下のように書くと、`b = List(2)`, `c = List(3)`として変数に格納してくれる
```
val lst = List("A", "B", "C")
lst match {
	case List("A", b, c) =>
		println("b = " + b)
		println("c = " + c)
	case _ => println("nothing")
}
```

## as パターン(@)
`a@List("A")`とすると、`List("A")`を変数`a`に格納してそれ以降で使うことができる。
```
val lst = List(List("A"), List("B", "C"))
    lst match {
      case List(a@List("A"), x) =>
        println(a)
        println(x)
      case _ =>
        println("nothing")
    }
```

これはコンパイルエラーになるらしい。`a`の型が定まらないから...?
```
(List("a"): Any) match {
  case List(a) | Some(a) =>
    println(a)
}
```

## 中置パターン
さっきの`match`文は以下のように書き換える事ができる
```
lst match {
	case "A" :: v :: c :: _ => 
		println("b = " + b)
		println("c = " + c)
	case _ =>
		println("nothing")
}
```

### 疑問
中置パターンのときは`List`ってかかなくていいってことはこれは`List`用の演算子ってこと？

# クラス
