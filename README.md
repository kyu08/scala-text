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
# トレイト
## トレイトの基本
- クラスからコンストラクタを定義する機能を抜いたようなもの
- 複数のトレイトをクラスやトレイトにミックスインできる
- 直接インスタンス化できない
- (TypeScript の `interface`的なやつやなたぶん)

```
trait TraitA
trait TraitB
class ClassA

// can be compiled.
class ClassB extends ClassA with TraitA with TraitB
```

## 菱形継承問題
Scalaのように多重継承をサポートしている言語では、↓のような場合にメソッドの衝突がおきてしまう。
Scalaでは`override`指定なしの場合のメソッド定義の衝突はコンパイルエラーになる
```
trait TraitA {
  greet(): Unit
}
trait TraitB {
  greet(): Unit = println("B")
}

trait TraitC {
  greet(): Unit =println("C")
}

class ClassA extend TraitB with TraitC
```
この対処法として2つの方法を示す。
```
// 1.
class ClassA extends TraitB with TraitC {
	override greet(): Unit = println("yeeeeeeeeey")
}

```

```
// 2.
class ClassA extends TraitB with TraitC {
        override greet(): Unit = super[TraitB].greet()

// でもってこういうこともできる
	override greet(): Unit = {
		super[TraitB].greet()
		super[TraitC].greet()
	}
}
```

とはいえ、継承関係が複雑になった場合に、すべてを明示的に呼ぶのは大変。Scalaのトレイトにはこの問題を解決するために"線形化"という機能がある。

## 線形化(linearization)
Scalaのトレイトの線形化機能というのは、トレイトがミックスインされた順番をトレイトの継承順番と見なす機能のこと。
(後にミックスインされた方が優先)
```
trait TraitA {
	def g(): Unit
}

trait TraitB extends TraitA {
	override def g(): Unit = println("B")
}
trait TraitC extends TraitA {
        override def g(): Unit = println("C")
}
class ClassA extends TraitB with TraitC

(new ClassA).g()
// "C"
```
もちろん`super`を使うことで親のメソッドを呼べる

このような線形化によるトレイトの積み重ねの処理をScalaの用語では、積み重ね可能なトレイトと呼ぶことがある。
## 落とし穴: トレイトの初期化順序
`TraitB` が初期化されてから`TraitA`が初期化されるので最終的に`"nullWorld"`が出力されてしまう。
```
trait A {
  val foo: String
}

trait B extends A {
  val bar = foo + "World"
}

class C extends B {
  val foo = "Hello"

  def printBar(): Unit = println(bar)
}
(new C).printBar()
// nullWorld
```
この解決策として、
1. `lazy val`を使う
1. `def`を使ってメソッドとして定義する
がある。

### 注意点
lazy valはvalに比べて若干処理が重く、複雑な呼び出しでデッドロックが発生する場合があります。 valのかわりにdefを使うと毎回値を計算してしまうという問題があります。があまり問題にはならないのでどちらでもOK(な場合が多い)

### 事前定義
他の解決策として"事前定義"(Early Definitions)を使う方法がある。フィールドの初期化をスーパークラスよりも先に行う方法。
```
trait A {
	val foo: String
}
trait B extends A {
	val bar = foo + "world" // val のままでよい
}

class C extends {
	val foo = "Hello"
} with B {
	def printBar(): Unit = println(bar)
}
```
ただ、この例もそうだし、大抵の場合もそうだけど、トレイトの初期化問題は継承されるトレイト側で解決した方がいいことがおおいので事前定義はあまり使わないとおもう。

# 疑問
ちょっと前にでてきた`+`がメソッド名になるやつの挙動が謎だった。↓これで動いてたのが謎
```
a + b
```

# 型パラメータと変位指定
クラスは、0こ以上の型をパラメータとしてとることができる。
最初の方から順に`A`, `B`...と名付けるのがScalaでは慣習的。

### 思ったこと
引数をもたない関数を定義する時でも、可読性のために`()`は書いた方がいいと思いました。(Scalaでは省略可能。以下のような動作をする)(定義元では()ありでかつ呼び出し側で()なしだとエラー)
```
// OK
def hoge(): Unit = println("Hoge")
hoge()
// OK
def hoge(): Unit = println("Hoge")
hoge
// NG
def hoge: Unit = println("Hoge")
hoge()
// OK
def hoge(): Unit = println("Hoge")
hoge
```

### 疑問
- `List`と`Tuple`ってどう違うねん

### Tuple のシンタックスシュガー
```
new Tuple2(1, 2)
// equals
(1, 2)
```
## 変位指定(variance)
反変と共変について

## 共変(covariant)
まず、"非変"とは、
```
G...型パラメータをもったクラス
A, B 型パラメータ
```
のとき、`A` = `B` のときにのみ、
```
val G[A] = G[B]  // 疑問: これなにしてるの
```
というような代入が許されるという性質のこと。
共変とは、`A`が`B`を継承している時のみ
```
val : G[B] = G[A]
```
とできる。

```
class G[+A]
```
と書くと共変にできる。

そのあとの説明は理解できず

## 反変
共変の対となる性質。
`A`が`B`が継承している時のみ
```
val G[A] = G[B]
```
という代入が許される。
```
class G[-A]
```
と書く。

## 型パラメータの境界(bounds)
### 上限境界(upper bounds)








