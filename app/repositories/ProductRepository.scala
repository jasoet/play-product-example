package repositories

import java.sql.Connection
import javax.inject.Singleton

import anorm._
import domains.Product

/**
  * Repository Class for [[domains.Product]]
  * Only define single database operation and cannot hold transaction.
  * Transaction must be placed on services.
  *
  * Every operation need implicit [[java.sql.Connection]].
  *
  * @author Deny Prasetyo.
  */

@Singleton
class ProductRepository {

  /**
    * Insert a new Product.
    * There are no validations logic here such as to limit category tree depth.
    *
    * @param o          Product Object
    * @param connection implicit [[java.sql.Connection]]
    * @return Some(Long) if Success, None if Failure
    */
  def insert(o: Product)(implicit connection: Connection): Option[Long] = {
    SQL(
      """
         INSERT INTO products (code, name, description, color, size, picture_url, price, category_id) VALUES
        ({code},{name},{description},{color},{size},{pictureUrl},{price},{categoryId})
      """)
      .on('code -> o.code,
        'name -> o.name,
        'description -> o.description,
        'color -> o.color,
        'size -> o.size,
        'pictureUrl -> o.pictureUrl,
        'price -> o.price,
        'categoryId -> o.categoryId)
      .executeInsert()
  }


  /**
    * Update existing Product
    *
    * @param code       Existing Product Code
    * @param o          Product Object
    * @param connection implicit [[java.sql.Connection]]
    * @return Row Affected Count
    */
  def update(code: String, o: Product)(implicit connection: Connection): Int = {
    SQL(
      """
        UPDATE products SET name={name}, description={description}, color={color}, size={size}, picture_url={pictureUrl}, price={price}, category_id={categoryId} WHERE code={code}
      """)
      .on('code -> code,
        'name -> o.name,
        'description -> o.description,
        'color -> o.color,
        'size -> o.size,
        'pictureUrl -> o.pictureUrl,
        'price -> o.price,
        'categoryId -> o.categoryId)
      .executeUpdate()
  }

  /**
    * Delete Existing Product
    *
    * @param code       Existing Product Code
    * @param connection implicit [[java.sql.Connection]]
    * @return Row Affected Count
    */
  def delete(code: String)(implicit connection: Connection): Int = {
    SQL(
      """
        DELETE FROM products WHERE code={code}
      """)
      .on('code -> code)
      .executeUpdate()
  }

  /**
    * Find all Products.
    *
    * @param connection implicit [[java.sql.Connection]]
    * @return List of Product
    */
  def findAll()(implicit connection: Connection): List[Product] = {
    val parser: RowParser[Product] =
      Macro.parser[Product](
        "code", "name", "description", "color", "size", "picture_url", "price", "category_id"
      )

    SQL(
      """
        SELECT code, name, description, color, size, picture_url, price, category_id FROM products
      """)
      .as(parser.*)
  }

  /**
    * Find a Product by code.
    *
    * @param connection implicit [[java.sql.Connection]]
    * @return List of Product
    */
  def findOne(code: String)(implicit connection: Connection): Option[Product] = {
    val parser: RowParser[Product] =
      Macro.parser[Product](
        "code", "name", "description", "color", "size", "picture_url", "price", "category_id"
      )

    SQL(
      """
        SELECT code, name, description, color, size, picture_url, price, category_id FROM products WHERE code={code}
      """)
      .on('code -> code)
      .as(parser.singleOpt)
  }

  /**
    * Find all Products by Name.
    * Use `like '%{name}%'` filter
    *
    * @param connection implicit [[java.sql.Connection]]
    * @return List of Products
    */
  def findByName(name: String)(implicit connection: Connection): List[Product] = {
    val parser: RowParser[Product] =
      Macro.parser[Product](
        "code", "name", "description", "color", "size", "picture_url", "price", "category_id"
      )

    SQL(
      """
        |SELECT code, name, description, color, size, picture_url, price, category_id FROM products WHERE name like {name}
      """)
      .on('name -> s"%$name%")
      .as(parser.*)
  }



}
