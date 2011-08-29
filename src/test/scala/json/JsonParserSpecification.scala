package json

import org.specs2.mutable._

/**
 * Created by IntelliJ IDEA.
 * User: Friderici
 * Date: 29.08.11
 * Time: 21:31
 * To change this template use File | Settings | File Templates.
 */
class JsonParserSpecification extends Specification {

  lazy val jsonExample = """
  {
    "addresBook": [
    {
        "name": "Jan Friderici",
        "email": ["jan.friderici@itemis.de", "friderici@itemis.de", "janfriderici@googlemail.com"],
        "occupuation": "Developer"
      }
    ]
  }
  """

  "The json parser" should {
    "parse the given example" in {
      val parsed = JsonParser.parseAll(JsonParser.value, jsonExample)
      parsed.get should_== Map(
        "addresBook" -> List(
            Map (
	          "name" -> "Jan Friderici",
	          "email"->  List("jan.friderici@itemis.de", "friderici@itemis.de", "janfriderici@googlemail.com"),
	          "occupuation" -> "Developer")))
    }
  }
}
