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
式(expression)は**評価が成功すると値になるものである。**評価の結果として**例外が投げられた場合等が評価が失敗した場合に当たる。**

文は評価しても値を持たない。
```
val i = 1
```

## ブロック式
**最後に評価した値を返す。**
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

一方、2番めの使い方について考えてみます。点を表す Point クラスのファクトリを objectで作ろうとすると、次のようになります。apply という名前のメソッドはScala処理系によって特別に扱われ、Point(x)のような記述があった場合で、Point objectにapplyという名前のメソッドが定義されていた場合、Point.apply(x)と解釈されます。これを利用してPoint objectの applyメソッドでオブジェクトを生成するようにすることで、Point(3, 5)のような記述でオブジェクトを生成できるようになります。

↑のメリットとして
- クラスの実装詳細を内部に隠しておくことができるというメリットがある。

## ケースクラス
- プライマリコンストラクタのすべてのフィールドを公開
- `equals()`などの基本的なメソッドをオーバーライドしたクラスを生成する
- そのクラスのインスタンスを生成するためのファクトリメソッドを生成する

## コンパニオンオブジェクト
クラスと同じファイルないで、同じ名前て定義されたシングルトンオブジェクトはコンパニオンオブジェクトと呼ばれる。privateなものにもアクセスできる。


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
```scala
1. Tuple は要素数が決まってる
2. Listは型が決まってる
とか？
```

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
val G[A] = G[B]
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

# 関数
Scalaの関数は、単に`Function0` ~ `Function22`までのトレイトの無名サブクラスのインスタンスである。
## apply メソッド
`apply`メソッドはScalaコンパイラから特別扱いされ、`x.apply(y)`は常に`x(y)`のように書くことができる。
## 関数の型宣言方法
```Scala
var func: ((x: Int) => Int) 
```
## 関数のカリー化
```Scala
val add = (x: Int, y: Int) => x + y
val addCurried = (x: Int) => ((y: Int) => x + y)
add(100, 200)
addCurried(100)(200)
```
## 高階関数
関数を引数に取ったり関数を返すメソッドや関数のこと。

# コレクションライブラリ
mutable な方が実行速度は早いけど堅牢性などを考えるとimmutableに書くべきだよね。
## Array
要素が同じでもequalsがtrueにならない、などの罠があるのでパフォーマンス上必要になる場合以外はあまり積極的に使うものではない。

## Range
`to`とか`until`で呼び出すことが多い
```
1 to 5
1 until 4
(1 to 4).toList // List(1,2,3,4)
```

## List
`List`や`Vector`はよく使われる
`List`はimmutable

### Nil
空のList

### ::
`::`(コンス)は、Listの銭湯に要素をくっつけるメソッド
```
val a1 = 1 :: Nil  // List(1)
val a2 = 2 :: a1  // List(2, 1)
```

### 疑問
```
// これはできるのに
1 :: List(2)
// とかはできないんだね
"," mkString List(1,2)
(左).メソッド(右)の形になる関係じゃないとむりってことかな...
```

## 中置記法
Scalaでは1引数のメソッドは中置記法で書くことができる。ちなみに、
```
1 :: 2 :: Nil
// equals
Nil.::(2).::(1)
```
ってかんじ。


## Scala の0引数メソッドの定義と呼び出し
Scala の0引数メソッドは`()`なしと`()`を使った定義の2種類ある。挙動は上で書いた通り。(別々のメソッドとして定義できるの知らなかった。あんまりよくなさそう。)

## 疑問
これの `xs.foldLeft` がよくわからん
```
def mkString[T](list: List[T])(sep: String): String = list match {
  case Nil => ""
  case x::xs => xs.foldLeft(x.toString){(x, y) => x + sep + y}
}
```

### flatMap
`List`をたいらにする
```Scala
List(List(1,2), List(3,4)).flatMap{e => e.map(g => g + 1)}
```
形を変えると`flatMap`を使って`for`をかける

```Scala
List(1,2,3).flatMap{e => List(4, 5).map(g => g * e)}
// List(4, 5, 8, 10, 12, 15)

for(x <- col1; y <- col2;) yield z
// equals
col1.flapMap{e => col2.map{g => z}}
```

## Vector
- immutable
- 高速
- まずこれを使うことを検討するべき

## Map
immutable と mutable の2種類がある
なにも設定せずに`Map`と書くと`scala.collection.immutable.Map`が使われる。
内部の実装として主に`scala.collection.immutable.HashMap`と`scala.collection.immutable.TreeMap`があるんだけど、通常は`HashMap`が使われる。

`scala.collection.mutable.Map`

## Set

# ケースクラスとパターンマッチング

`sealed` -> 同一ファイル内からのみ継承可能なクラス/トレイトのこと
コンパイラがパターンマッチングの抜け漏れを指摘してくれるので、ケースクラスのスーパークラス/トレイトには`sealed`を付けといた方がいい。

## ケースクラスによって自動生成されるもの
インスタンス間の同値比較ができるようになる。クラスが同じでプライマリコンストラクタ引数の値がすべて一致していれば同値と判定する。

# エラー処理
Scalaでの例外処理は
1. 例外を使う方法
1. `Option`や`Either`や`Try`などのデータ型を使う方法
がある。

## エラー処理で実現すべきこと
### 例外安全性
例外が発生してもシステムがダウンしたり、データの不整合などの問題が起きないこと
### 強い例外安全性
例外が発生した場合、すべての状態が例外発生前に戻らなければならないという制約。

## `Option`
Option はScalaでもっとも多用されるデータ型のひとつ。
nullの代替として使われる。

- `Some`
- `None`
の2つがある。
`Some`は何かしらの値が格納されている時の`Option`の型で`None`は何も値が格納されていない時の`Option`の型。


### 疑問
`_`の意味がよくわかってない。(なんかよく出てくる便利なやつみたいなイメージ)

- `None`は3倍したりしても`None`のままなので扱いやすい。

`flatten`するとSomeのネストを解消してくれる

`Option`にも`flapMap`メソッドがある

```Scala
v1.map(i1 => v2.map(i2 => i2 * i1)).flatten
// equals
v1.flapMap(i1 => v2.map(i2 => i2 * i1))
```

## Either
`Option`によって値が取得できたかどうかはわかるけど、エラーの状態まではわからない。エラーの種類まで取得できるのが`Either`。
Option は `Some` or `None`だったけど、`Either`は `Right` or `Left`
一般的に`Left`にエラー値を、`Right`に正常な値をいれることが多い。(英語の"right"が正しいという意味なので、それにかけているという説があります。)
ログインエラーを表現する例を示す。↓このように代数的データ型として定義しよう。
```Scala
sealed trait LoginError
case object InvalidPassword extends LoginError
case object UserNotFound extends LoginError
case object PasswordLocked extends LoginError
```
ログインAPIの型は以下のようにする
```Scala
case class User(id: Long, name: String, password: String)

object LoginService {
	def login(name: String, password: String): Either[LoginError, User] = ???
}
```
で、これを使うと...
```Scala
LoginService.login(name = "hoge", password = "pass") match {
	case Right(User) => println(s"Logged in!")
	case Left(InvalidPasswordError) => println(s"invalid pass")
}
```
みたいな感じになるけど、実はこのコードだと、`UserNotFound`, `PasswordLocked`の場合が抜けてる。それをコンパイラが教えてくれるんやで。素敵やろ

## 名前渡しパラメータ
多くの言語でもそうなようにScalaでも、メソッド実行前にはまず引数が評価され、次にメソッド本体のコードが実行される。
この評価順序のことを先行評価(eager evaluation)あるいは正格評価(strict evaliation)とよぶ。
名前渡しパラメータを使うと、変数が実際に使用される箇所まで評価を遅延させることができる。

## Try
Eitherと同じで正常な値とエラー値のどちらかを表現するデータ型である。
Eitherと違うのは、2つの型が平等ではなく、エラー値がThrowableに限定されており、型引数を1つしか取らないこと。
具体的には
- Success
- Failure
の2つをとる。
例外が起きそうな箇所を`Try`で包みFailureにして値として扱えるようにするのがTryの特徴。

## Option と Either と Try の使い分け
nullを扱う場面ではOptionでじゅうぶん。
Optionだと情報が足りなくて、かつエラー型が代数的に定義されているものを扱うときはEitherがよい。
Tryは例外を値として扱いたい時に用いると良い。非同期処理をする時など。

# 暗黙の型変換と暗黙のパラメータ
## Implicit Conversion
暗黙の型変換機能をユーザが定義できるようにする機能。

```Scala
implicit def intToBool(arg: Int): Boolean = arg != 0
```
けどこれはあんまりよくない。

## pimp my library パターン
こっちが本来のつかいかた。既存のクラスにメソッドを追加して拡張する(ようにみせかける)方法。
(これは明示的に呼び出す必要がないってこと？)

# 型クラス
型クラス ≒ implicit parameter

# Future / Promise
## Future とは
JavaScript の Promise に似てるよ。

## Promise

# テスト
## ユニットテストを書く時に大切なこと
1. 高速に実行できるテストを実装する
1. 再現性のあるテストを実装する

## テストの設計
1. 機能を満たすことをテストする
1. 機能が実行できる境界値に大してテストする
1. 例外やログがちゃんとでることをテストする

## モック
モックを含め、テストの対象が依存しているオブジェクトを書き換える代用品の総称をテストダブルと呼ぶ。

### 疑問
- `private`はそのclass内からのみ
- `private[this]`はそのオブジェクトからのみ。
てことは`private[this]`だとobject からも呼べるってことか

# トレイトを使ったリファクタリング
こういうクラスがあって、`registaer`と`login`以外のメソッドを外部に隠蔽したいとき、隠したいものに`private[this]`つけるのもいいけど、
```Scala
class UserService {
  // メソッドの実装は同じなので???で代用しています

  // ストレージ機能
  def insert(user: User): User = ???

  def createUser(rs: WrappedResultSet): User = ???

  def find(name: String): Option[User] = ???

  def find(id: Long): Option[User] = ???

  // パスワード機能
  def hashPassword(rawPassword: String): String = ???

  def checkPassword(rawPassword: String, hashedPassword: String): Boolean = ???

  // ユーザー登録
  def register(name: String, rawPassword: String): User = ???

  // ユーザー認証
  def login(name: String, rawPassword: String): User = ???
}
```
↓のようにtraitを用いて外部に公開するメソッドを制限する方法もある。


```Scala
trait UserService {
  val maxNameLength = 32

  def register(name: String, rawPassword: String): User

  def login(name: String, rawPassword: String): User
}
```

### 感想
traitが実装を持てると再利用性が高くてよさそう！

## DI
DI して 依存の方向を適切に保とう！






