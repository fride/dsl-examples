package json

import util.parsing.combinator.{JavaTokenParsers, RegexParsers}

/**
 * Parser for JSON Objects. See "Programming in Scala"
 */
object JsonParser extends JavaTokenParsers {

  /**
   * Remove " at end and start.
   */
  lazy val clean_string = (s:String) => {
    if (s.startsWith("\"")) s.substring(1,s.length-1)
    else s
  }
  lazy val value:Parser[Any] = (
    arr
  | obj
  | decimalNumber ^^ (_.toDouble)
  | stringLiteral ^^ (clean_string(_))
  | "null"        ^^ (x => null)
  | "true"        ^^ (x => true)
  | "false"       ^^ (x => false)
  )

  lazy val arr  = "[" ~> repsep (value, ",") <~ "]"

  lazy val obj = "{" ~> repsep (member, ",") <~ "}" ^^ {
    Map() ++ _
  }

  lazy val member = stringLiteral ~ ":" ~ value ^^ {
    case  str ~ ":" ~ value => (clean_string(str),value)
  }
}
