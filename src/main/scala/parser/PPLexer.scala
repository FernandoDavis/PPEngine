package parser

import java.awt.image.BufferedImage
import java.util

import Entities.{Crab1, Pichon, Player}
import Objects.Box
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.io.File

import scala.util.parsing.combinator.syntactical.StandardTokenParsers
import mainPPE._

import scala.util.Random

class PPLexer extends StandardTokenParsers {

  lexical.reserved += ("Frame", "Player", "Object", "Enemy", "Goal", "Level", "True", "False", "end", "Behaviour")

  lexical.delimiters += ("(", ")", ",", ":", "+")

  def typeName: Parser[Any] = "Frame" | "Player" | "Object" | "Enemy" | "Goal" | "Behaviour"

  def level: Parser[Level] = "Level" ~ "(" ~ listAttr ~ ")" ~ ":" ~ typeList ~ end ^^ {
    case _ =>
      var l: GenericLevel = new GenericLevel
      Main.objArray.get(Main.objArray.size - 1).foreach(l.addObject)
      Main.currentLevelIndex += 1
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
        var str: String = listAttr.toString
        var arr: Array[Int] = subIndex(str, 0)
        val x = str.substring(arr(0), arr(1)).toInt
        arr = subIndex(str, arr(1) + 1)
        val y = str.substring(arr(0), arr(1)).toInt
        arr = subIndex(str, arr(1) + 1)
        val w = str.substring(arr(0), arr(1)).toInt
        arr = subIndex(str, arr(1) + 1)
        val h = str.substring(arr(0), arr(1)).toInt
        arr = subIndex(str, arr(1) + 1)
        val z = str.substring(arr(0), arr(1)).toInt
        val findImg = str.substring(arr(0) - 3, arr(0) + z)

        var p: Player = new Player(x, y, w, h)
        var skin: BufferedImage = null
        if (findImg == "img1") {
          var texture: BufferedImage = ImageIO.read(new File("src/IMAGES/BRICK50X50.jpg"))
          p.setImg(texture)
          println("Skin 1 Applied to Player")
        }
        else if (findImg == "img2~" || findImg == "img2") {
          var face: BufferedImage = ImageIO.read(new File("src/IMAGES/dead_inside.jpg"))
          p.setImg(face)
          println("Skin 2 Applied to Player")
        }
        else if (findImg == "img3~") {
          var water: BufferedImage = ImageIO.read(new File("src/IMAGES/happyface.jpg"))
          p.setImg(water)
          println("Skin 3 Applied to Player")
        }
        Main.objArray.get(Main.currentLevelIndex).add(p)
      }

      else if (tN.toString().equals("Object")) {
        var str: String = listAttr.toString
        println(str)

        var arr: Array[Int] = subIndex(str, 0)
        val x = str.substring(arr(0), arr(1)).toInt
        arr = subIndex(str, arr(1) + 1)
        val y = str.substring(arr(0), arr(1)).toInt
        arr = subIndex(str, arr(1) + 1)
        val w = str.substring(arr(0), arr(1)).toInt
        arr = subIndex(str, arr(1) + 1)
        val h = str.substring(arr(0), arr(1)).toInt
        arr = subIndex(str, arr(1) + 1)
        val z = str.substring(arr(0), arr(1)).toInt
        val findImg = str.substring(arr(0) - 3, arr(0) + z)
        val box: Box = new Box(x, y, w, h)
       // box.setAnchored(true)
        if (findImg == "img1") {
          val texture: BufferedImage = ImageIO.read(new File("src\\IMAGES\\BRICK50X50.jpg"))
          box.setImg(texture)
        }
        else if (findImg == "img2~" || findImg == "img2") {
          val texture: BufferedImage = ImageIO.read(new File("src\\IMAGES\\zigzagbrick.jpg"))
          box.setImg(texture)
        }

        var behaviourString: ArrayList[String] = new ArrayList[String]()

        var personality: Personality = new Personality()

        val bIndex = str.indexOf("Behaviour") + 14
        var newstr = str.substring(bIndex) //~Some((

        if(newstr.contains("+")) {
          newstr.split("\\+").foreach(behaviourString.add)

          behaviourString.set(0, behaviourString.get(0).substring(0, findTilde(behaviourString.get(0), 0)))

          for(i <- 1 until behaviourString.length-1){
            behaviourString.set(i, behaviourString.get(i).substring(2, findTilde(behaviourString.get(i), 2)))
          }

          val lastIndex = behaviourString.size-1
          behaviourString.set(lastIndex, behaviourString.get(lastIndex).substring(2, findTilde(behaviourString.get(lastIndex), 2)))
        }
        else{
          newstr = newstr.split("~")(0)
        }
          for (i <- 0 until behaviourString.length) {
            println(behaviourString.get(i))
            behaviourString.get(i) match {
              case "randomFly" => personality += Behaviour.randomFly
              case "flyAroundPlayer" => personality += Behaviour.flyAroundPlayer
              case "jump" => personality += Behaviour.jump
              case "attackPlayer" => personality += Behaviour.attackPlayer(new Random().nextInt(20))
              case "followPlayer" => personality += Behaviour.followPlayer
              case "constantVelocity" => personality += Behaviour.constantVelocity(new Random().nextInt(10), new Random().nextInt(10))
              case "moveBackAndForth" => personality += Behaviour.moveBackAndForthH(new Random().nextInt(200), new Vector2D(x, y))
              case "none" =>  {box.setAnchored(true)}
              case _ => println("do nothing")
            }
          }

        box.setPersonality(personality)
        Main.objArray.get(Main.currentLevelIndex).add(box)
      }

      else if (tN.toString().equals("Enemy")) {
        val str: String = listAttr.toString
        var arr: Array[Int] = subIndex(str, 0)
        val x = str.substring(arr(0), arr(1)).toInt
        arr = subIndex(str, arr(1) + 1)
        val y = str.substring(arr(0), arr(1)).toInt
        arr = subIndex(str, arr(1) + 1)
        val z = str.substring(arr(0), arr(1)).toInt
        val enemyType = str.substring(arr(0) - 5, arr(0) + z)
        var enemy: Entity = null
        enemyType match {
          case "enemy1" => enemy = new Crab1(x, y)
          case "enemy2~" => enemy = new Pichon(x, y)
        }
        Main.objArray.get(Main.currentLevelIndex).add(enemy)
      }

    case _ => {
      println("nohayna")
    }


  }

  def subIndex(str: String, il: Int): Array[Int] = {
    var i = il
    while (i < str.length && !Character.isDigit(str.charAt(i))) {
      i += 1
    }
    var j = i
    while (j < str.length && Character.isDigit(str.charAt(j))) {
      j += 1
    }
    Array(i, j)
  }

  def findTilde(str: String, il: Int): Int = {
    var i = il
    while (i < str.length && str.charAt(i)!= '~') {
      i += 1
    }
    i
  }

  def listAttr: Parser[Any] = attr ~ opt(("," | "+") ~ listAttr)

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
      }
      case Error(msg, n) => println("Error: " + msg)

      case Failure(msg, n) => println("Error: " + msg)

      case _ =>
    }
  }
}