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
基本的な書き方
```
class Point(_x: Int, _y: Int) {
	val x = _x
	val y = _y
}
```
こう書くこともできる
```
class Point(val x: Int, val y: Int){}

```
- クラス名の直後にコンストラクタ引数の定義がある

### 疑問
- `private[this]`よくわからなかった。"クラス内からのみアクセス可能"と"そのオブジェクトからのみアクセス可能"がどう違うかわからなかった。(-> コンパニオンオブジェクトからアクセスできるかどうかの違いってことか？)
- なんでこれが動くんじゃ(`p1 + p2`のとこ)
```
class Point(val x: Int, val y: Int) {
  def +(p: Point): Point = {
    new Point(x + p.x, y + p.y)
  }
  override def toString(): String = "(" + x + ", " + y + ")"
}

val p1 = new Point(1, 1)
// p1: Point = (1, 1)

val p2 = new Point(2, 2)
// p2: Point = (2, 2)

p1 + p2
```

## メソッド
3行目のやつを`部分適用`といって、新しい関数を作ることができる。
```
def add(x: Int, y: Int) = x + y
val func = add(1, _)
func(3)
// 4
```
## フィールド定義
- `private[this]` -> 高速になるのでパフォーマスチューニングのとき気をつけよう
## 抽象クラス
subclassでメソッドの実装を書く時`override`キーワードは文法的には必須ではないけど可読性のために書こう。
```
abstract class XY {
	def x: Int
	def y: Int
}
```

# オブジェクト
Scala では全ての値がオブジェクトである。(オブジェクトを継承してるってこと？)
そのため(因果関係がわからん)`static`メソッドや`static`変数を定義することはできない。
そのかわりに`object`キーワードによって同じ名前のシングルトンオブジェクトを定義できる。(?)

## 疑問
↓ここまるでわからん
```
一方、2番めの使い方について考えてみます。点を表す Point クラスのファクトリを objectで作ろうとすると、次のようになります。apply という名前のメソッドはScala処理系によって特別に扱われ、Point(x)のような記述があった場合で、Point objectにapplyという名前のメソッドが定義されていた場合、Point.apply(x)と解釈されます。これを利用してPoint objectの applyメソッドでオブジェクトを生成するようにすることで、Point(3, 5)のような記述でオブジェクトを生成できるようになります。
```

↑のメリットとして
- クラスの実装詳細を内部に隠しておくことができるというメリットがある。

## ケースクラス
- プライマリコンストラクタのすべてのフィールドを公開
- `equals()`などの基本的なメソッドをオーバーライドしたクラスを生成する
- そのクラスのインスタンスを生成するためのファクトリメソッドを生成する

### tips
- `sbt console`内で`:paste`って打つと改行できるモードになる。

class Person(name: String, private[this] val age: Int)

object Person {
  def printWeight(): Unit = {
    val taro = new Person("taro", 20)
    println(taro.age)
  }
}




















