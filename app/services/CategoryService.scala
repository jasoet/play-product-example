package services

import javax.inject.{Inject, Singleton}

import domains.Category
import play.api.db._
import repositories.CategoryRepository

import scala.util.Try

/**
  * Service Class for [[domains.Category]]
  * Transaction must be placed here.
  *
  * Return Value should wrapped in Try[T]
  *
  * Add all Required Validation Here.
  * If Validation fails, should return Failure with Correct Exception Type
  *
  * For now we use Try[T], in the future we can use Future[T] if necessary
  *
  * @author Deny Prasetyo.
  */

@Singleton
class CategoryService @Inject()(db: Database, repository: CategoryRepository) {

  def insert(o: Category): Try[Option[Long]] = Try {
    db.withTransaction { implicit c =>
      repository.insert(o)
    }
  }

  def update(id: Int, o: Category): Try[Int] = Try {
    db.withTransaction { implicit c =>
      repository.update(id, o)
    }
  }

  def delete(id: Int): Try[Int] = Try {
    db.withTransaction { implicit c =>
      repository.delete(id)
    }
  }

  def findAll(): Try[List[Category]] = Try {
    db.withTransaction { implicit c =>
      repository.findAll()
    }
  }

  def findOne(id: Int): Try[Option[Category]] = Try {
    db.withTransaction { implicit c =>
      repository.findOne(id)
    }
  }

  def findByName(name: String): Try[List[Category]] = Try {
    db.withTransaction { implicit c =>
      repository.findByName(name)
    }
  }

  def findByParentId(parentId: Int): Try[List[Category]] = Try {
    db.withTransaction { implicit c =>
      repository.findByParentId(parentId)
    }
  }
}
