package functional


import java.sql.Connection

import domains.Category
import org.flywaydb.core.Flyway
import org.scalatest.BeforeAndAfterAll
import org.scalatest.Matchers._
import org.scalatestplus.play.{OneAppPerTest, PlaySpec}
import repositories.CategoryRepository

/**
  * Functional Test for Category Repository
  *
  * @author Deny Prasetyo.
  */

class CategoryRepositorySpec extends PlaySpec with OneAppPerTest with BeforeAndAfterAll {
  def withTransaction[T](block: Connection => T): T = {
    DatabaseConfig.withDatabase { db =>
      db.withTransaction(block)
    }
  }

  /**
    * Run Database Migration Before Test
    */
  override def beforeAll(): Unit = {
    DatabaseConfig.withDatabase { db =>
      val flyway = new Flyway
      flyway.setDataSource(db.dataSource)
      flyway.migrate()
    }
  }

  /**
    * Clean Database after test
    */
  override def afterAll(): Unit = {
    DatabaseConfig.withDatabase { db =>
      val flyway = new Flyway
      flyway.setDataSource(db.dataSource)
      flyway.clean()
    }
  }


  "Category Repository" should {

    val repository = new CategoryRepository
    var categoryId: Int = 0

    "success insert" in {
      val insertResult = withTransaction { implicit connection =>
        val category = Category(0, "Electronics", None)
        repository.insert(category)
      }
      insertResult.isDefined shouldBe true
      categoryId = insertResult.get.toInt
      categoryId shouldNot be(0)
    }

    var categoryFromDb: Category = null

    "success find one" in {
      categoryFromDb = withTransaction { implicit connection =>
        repository.findOne(categoryId).get
      }

      categoryFromDb.name shouldEqual "Electronics"
    }

    "success update" in {
      val updateResult = withTransaction { implicit connection =>
        val updateCategory = categoryFromDb.copy(name = "Meubel")
        repository.update(categoryId, updateCategory)
      }

      updateResult mustEqual 1

      val updatedCategory = withTransaction { implicit c =>
        repository.findOne(categoryId).get
      }

      updatedCategory.name shouldEqual "Meubel"
    }

    "success delete" in {
      val deleteResult = withTransaction { implicit c =>
        repository.delete(categoryId)
      }

      deleteResult mustEqual 1

      val deletedCategory = withTransaction { implicit c =>
        repository.findOne(categoryId)
      }

      deletedCategory.isDefined shouldBe false
    }
  }

}
