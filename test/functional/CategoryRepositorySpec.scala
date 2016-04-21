package functional


import java.sql.Connection

import domains.Category
import org.flywaydb.core.Flyway
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll}
import org.scalatest.Matchers._
import org.scalatestplus.play.{OneAppPerTest, PlaySpec}
import repositories.CategoryRepository

/**
  * Functional Test for Category Repository
  *
  * @author Deny Prasetyo.
  */

class CategoryRepositorySpec extends PlaySpec with OneAppPerTest with BeforeAndAfter {
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


  "Category Repository Data Modification" should {

    val repository = new CategoryRepository
    val categoryId: Int = 1

    "success insert" in {
      val insertResult = withTransaction { implicit connection =>
        val category = Category(0, "Electronics", None)
        repository.insert(category)
      }
      insertResult.isDefined shouldBe true
      insertResult.get shouldNot be(0)
    }

    var categoryFromDb: Category = null

    "success find one" in {
      categoryFromDb = withTransaction { implicit connection =>
        repository.findOne(categoryId).get
      }

      categoryFromDb.name shouldEqual "Pakaian Wanita"
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
      val deletedId = 81
      val deleteResult = withTransaction { implicit c =>
        repository.delete(deletedId)
      }

      deleteResult mustEqual 1

      val deletedCategory = withTransaction { implicit c =>
        repository.findOne(deletedId)
      }

      deletedCategory.isDefined shouldBe false
    }
  }

  "Category Repository Queries" should {
    val repository = new CategoryRepository

    "produce correct data when findAll()" in {
      val categoryList = withTransaction { implicit c =>
        repository.findAll()
      }

      categoryList.size should be(84)
    }

    "produce correct data when findOne" in {
      val findExist = withTransaction { implicit c =>
        repository.findOne(1)
      }

      findExist.isDefined shouldBe true

      val findNone = withTransaction { implicit c =>
        repository.findOne(999)
      }

      findNone.isDefined shouldBe false
    }

    "produce correct data when findByParentId" in {
      val children = withTransaction { implicit c =>
        repository.findByParentId(1)
      }

      children.size should be(5)
      children.map(_.id) should contain only(4, 5, 6, 7, 8)
    }

    "produce correct data when findByName" in {
      val categoriesByName = withTransaction { implicit c =>
        repository.findByName("Pakaian")
      }

      categoriesByName.size should be(6)
      categoriesByName.map(_.id) should contain only(1, 2, 7, 8, 37, 44)
    }

    "product correct data when listAsFlat" in {
      val categoryFlatList = withTransaction { implicit c =>
        repository.listAsFlat()
      }
      categoryFlatList.size should be(67)
      categoryFlatList.map(_.rootId) should contain only(1, 2, 3)

    }

  }

}
