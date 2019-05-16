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

class TestLexer extends StandardTokenParsers {

  lexical.reserved += ("Frame", "Player", "Object", "Behaviour", "Enemy", "Goal", "Level", "True", "False", "end")

  lexical.delimiters += ("(", ")", ",", ":")

  def typeName: Parser[Any] = "Frame" | "Player" | "Object" | "Behaviour" | "Enemy" | "Goal"

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
        println("YEET")
        var str: String = listAttr.toString
        println(str)
        var x: Int = 0
        var y: Int = 0
        var z: Int = 0
        var arr: Array[Int] = subIndex(str, 0)

        x = str.substring(arr(0), arr(1)).toInt
        arr = subIndex(str, arr(1) + 1)
        y = str.substring(arr(0), arr(1)).toInt
        arr = subIndex(str, arr(1) + 1)
        z = str.substring(arr(0), arr(1)).toInt
        println(x + " " + y + " " + str.substring(arr(0) - 3, arr(0)) + z)
        val findImg = str.substring(arr(0) - 3, arr(0) + z)

        //var x : Int = list.get(0).asInstanceOf[Int]
        // var y : Int = list.get(1).asInstanceOf[Int]
        //          var i : String = list.get(2).asInstanceOf[String]
        //          var Bh : Behaviour = list.get(3).asInstanceOf[Behaviour]
        //   var p: Player = new Player(x,y)
        //
        //        Main.objArray.get(Main.currentLevelIndex).add(p)

        var p: Player = new Player(x, y)

        // var skins = new SpriteSheet(ImageIO.read(new File("src\\BRICK50X50.jpg")), 1,1,50,50,1) //SprSheet, crop all textures to player/object dimensions? Unsure what the crop function is.
        var skin: BufferedImage = null

        //SWITCH STATEMENT VERSION doesnt work, im doing it wrong
        /*     findImg match{
               case "img1" =>  skin = ImageIO.read(new File("src/IMAGES/BRICK50X50.jpg"))
                 p.setImg(skin)
                 println("Skin 1 Applied to Player")
               case "img2" =>  skin = ImageIO.read(new File("src/IMAGES/happyface.jpg"))
                 p.setImg(skin)
                 println("Skin 2 Applied to Player")

               case "img3" =>  skin = ImageIO.read(new File("src/IMAGES/water.jpg"))
                 p.setImg(skin)
                 println("Skin 3 Applied to Player")
             }
             */

        if (findImg == "img1") {
          //var texture = skinss.getFrame(1)
          var texture: BufferedImage = ImageIO.read(new File("src/IMAGES/BRICK50X50.jpg"))
          p.setImg(texture)
          println("Skin 1 Applied to Player")
        }
        else if
        (findImg == "img2~" || findImg == "img2") {
          //var texture = skins.getFrame(2) //SprSheet
          var face: BufferedImage = ImageIO.read(new File("src/IMAGES/dead_inside.jpg"))
          p.setImg(face)
          println("Skin 2 Applied to Player")
        }
        else if
        (findImg == "img3~") {
          //var texture = skins.getFrame(2) //SprSheet
          var water: BufferedImage = ImageIO.read(new File("src/IMAGES/happyface.jpg"))
          p.setImg(water)
          println("Skin 3 Applied to Player")
        }

        Main.objArray.get(Main.currentLevelIndex).add(p)
        //couldnt be found when declared together???
      }


      else if (tN.toString().equals("Object")) {
        println("yeetObject")
        var str: String = listAttr.toString
        println(str)
        var x: Int = 0
        var y: Int = 0
        var w: Int = 100
        var h: Int = 100
        var z: Int = 0
        var arr: Array[Int] = subIndex(str, 0)

        x = str.substring(arr(0), arr(1)).toInt
        arr = subIndex(str, arr(1) + 1)
        y = str.substring(arr(0), arr(1)).toInt
        arr = subIndex(str, arr(1) + 1)
        w = str.substring(arr(0), arr(1)).toInt
        arr = subIndex(str, arr(1) + 1)
        h = str.substring(arr(0), arr(1)).toInt
        arr = subIndex(str, arr(1) + 1)
        z = str.substring(arr(0), arr(1)).toInt

        println(x + " " + y + " " + str.substring(arr(0) - 3, arr(0)) + " " + z)
        val findImg = str.substring(arr(0) - 3, arr(0) + z)
        //var x : Int = list.get(0).asInstanceOf[Int]
        // var y : Int = list.get(1).asInstanceOf[Int]
        //          var i : String = list.get(2).asInstanceOf[String]
        //          var Bh : Behaviour = list.get(3).asInstanceOf[Behaviour]

        val box: Box = new Box(x, y, w, h)
        box.setAnchored(true)

        if (findImg == "img1") {
          val texture: BufferedImage = ImageIO.read(new File("src\\IMAGES\\BRICK50X50.jpg"))
          box.setImg(texture)
          println("Texture 1 Applied to Object")
        }
        else if
        (findImg == "img2~" || findImg == "img2") {
          val texture: BufferedImage = ImageIO.read(new File("src\\IMAGES\\zigzagbrick.jpg"))
          box.setImg(texture)
          println("Texture 2 Applied to Object")
        }

        Main.objArray.get(Main.currentLevelIndex).add(box)
      }

      else if (tN.toString().equals("Enemy")) {
        println("birb")
        val str: String = listAttr.toString
        println(str)
        var x: Int = 0
        var y: Int = 0
        var z: Int = 0
        var arr: Array[Int] = subIndex(str, 0)

        x = str.substring(arr(0), arr(1)).toInt
        arr = subIndex(str, arr(1) + 1)
        y = str.substring(arr(0), arr(1)).toInt
        arr = subIndex(str, arr(1) + 1)
        z = str.substring(arr(0), arr(1)).toInt

        println(x + " " + y + " " + str.substring(arr(0) - 5, arr(0)) + " " + z)
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