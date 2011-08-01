package fsm.parser

import util.parsing.combinator._
import syntactical.StandardTokenParsers


object FsmDslParer extends JavaTokenParsers with PackratParsers {

  lazy val fsm:PackratParser[Any]         = events ~ resetEvents ~ commands ~ rep(state)

  lazy val events:PackratParser[Any]      = "events" ~> rep(nameAndId) <~ "end"

  lazy val nameAndId:PackratParser[Any]   = name  ~ id

  lazy val resetEvents:PackratParser[Any] = "resetEvents" ~> rep1sep(name, ",") <~ "end"

  lazy val commands:PackratParser[Any]    = "commands" ~> rep(nameAndId) <~ "end"

  lazy val state:PackratParser[Any]       = "state" ~> name ~ (actions?) ~ rep(transition) <~ "end"

  lazy val actions:PackratParser[Any]     = "actions" ~> "{" ~> repsep(name, ",") <~ "}"


  lazy val transition                     = name <~ "=>" ~> name

  lazy val name = "[a-z][a-zA-Z0-9_]*".r

  lazy val id = "[A-Z0-9_]+".r

  def parse(str:String) = {
     parseAll(fsm,str)
  }
}
