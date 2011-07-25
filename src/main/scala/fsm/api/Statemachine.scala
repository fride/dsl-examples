package fsm.api

import java.io.StringWriter


case class StateMachine(states:Seq[State], transitions:Seq[Transition], startState:State, finishStates:Seq[State])


object StateMachineTest {
  def main(args:Array[String]) {

  }
}
