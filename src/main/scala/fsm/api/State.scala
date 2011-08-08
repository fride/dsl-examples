package fsm.api


case class State (name:String, action:List[String], transitions:List[Transition])
