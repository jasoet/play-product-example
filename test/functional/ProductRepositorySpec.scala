package functional

import java.sql.Connection

import domains.Product
import org.flywaydb.core.Flyway
import org.scalatest.BeforeAndAfter
import org.scalatest.Matchers._
import org.scalatestplus.play.{OneAppPerTest, PlaySpec}
import repositories.ProductRepository

/**
  * Functional Test for Product Repository
  *
  * @author Deny Prasetyo.
  */

class ProductRepositorySpec extends PlaySpec with OneAppPerTest with BeforeAndAfter {
  def withTransaction[T](block: Connection => T): T = {
    DatabaseConfig.withDatabase { db =>
      db.withTransaction(block)
    }
  }

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


  "Product Repository Data Modification" should {

    val repository = new ProductRepository
    val productId: String = "veendeva-plain-flare-maxi-dress"

    "success insert" in {
      val insertResult = withTransaction { implicit connection =>
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
        repository.insert(product)
      }
      insertResult shouldNot be(0)
    }

    var productFromDb: Product = null

    "success find one" in {
      productFromDb = withTransaction { implicit connection =>
        repository.findOne(productId).get
      }

      productFromDb.name shouldEqual "Veendeva Plain Flare Maxi Dress"
    }

    "success update" in {
      val updateResult = withTransaction { implicit connection =>
        val updateProduct = productFromDb.copy(name = "Jasoet Updated Plain Flare Maxi Dress")
        repository.update(productId, updateProduct)
      }

      updateResult mustEqual 1

      val updatedProduct = withTransaction { implicit c =>
        repository.findOne(productId).get
      }

      updatedProduct.name shouldEqual "Jasoet Updated Plain Flare Maxi Dress"
    }

    "success delete" in {
      val deletedId: String = "shizila-ethnic-casual-couple-set"
      val deleteResult = withTransaction { implicit c =>
        repository.delete(deletedId)
      }

      deleteResult mustEqual 1

      val deletedProduct = withTransaction { implicit c =>
        repository.findOne(deletedId)
      }

      deletedProduct.isDefined shouldBe false
    }
  }

  "Product Repository Queries" should {
    val repository = new ProductRepository

    "produce correct data when findAll()" in {
      val productList = withTransaction { implicit c =>
        repository.findAll()
      }

      productList.size should be(12)
    }

    "produce correct data when findOne" in {
      val findExist = withTransaction { implicit c =>
        repository.findOne("nashiralia-abstract-layer-mini-dress")
      }

      findExist.isDefined shouldBe true

      val findNone = withTransaction { implicit c =>
        repository.findOne("very-random-id-that-must-be-not-exists")
      }

      findNone.isDefined shouldBe false
    }

    "produce correct data when findByName" in {
      val productsByName = withTransaction { implicit c =>
        repository.findByName("Dress")
      }

      productsByName.size should be(7)
      productsByName.forall(_.name.contains("Dress")) shouldBe true
    }

    "produce correct data when findBySize" in {

      val productBySize = withTransaction { implicit c =>
        repository.findBySize("L")
      }

      productBySize.size should be(3)
      productBySize.forall(_.size.equalsIgnoreCase("L")) shouldBe true
    }


    "produce correct data when findByColor" in {
      val colors = List("Black", "Navi", "Tosca")
      val productByColors = withTransaction { implicit c =>
        repository.findByColors(colors)
      }

      productByColors.size should be(7)
      productByColors.forall { p =>
        p.color.contains("Black") || p.color.contains("Navi") || p.color.contains("Tosca")
      } shouldBe true
    }


    "produce correct data when findByPriceRange" in {
      val from = 75000
      val to = 100000
      val productByPriceRange = withTransaction { implicit c =>
        repository.findByPriceRange(from, to)
      }

      productByPriceRange.size should be(8)
      productByPriceRange.forall(p => p.price >= from && p.price <= to) shouldBe true
    }


  }

}
