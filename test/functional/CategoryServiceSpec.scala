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


  "Category Service Data Modification" should {

    val service = DatabaseConfig.withDatabase { db =>
      new CategoryService(db, new CategoryRepository)
    }

    val categoryId: Int = 1

    "success insert" in {
      val insertResult = withTransaction { implicit connection =>
        val category = Category(0, "Electronics", None)
        service.insert(category)
      }
      insertResult.isSuccess shouldBe true
      insertResult.get shouldNot be(0)
    }

    var categoryFromDb: Category = null

    "success find one" in {
      categoryFromDb = withTransaction { implicit connection =>
        service.findOne(categoryId).get.get
      }

      categoryFromDb.name shouldEqual "Pakaian Wanita"
    }

    "success update" in {
      val updateResult = withTransaction { implicit connection =>
        val updateCategory = categoryFromDb.copy(name = "Meubel")
        service.update(categoryId, updateCategory)
      }

      updateResult mustEqual 1

      val updatedCategory = withTransaction { implicit c =>
        service.findOne(categoryId).get.get
      }

      updatedCategory.name shouldEqual "Meubel"
    }

    "success delete" in {
      val deletedId = 81
      val deleteResult = withTransaction { implicit c =>
        service.delete(deletedId)
      }

      deleteResult mustEqual 1

      val deletedCategory = withTransaction { implicit c =>
        service.findOne(deletedId)
      }

      deletedCategory.isSuccess shouldBe false
    }
  }

  "Category Service Queries" should {
    val service = DatabaseConfig.withDatabase { db =>
      new CategoryService(db, new CategoryRepository)
    }

    "produce correct data when findAll()" in {
      val categoryList = withTransaction { implicit c =>
        service.findAll().get
      }

      categoryList.size should be(84)
    }

    "produce correct data when findOne" in {
      val findExist = withTransaction { implicit c =>
        service.findOne(1).get
      }

      findExist.isDefined shouldBe true

      val findNone = withTransaction { implicit c =>
        service.findOne(999).get
      }

      findNone.isDefined shouldBe false
    }

    "produce correct data when findByParentId" in {
      val children = withTransaction { implicit c =>
        service.findByParentId(1).get
      }

      children.size should be(5)
      children.map(_.id) should contain only(4, 5, 6, 7, 8)
    }

    "produce correct data when findByName" in {
      val categoriesByName = withTransaction { implicit c =>
        service.findByName("Pakaian").get
      }

      categoriesByName.size should be(6)
      categoriesByName.map(_.id) should contain only(1, 2, 7, 8, 37, 44)
    }

  }

}
