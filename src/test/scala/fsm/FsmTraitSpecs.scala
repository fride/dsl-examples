package fsm

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers


class FsmTraitSpecs extends FlatSpec with ShouldMatchers  {

  sealed trait State {
    def name:String
    override def toString = name
  }
  object START extends State { def name = "Start"}
  object J extends State { def name = "J"}
  object A extends State { def name = "A"}
  object N extends State { def name = "N"}


  class ExampleFsm extends FSM[State,String] {
    in(START) {
      case "J" => J
    }

    in(J) {
      case "A" => A
    }

    in(A) {
      case "N" => N
    }

    stopOn(N)
    startWith(START)
  }

  "The example DSL should" should "accept \"JAN\"" in {
    val fsm = new ExampleFsm()
    fsm.onEvent("J")
    fsm.onEvent("A")
    fsm.onEvent("N")
    fsm.terminated should be(true)
  }

  "The example DSL" should "Work as a partial function" in {
    val fsm = new ExampleFsm()
    val states = ("J" :: "A" :: "N" :: Nil).map(fsm)
    fsm.terminated should be(true)
    states should be (List(J,A,N))
  }
}
