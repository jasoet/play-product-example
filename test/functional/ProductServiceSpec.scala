package functional

import domains.Product
import org.flywaydb.core.Flyway
import org.scalatest.BeforeAndAfter
import org.scalatest.Matchers._
import org.scalatestplus.play.{OneAppPerTest, PlaySpec}
import repositories.ProductRepository
import services.ProductService

/**
  * Functional Test for Product Service
  *
  * @author Deny Prasetyo.
  */

class ProductServiceSpec extends PlaySpec with OneAppPerTest with BeforeAndAfter {

  /**
    * Run Database Migration Before Test
    */
  before {
    DatabaseConfig.withDatabase { db =>
      val flyway = new Flyway
      flyway.setDataSource(db.dataSource)
      flyway.migrate()
    }
  }

  /**
    * Clean Database after test
    */
  after {
    DatabaseConfig.withDatabase { db =>
      val flyway = new Flyway
      flyway.setDataSource(db.dataSource)
      flyway.clean()
    }
  }


  "Product Service Data Modification" should {

    val service =
      new ProductService(DatabaseConfig.database, new ProductRepository)

    val productId: String = "veendeva-plain-flare-maxi-dress"

    "success insert" in {
      val product = Product(
        "winzifara-rope-round-necklace",
        "Winzifara Rope Round Necklace",
        """
          Length 25 cm, Width 18 cm
          Material : Rope
        """,
        "Black, Pink, White dan Green",
        "S",
        "https://salestock-public-prod.global.ssl.fastly.net/product_images/62cda18bd00fec5f2d3007ef1290a0de_600.jpg",
        59000,
        16)
      val insertResult = service.insert(product)

      insertResult shouldNot be(0)
    }

    var productFromDb: Product = null

    "success find one" in {
      productFromDb = service.findOne(productId).get.get

      productFromDb.name shouldEqual "Veendeva Plain Flare Maxi Dress"
    }

    "success update" in {
      val updateProduct = productFromDb.copy(name = "Jasoet Updated Plain Flare Maxi Dress")
      val updateResult = service.update(productId, updateProduct).get

      updateResult mustEqual 1

      val updatedProduct =
        service.findOne(productId).get.get

      updatedProduct.name shouldEqual "Jasoet Updated Plain Flare Maxi Dress"
    }

    "success delete" in {
      val deletedId: String = "shizila-ethnic-casual-couple-set"
      val deleteResult =
        service.delete(deletedId).get

      deleteResult mustEqual 1

      val deletedProduct =
        service.findOne(deletedId).get

      deletedProduct.isDefined shouldBe false
    }
  }

  "Product Service Queries" should {
    val service = new ProductService(DatabaseConfig.database, new ProductRepository)


    "produce correct data when findAll()" in {
      val productList =
        service.findAll().get

      productList.size should be(12)
    }

    "produce correct data when findOne" in {
      val findExist =
        service.findOne("nashiralia-abstract-layer-mini-dress").get

      findExist.isDefined shouldBe true

      val findNone =
        service.findOne("very-random-id-that-must-be-not-exists").get

      findNone.isDefined shouldBe false
    }

    "produce correct data when findByName" in {
      val productsByName =
        service.findByName("Dress").get

      productsByName.size should be(7)
      productsByName.forall(_.name.contains("Dress")) shouldBe true
    }

    "produce correct data when findBySize" in {

      val productBySize =
        service.findBySize("L").get

      productBySize.size should be(3)
      productBySize.forall(_.size.equalsIgnoreCase("L")) shouldBe true
    }


    "produce correct data when findByColor" in {
      val colors = List("Black", "Navi", "Tosca")
      val productByColors =
        service.findByColors(colors).get

      productByColors.size should be(7)
      productByColors.forall { p =>
        p.color.contains("Black") || p.color.contains("Navi") || p.color.contains("Tosca")
      } shouldBe true
    }


    "produce correct data when findByPriceRange" in {
      val from = 75000
      val to = 100000
      val productByPriceRange =
        service.findByPriceRange(from, to).get

      productByPriceRange.size should be(8)
      productByPriceRange.forall(p => p.price >= from && p.price <= to) shouldBe true
    }


  }

}
