package html

import org.specs2.mutable.Specification

/**
 * Created by IntelliJ IDEA.
 * User: Friderici
 * Date: 09.09.11
 * Time: 14:08
 * To change this template use File | Settings | File Templates.
 */

class HtmlBuilderSpecification extends Specification {

  def makeExamplePage = {
    val b = new HtmlBuilder();
    import b._
    b.page("Eine Beispiel Seite") {
      body() {
        b.p(Map("class" -> "important")) {
          text("Dieser Text steht in einem Paragraphen") {
            ul() {
              for (i <- 1.to(6)) {
                li(Map("selected" -> (if (i % 2 == 0) "odd" else "even"))) {
                    text("Element " + i) {}
                }
              }
            }
          }
          text("Dieser zweite Text steht in einem Paragraphen") {
              text(" Ein geschachtelter <b>Text</b>") {}
          }
        }
      }
    }
  }

  "The given Example Builder" should {
    "evaluate to valid html"  in {
      makeExamplePage.toString.trim() should_== """
<html <body <p class="important"Dieser Text steht in einem Paragraphen<ul <li selected="even"Element 1</li>
<li selected="odd"Element 2</li>
<li selected="even"Element 3</li>
<li selected="odd"Element 4</li>
<li selected="even"Element 5</li>
<li selected="odd"Element 6</li></ul>
Dieser zweite Text steht in einem Paragraphen Ein geschachtelter <b>Text</b>
<select name="beruf" ><option value="1">Entwickler</option><option value="2">Berater</option><option value="3">Architekt</option></select></p></body></html>

        """.trim()
    }
  }
}
