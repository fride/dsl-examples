package fsm

import scala.PartialFunction
import collection.mutable.Map
import com.sun.xml.internal.xsom.impl.Ref.Term

trait FSM[ST,E] {

  type StateFunction = PartialFunction[E,ST]

  private var currentState:Option[ST] = None

  private val transitions:Map[ST,StateFunction] = Map()
  private var terminalStates:Set[ST] = Set()

  def isTerminated = {
    currentState match {
      case Some(st) => terminalStates.contains(st)
      case _ => false
    }
  }
  def startWith(state:ST) = {
    currentState = Some(state)
  }

  def stopOn(terminalState:ST) {
     terminalStates += terminalState
  }
  def in(state:ST)(fun:StateFunction) =  {
    transitions.put(state,fun)
  }

  def onEvent(e:E) = {
    println("In " + currentState + " got " + e)
    val nextState = for (state <- currentState;
         transition <- transitions.get(state);
         if transition.isDefinedAt(e)) yield(transition(e))
    nextState match {
      case None => println("No Transition found")
      case Some(st) => enterState(st)
    }
  }

  protected def enterState(st:ST) = {
    currentState = Some(st)
  }
}

object FSM {

}
