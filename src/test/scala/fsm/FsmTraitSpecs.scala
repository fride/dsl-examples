package fsm

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers


class FsmTraitSpecs extends FlatSpec with ShouldMatchers  {

  sealed trait State
  object A extends State
  object B extends State
  object C extends State

  class ExampleFsm extends FSM[State,String] {
    in(A) {
      case "B" =>
        println("Got B")
        B
    }

    in(B) {
      case "C" =>
        print("Got C")
        C
      case "A" =>
        println("Got A")
        A
    }

    stopOn(C)

    startWith(A)
  }

  "The example DSL shoul" should "Terminate with BC" in {
    val fsm = new ExampleFsm()
    fsm.onEvent("B")
    fsm.onEvent("C")
    fsm.isTerminated should be(true)
  }

  "The example DSL shoul" should "Terminate with BABC" in {
    val fsm = new ExampleFsm()
    fsm.onEvent("B")
    fsm.onEvent("A")
    fsm.onEvent("B")
    fsm.onEvent("C")
    fsm.isTerminated should be(true)
  }

}
