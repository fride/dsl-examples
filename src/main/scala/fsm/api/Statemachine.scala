package fsm.api

import java.io.StringWriter
import scalaz.StreamT.Yield

/**
 *
 */
case class StateMachine ( states:Seq[State], current:Option[State] ) {

  def next(event:String) = {

    val nextFsm = for {
      state <- current.toSeq
      transition <- state.transitions
      if (transition.trigger == event)
      newCurrent <-  findState(transition.target)
    } yield {
      this.copy(current = Some(newCurrent))
    }

  }

  def findState(name:String):Option[State] = states.find(_.name == name)

}



