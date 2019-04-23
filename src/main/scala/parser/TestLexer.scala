package parser

import java.util

import Entities.Player
import Objects.Box

import scala.util.parsing.combinator.syntactical.StandardTokenParsers
import mainPPE.{ArrayList, Behaviour, Component, GenericLevel, Level, Level1, Level2, Main, Obj}

class TestLexer extends StandardTokenParsers {

  lexical.reserved += ("Frame", "Player", "Object", "Behaviour", "Enemy", "Goal", "Level", "True", "False", "end")

  lexical.delimiters += ("(", ")", ",", ":")

  def typeName: Parser[Any] = "Frame" | "Player" | "Object" | "Behaviour" | "Enemy" | "Goal"

  def level: Parser[Level] = "Level" ~ "(" ~ listAttr ~ ")" ~ ":" ~ typeList ~ end ^^ {
    case _ =>
      var l : GenericLevel = new GenericLevel
      Main.objArray.get(Main.objArray.size-1).foreach(l.addObject)
      Main.currentLevelIndex+=1
      Main.objArray.add(new ArrayList[Obj](255))
      l
  }

  def bool: Parser[Any] = "True" | "False"

  def end: Parser[Any] = "end"

  def exp: Parser[Any] = levelList ^^ { case main => Main.run() }

  def levelList: Parser[Any] = level ~ opt(levelList) ^^ {
    case level ~ someL =>
      Main.getGame.addLevel(level)
  }

  def typeList: Parser[Any] = typeDeclar ~ opt(typeList)

  def typeDeclar: Parser[Any] = typeName ~ "(" ~ opt(listAttr) ~ ")" ~ opt(":" ~ typeList ~ end) ^^ {

    case tN ~ "(" ~ Some(listAttr) ~ ")" ~ _ =>
      println(tN.toString)
      if (tN.toString().equals("Player")) {
        println("yeet")
        var str: String = listAttr.toString
        println(str)
        var x: Int = 0
        var y: Int = 0
        var arr: Array[Int] = subIndex(str, 0)
        x = str.substring(arr(0), arr(1)).toInt
        arr = subIndex(str, arr(1) +1)
        y = str.substring(arr(0), arr(1)).toInt
        println(x + " " + y)
        //var x : Int = list.get(0).asInstanceOf[Int]
        // var y : Int = list.get(1).asInstanceOf[Int]
        //          var i : String = list.get(2).asInstanceOf[String]
        //          var Bh : Behaviour = list.get(3).asInstanceOf[Behaviour]
        Main.objArray.get(Main.currentLevelIndex).add(new Player(x, y))
      }

      else if (tN.toString().equals("Object")) {
        println("yeetObject")
        var str: String = listAttr.toString
        println(str)
        var x: Int = 0
        var y: Int = 0
        var arr: Array[Int] = subIndex(str, 0)
        x = str.substring(arr(0), arr(1)).toInt
        arr = subIndex(str, arr(1) + 1)
        y = str.substring(arr(0), arr(1)).toInt
        println(x + " " + y)
        //var x : Int = list.get(0).asInstanceOf[Int]
        // var y : Int = list.get(1).asInstanceOf[Int]
        //          var i : String = list.get(2).asInstanceOf[String]
        //          var Bh : Behaviour = list.get(3).asInstanceOf[Behaviour]
        var box: Box = new Box(x, y, 100, 100)
        box.setAnchored(true)
        Main.objArray.get(Main.currentLevelIndex).add(box)
      }


    case _ => {
      println("nohayna")
    }


  }

  // removed : before type list

  def subIndex(str: String, il: Int): Array[Int] = {
    var i = il
    while (i < str.length && !Character.isDigit(str.charAt(i))) {
      i += 1
      i - 1
    }
    var j = i
    while (j < str.length && Character.isDigit(str.charAt(j))) {
      j += 1
      j - 1
    }
    Array(i, j)
  }

  def listAttr: Parser[Any] = attr ~ opt("," ~ listAttr)

  def attr: Parser[Any] = simpleTypeDeclar | bool | numericLit | ident


  def simpleTypeDeclar: Parser[Any] = typeName ~ "(" ~ listAttr ~ ")"

  def eval(input: String) = {
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