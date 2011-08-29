package fsm.dsl

import fsm.FSM

sealed trait BookingState {
    def viewName:String
}

object EnterBooking extends BookingState {val viewName ="/pages/booking/enter.html" }
object ReviewBooking extends BookingState {val viewName ="/pages/booking/review.html" }
object BookingCanceled extends BookingState {val viewName ="/pages/booking/cancel.html" }
object BookingConfirmed extends BookingState {val viewName ="/pages/booking/confirmed.html" }

sealed trait BookingEvent;

object Next extends BookingEvent
object Review extends BookingEvent
object Cancel extends BookingEvent
object Confirm extends BookingEvent

/**
 * Booking Workflow from http://static.springsource.org/spring-webflow/docs/2.3.x/reference/html/ch03s06.html
 * in scala.
 */
class Booking extends FSM[BookingState, BookingEvent] {

  in (EnterBooking) {
    case Next => ReviewBooking
    case Cancel => BookingCanceled
  }

  in (ReviewBooking) {
    case Confirm => BookingConfirmed
    case Cancel => BookingCanceled
  }

  startWith(EnterBooking)
  stopOn(BookingCanceled)
  stopOn(BookingConfirmed)

  /*onTransition {
    case (EnterBooking, Next) => println("Prepare Booking ")
    case (ReviewBooking, Cancel) => println("Geht nicht")
  } */
}

object Booking {


}
