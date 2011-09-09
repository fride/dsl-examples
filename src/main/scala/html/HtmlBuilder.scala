package html

import scala.util.DynamicVariable
import collection.mutable.ListBuffer

object HtmlBuilder {
  case class Context(build:Option[HtmlBuilder], current: Option[HtmlElement])

  // not needed right now.
  implicit def tuple2HtmlElement(value:(String, Map[String,String])) = new BlockElement(value._1, value._2)

  // not needed right now.
  implicit def stringToTextElement(value:String) = new TextElement(value)

  def examplePage() = {
    val b = new HtmlBuilder();
    import b._ // may conflict with implicits!?
    page (title = "Eine Beispiel Seite")  {
      body() {
        p(Map("class" -> "important")) {
          text("Dieser Text steht in einem Paragraphen") {
            ul() {
              // Embedded expressions!
              for (i <- 1.to(6)) {
                li(Map("selected" -> (if (i % 2 == 0) "odd" else "even"))) {
                    text("Element " + i) {}
                }
              }
            }
          }
          text("Dieser zweite Text steht in einem Paragraphen") {
              text(" Ein geschachtelter <b>Text</b>"){}
          }
          selection("beruf", attributes = Map("onSelect" -> "alert('clicked me');")){
            ("1" -> "Entwickler") :: ("2" -> "Berater") :: ("3" -> "Architekt") :: Nil  // List with tuples
          }
        }
      }
    }
  }
}

class HtmlBuilder {
  import HtmlBuilder._
  // Wir benutzern eine DynamicVariable um den aktuellen Kontext zu
  // verfolgen. So bekommt jeder Aufruf von blockElement seinen
  // Kontext...
  val context = new DynamicVariable(Context(None,None))

  def page(title:String, attributes:Map[String, String] = Map())(f: =>Unit) = {
    val page = new HtmlPage(title)
    context.withValue(Context(Some(this), Some(page))) {
      blockElement("head", f = blockElement("title", f = text(title){}))
      f
      page
    }
  }

  def body(attributes:Map[String, String]=Map())(f: => Unit) = blockElement("body", attributes,f)

  def p(attributes:Map[String, String]=Map()) (f: => Unit) = blockElement("p", attributes,f)

  def ul(attributes:Map[String, String]=Map()) (f: => Unit) = blockElement("ul", attributes,f)

  def li(attributes:Map[String, String]=Map()) (f: => Unit) = blockElement("li", attributes,f)

  def a(href:String,  attributes:Map[String, String]) (f: => Unit) = blockElement("li", attributes + ("href" -> href),f)

  def selection(name:String, attributes:Map[String, String]=Map())( f: => Seq[(String, String)]) = {
    val res = new Selection(name,attributes, f)
    for (parent <- context.value.current) { parent addChild res}
    res
  }

  def text(text:String) (f: => Unit = ()) = {
    val res = new TextElement(text)
    for (parent <- context.value.current) { parent addChild res}
     context.withValue(Context(Some(this), Some(res))) {
      f
      res
    }
  }
  private def blockElement(name:String, attributes:Map[String, String] = Map(), f: => Unit) = {
    val res = new BlockElement(name, attributes)
    for (parent <- context.value.current) { parent addChild res}
    context.withValue(Context(Some(this), Some(res))) {
      f
      res
    }
  }
}
trait HtmlElement {
  private val myChildren:ListBuffer[HtmlElement] = new ListBuffer()
  def children:Seq[HtmlElement]  = myChildren.toSeq
  def attributes:Map[String,String] = Map()
  def name:String
  def addChild(child: HtmlElement) = {
    myChildren += child
    this
  }
  override def toString = (
      "<" +name + " "
        + attributes.map(p => """%s="%s"""".format(p._1,p._2)).mkString(" ")
        + " >"
        + children.map(_.toString).mkString("\n") + "</" + name +">"
    )

  def <<(child: HtmlElement) = addChild(child)
}

class BlockElement(val name:String, override val attributes:Map[String, String] = Map()) extends HtmlElement

class HtmlPage(val title:String) extends BlockElement("html", Map())

class TextElement(val text:String) extends BlockElement("text", Map()) {
   override def toString = text + children.map(_.toString).mkString("")
}
class Selection(val inputName:String,
                override val attributes:Map[String, String] = Map(),
                var elements:Seq[(String, String)]) extends HtmlElement {

  def name = "select"
  override def toString = (
      "<select name=\"" + inputName + "\" >"
      + (for (elem <- elements) yield {"<option value=\"" + elem._1 + "\">" + elem._2 + "</option>"}).mkString("")
      + "</select>"
    )
}

