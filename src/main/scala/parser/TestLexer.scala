package parser
import scala.util.parsing.combinator.syntactical.StandardTokenParsers
import mainPPE._

class TestLexer extends StandardTokenParsers {

  lexical.reserved += ("Frame", "Player", "Object", "Behaviour", "Enemy", "Goal", "Level", "True", "False", "end")

  lexical.delimiters += ("(", ")", ",", ":")

  def typeName: Parser[Any] = "Frame" | "Player" | "Object" | "Behaviour" | "Enemy" | "Goal"
  def level: Parser[Any] = "Level" ~ "(" ~ listAttr ~ ")" ~ ":" ~ typeList ~ end
  def bool: Parser[Any] = "True" | "False"
  def end: Parser[Any] = "end"

  def exp: Parser[Any] = levelList

  def levelList: Parser[Any] = level ~ opt(levelList)

  def typeList: Parser[Any] = typeDeclar ~ opt(typeList)

  def typeDeclar: Parser[Any] = typeName ~ "(" ~ opt(listAttr) ~ ")" ~ opt(":" ~ typeList ~ end)

  def listAttr: Parser[Any] = attr ~ opt("," ~ listAttr)

  def attr: Parser[Any] = simpleTypeDeclar | bool | numericLit | ident

  def simpleTypeDeclar: Parser[Any] = typeName ~ "(" ~ listAttr ~ ")"

  def eval(input: String) ={
    val tokens = new lexical.Scanner(input)
    println(input)
    println
    println(phrase(exp)(tokens))
  }

  def parseAll[T](p: Parser[T], in: String): ParseResult[T] = {
    phrase(p)(new lexical.Scanner(in))
  }

  def apply(code: String) = {
    parseAll(exp, code) match {
      case Success(r, n) => {
          println("success")
//        val interpreter = new Interpreter(r)
//
//
//
//        try {
//
//          interpreter.run
//
//        } catch {
//
//          case e: RuntimeException => println(e.getMessage)
//
//        }

      }

      case Error(msg, n) => println("Error: " + msg)

      case Failure(msg, n) => println("Error: " + msg)

      case _ =>

    }
  }
}