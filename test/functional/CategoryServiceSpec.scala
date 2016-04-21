package functional

import java.sql.Connection

import domains.Category
import org.flywaydb.core.Flyway
import org.scalatest.BeforeAndAfter
import org.scalatest.Matchers._
import org.scalatestplus.play.{OneAppPerTest, PlaySpec}
import repositories.CategoryRepository
import services.CategoryService

/**
  * Functional Test for Category Service
  *
  * @author Deny Prasetyo.
  */

class CategoryServiceSpec extends PlaySpec with OneAppPerTest with BeforeAndAfter {


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


  "Category Service Data Modification" should {

    val service =
      new CategoryService(DatabaseConfig.database, new CategoryRepository)

    val categoryId: Int = 1

    "success insert" in {
      val category = Category(0, "Electronics", None)
      val insertResult = service.insert(category)

      insertResult.isSuccess shouldBe true
      insertResult.get shouldNot be(0)
    }

    var categoryFromDb: Category = null

    "success find one" in {
      categoryFromDb =
        service.findOne(categoryId).get.get

      categoryFromDb.name shouldEqual "Pakaian Wanita"
    }

    "success update" in {
      val updateCategory = categoryFromDb.copy(name = "Meubel")
      val updateResult = service.update(categoryId, updateCategory).get

      updateResult mustEqual 1

      val updatedCategory =
        service.findOne(categoryId).get.get

      updatedCategory.name shouldEqual "Meubel"
    }

    "success delete" in {
      val deletedId = 81
      val deleteResult =
        service.delete(deletedId).get

      deleteResult mustEqual 1

      val deletedCategory =
        service.findOne(deletedId).get

      deletedCategory.isDefined shouldBe false
    }
  }

  "Category Service Queries" should {
    val service = new CategoryService(DatabaseConfig.database, new CategoryRepository)
    "produce correct data when findAll()" in {
      val categoryList =
        service.findAll().get

      categoryList.size should be(84)
    }

    "produce correct data when findOne" in {
      val findExist =
        service.findOne(1).get

      findExist.isDefined shouldBe true

      val findNone =
        service.findOne(999).get

      findNone.isDefined shouldBe false
    }

    "produce correct data when findByParentId" in {
      val children =
        service.findByParentId(1).get

      children.size should be(5)
      children.map(_.id) should contain only(4, 5, 6, 7, 8)
    }

    "produce correct data when findByName" in {
      val categoriesByName =
        service.findByName("Pakaian").get

      categoriesByName.size should be(6)
      categoriesByName.map(_.id) should contain only(1, 2, 7, 8, 37, 44)
    }

  }

}
