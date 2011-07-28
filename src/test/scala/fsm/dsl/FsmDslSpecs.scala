package fsm.dsl

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FlatSpec
import fsm.dsl.FsmDsl.Fsm
import fsm.api.StateMachine

class FsmDslSpecs extends FlatSpec with ShouldMatchers  {

  val fsm = new Fsm {
       state("1stState") {
         transitions := {
           "2" ==> "2ndState"
         }
       }
      state("2ndState") {
         transitions := {
           "1" ==> "1stState"
         }
       }
    }

  "The example Dsl" should "Have 2 state named sndState and 1stState" in {
    fsm.states.size should equal (2)
    fsm.states.map(_.name) should (contain("1stState") and contain("2ndState"))
  }
}
