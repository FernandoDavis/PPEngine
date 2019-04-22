package parser

import scala.util.parsing.combinator._

class PPLexer extends RegexParsers{

  sealed trait WorkflowToken

  case class INT(i : Int) extends WorkflowToken
  case class ID(str: String) extends WorkflowToken
  case class FLOAT(f : Float) extends WorkflowToken

  case object COLON extends WorkflowToken
  case object COMMA extends WorkflowToken
  case object LPAREN extends WorkflowToken
  case object RPAREN extends WorkflowToken
  case object DELIMETER extends WorkflowToken
  case object BOOL extends WorkflowToken
  case object LEVEL extends WorkflowToken
  case object TYPENAME extends WorkflowToken

  override def skipWhitespace = true
  override val whiteSpace = "[\t\r\f]+".r

  def int: Parser[INT] = {
    """\\d+""".r ^^ { int => INT(int.toInt)}
  }

  def float: Parser[FLOAT] = {
    """\\d+.\\d+""".r ^^ { f => FLOAT(f.toFloat)}
  }

  def id: Parser[ID] = {
    "[a-zA-Z_][a-zA-Z0-9_]*".r ^^ { str => ID(str.toString) }
  }

  def end          = "end"          ^^ (_ => DELIMETER)
  def colon         = ":"             ^^ (_ => COLON)
  def comma         = ","             ^^ (_ => COMMA)
  def lparen        = "("             ^^ (_ => LPAREN)
  def rparen        = "("             ^^ (_ => RPAREN)
  def bool          = "True" | "False"         ^^ (_ => BOOL)
  def level          = "Level"         ^^ (_ => LEVEL)
  def typeName          = "Frame" | "Player" | "Object" | "Behaviour" | "Mob" | "Goal"         ^^ (_ => TYPENAME)

  private def processIndentations(tokens: List[WorkflowToken]): List[WorkflowToken] = {
    tokens.headOption match {
      case Some(token) =>
        token :: processIndentations(tokens.tail)
    }
  }

//  def tokens: Parser[List[WorkflowToken]] = {
//    phrase(rep1(end | colon | comma | lparen | rparen | bool | level
//      | typeName | int | float | id)) ^^ {  }
//  }

  //Evaluates the text to be parsed
//  def eval(input: String) =
//    parseAll(tokens, input) match {
//      case Success(result, _) => "Parsed successfully!"
//      case failure: NoSuccess => "Parse unsuccessful!"
//    }

}
