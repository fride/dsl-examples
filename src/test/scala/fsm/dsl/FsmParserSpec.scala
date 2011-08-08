package fsm.dsl

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import fsm.parser.{FsmDslParer}
import fsm.api.{State, Transition, StateMachine}

/**
 * Created by IntelliJ IDEA.
 * User: Friderici
 * Date: 31.07.11
 * Time: 14:20
 * To change this template use File | Settings | File Templates.
 */

class FsmParserSpecs extends FlatSpec with ShouldMatchers  {
  val dslText = """
events
 doorClosed D1CL
 drawerOpened D2OP
 lightOn L1ON
 doorOpened D1OP
 panelClosed PNCL
end

resetEvents
 doorOpened
end

commands
 unlockPanel PNUL
 lockPanel PNLK
 lockDoor D1LK
 unlockDoor D1UL
end

state idle
 actions {unlockDoor, lockPanel}
 doorClosed => active
end

state active
 drawerOpened => waitingForLight
 lightOn => waitingForDrawer
end

state waitingForLight
 lightOn => unlockedPanel
end

state waitingForDrawer
 drawerOpened => unlockedPanel
end

state unlockedPanel
 actions {unlockPanel, lockDoor}
 panelClosed => idle
end
"""

  "The Parse" should  "parse the fowler DSL example" in {
    println(this.dslText)
    println(FsmDslParer.parse(this.dslText))
  }
}
