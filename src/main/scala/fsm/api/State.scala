package fsm.api

/**
 * Created by IntelliJ IDEA.
 * User: Friderici
 * Date: 25.07.11
 * Time: 17:55
 * To change this template use File | Settings | File Templates.
 */

case class State (name:String, action:List[String], transitions:List[Transition])
