package parser

object Main1 {

  def main(args: Array[String]) {

    //strToParse is the string to be parsed
    val PPEngineCode = scala.io.Source.fromFile("src\\PPEngineCode.txt").mkString

    //Create the parser
    val parser = new TestLexer

    //Print out the result of the parser!
    println(parser.eval(PPEngineCode))

  }
}
