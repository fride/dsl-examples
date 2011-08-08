package fsm.dsl

import collection.immutable.Nil
import collection.mutable.ListBuffer
import fsm.api.{StateMachine, Transition, State}

object FsmDsl {

  implicit def fsmBuilder2Fsm(fsm:Fsm) = {
      val s = fsm.states.map(_.toState)
      StateMachine(
        states = s
        , current = Some(s head)
        )
  }

  class Fsm {

    def currentState: Option[StateHelper] = {
      states match {
        case (state :: tail) => Some(state)
        case _ => None
      }
    }

    private[FsmDsl] var states:List[StateHelper] = Nil

    //implicit def stringToTransitionHelper(str:String) = new TransitionHelper(str)

    object actions {
      def :=(actions: String*): Unit = {
        for {
          stateHelper <- currentState
        } {
          stateHelper.actions = actions.toList
        }
      }
    }

    object resetEvents {
      def := (events:String*) = {

      }
    }
    object transitions {
      def :=(fun: => Unit): Unit = {
        fun
      }
    }
    implicit def str2TransitionHelper(str: String) = new TransitionHelper(str)

    class TransitionHelper(on: String) {
      def ==>(to: String) = {
        val transition = Transition(on, to)
        for (stateHelper <- currentState) {
          stateHelper.transitions += transition
        }
        transition
      }
    }

    class StateHelper(val name: String) {
      var actions: List[String] = Nil
      val transitions: ListBuffer[Transition] = ListBuffer()
      def toState = State(name
                          , actions.toList
                          , transitions.toList)
      override def toString = "actions: " + actions + ", " + "transitions: " + transitions
    }

    def state(name: String)(fun: => Unit):State = {
      val helper = new StateHelper(name)
      states = helper :: states
      fun
      println("state made!" + helper.toString)
      helper.toState
    }
    override def toString = states.map(_.toString).toString()
  }

}


object FsmDslTest {

  import FsmDsl._


  def main(args:Array[String]) {
    val fsm = new Fsm {

      resetEvents := "doorOpened"

      val idle:State = state("idle") {
        actions := ("unlockDoor", "lockPanel")
        transitions := {
          "doorClosed" ==> "active"
        }
      }

      val active = state("active") {
        transitions := {
           "drawerOpened" ==> "waitingForLight"
           "lightOn"      ==> "waitingForDrawer"
        }
      }

      val waitingForLight = state("waitingForLight") {
        transitions := {
          "lightOn" ==> "unlockedPanel"
        }
      }
      val waitingForDrawer = state("waitingForDrawer") {
        transitions := {
          "drawerOpened" ==> "unlockedPanel"
        }
      }

      val unlockedPanel = state("unlockedPanel") {
        actions := ("unlockPanel", "lockDoor")
        transitions := {
          "panelClosed" ==> "idle"
        }
      }
    }
    println("FSM: " + fsm)
  }

}
