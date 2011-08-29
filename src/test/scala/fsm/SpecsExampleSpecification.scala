import org.specs2._

class SpecsExampleSpecification extends Specification { def is =
  "Dies ist eine Specifikation für Listen"    ^
                                              p ^
  "Die LIste 1,2,3,4"                         ^
  "Muss invertertiert gleich 4,3,2,1 sein"    ! e1 ^
  "take 2 soll 3,4 liefern"                   ! e2 ^
  end

  def e1 = List(1,2,3,4).reverse must beEqualTo( List(4,3,2,1))
  def e2 = List(1,2,3,4).take(2) must beEqualTo( List(1,2))
}
