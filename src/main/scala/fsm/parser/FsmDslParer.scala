package fsm.parser

import util.parsing.combinator._
import fsm.api.{StateMachine, State, Transition}

object FsmDslParer extends JavaTokenParsers with PackratParsers {

  lazy val fsm:PackratParser[StateMachine]  = events ~ resetEvents ~ commands ~ states ^^
    { case events ~ resetEvents ~ commands ~ s => StateMachine (s, Some( s.head)) }

  lazy val states:PackratParser[List[State]] = rep(state)

  lazy val events:PackratParser[List[(String,String)]]  = "events" ~> rep(nameAndId) <~ "end"

  lazy val nameAndId:PackratParser[(String,String)]   = name  ~ id ^^
    { case n ~i => (n.toString(), i.toString())}

  lazy val resetEvents:PackratParser[Any] = "resetEvents" ~> rep1sep(name, ",") <~ "end"

  lazy val commands:PackratParser[Any]    = "commands" ~> rep(nameAndId) <~ "end"

  lazy val state:PackratParser[State]       = "state" ~> name ~ opt(actions) ~ transitions <~ "end" ^^
    { case name ~ Some(actions) ~ transitions => State(name,actions,transitions)
      case name ~ None ~ transitions => State(name,Nil, transitions)
    }

  lazy val actions:PackratParser[List[String]]  = "actions" ~> "{" ~> repsep(name, ",") <~ "}"

  lazy val transitions:Parser[List[Transition]] = rep(transition)

  lazy val transition:Parser[Transition]                     = name ~ "=>" ~ name  ^^
    { case  signal ~ "=>" ~ state => (Transition(signal.toString(), state.toString()))}

  lazy val name = "[a-z][a-zA-Z0-9_]*".r ^^ (_.toString)

  lazy val id = "[A-Z0-9_]+".r

  def parse(str:String) = {
     parseAll(fsm,str)
  }
}
