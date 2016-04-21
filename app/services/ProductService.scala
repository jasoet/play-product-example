package services

import javax.inject.{Inject, Singleton}

import domains.Product
import play.api.db._
import repositories.ProductRepository

import scala.util.Try

/**
  * Service Class for [[domains.Product]]
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
class ProductService @Inject()(db: Database, repository: ProductRepository) {

  def insert(o: Product): Try[Int] = Try {
    db.withTransaction { implicit c =>
      repository.insert(o)
    }
  }

  def update(code: String, o: Product): Try[Int] = Try {
    db.withTransaction { implicit c =>
      repository.update(code, o)
    }
  }

  def delete(code: String): Try[Int] = Try {
    db.withTransaction { implicit c =>
      repository.delete(code)
    }
  }

  def findAll(): Try[List[Product]] = Try {
    db.withTransaction { implicit c =>
      repository.findAll()
    }
  }

  def findOne(code: String): Try[Option[Product]] = Try {
    db.withTransaction { implicit c =>
      repository.findOne(code)
    }
  }

  def findByName(name: String): Try[List[Product]] = Try {
    db.withTransaction { implicit c =>
      repository.findByName(name)
    }
  }

  def findBySize(size: String): Try[List[Product]] = Try {
    db.withTransaction { implicit c =>
      repository.findBySize(size)
    }
  }

  def findByColors(colors: List[String] = List.empty): Try[List[Product]] = Try {
    db.withTransaction { implicit c =>
      repository.findByColors(colors)
    }
  }


  def findByPriceRange(from: Int, to: Int): Try[List[Product]] = Try {
    db.withTransaction { implicit c =>
      repository.findByPriceRange(from, to)
    }
  }


}
