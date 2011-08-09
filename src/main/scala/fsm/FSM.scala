package fsm

import scala.PartialFunction
import collection.mutable.Map
import java.lang.IllegalStateException

/**
 * Trait for Classes that need a finte state machine.
 * @param ST the States type
 * @param E the events type
 */
trait FSM[ST,E] extends PartialFunction[E,ST] {

  type StateFunction = PartialFunction[E,ST]
  type TransitionHandler = PartialFunction[(ST,ST),Unit]

  private var currentState:Option[ST] = None

  private val transitions:Map[ST,StateFunction] = Map()
  private var terminalStates:Set[ST] = Set()
  private var transitionHandler:TransitionHandler = {
    case (from, to) => println("Transition from " + from + " => " + to)
  }

  def terminated = {
    currentState match {
      case Some(st) => terminalStates.contains(st)
      case _ => false
    }
  }

  /**
   * Set the start state
   */
  def startWith(state:ST) = {
    currentState = Some(state)
  }

  /**
   * Add a terminating state.
   */
  def stopOn(terminalState:ST) {
     terminalStates += terminalState
  }

  /**
   * What to do in a given state
   * @param state the state
   * @oaram fun The transition.
   */
  def in(state:ST)(fun:StateFunction) =  {
    transitions.put(state,fun)
  }

  private def nextState(e: E): Option[ST] = {
    for (state <- currentState;
                         transition <- transitions.get(state);
                         if transition.isDefinedAt(e)) yield {
      val n = transition(e)
      transitionHandler((state,n))
      n
    }
  }

  /**
   *@param e the event to handle.
   *@return the new state.
   *@throws IllegalStateException if the event can not be handled.
   */
  def onEvent(e:E) = {
    println("In " + currentState + " got " + e)
    nextState(e)  match {
      case None => throw new IllegalStateException("No Transition for Event: " + e)
      case Some(st) => enterState(st); st
    }
  }


  def onTransition(fun:TransitionHandler) = {
    this.transitionHandler = fun
  }

  protected def enterState(st:ST) = {
    currentState = Some(st)
  }


  def apply(event:E):ST = onEvent(event)

  override def isDefinedAt(event:E) = {
    nextState(event) match {
      case Some(st) => true
      case None => false
    }
  }
}
