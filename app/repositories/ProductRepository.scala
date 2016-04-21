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

  private val parser: RowParser[Product] =
    Macro.parser[Product](
      "code", "name", "description", "color", "size", "picture_url", "price", "category_id"
    )

  /**
    * Insert a new Product.
    * Size repesented by single letter Uppercase (XS,S,M,L,XL,XXL)
    *
    * @param o          Product Object
    * @param connection implicit [[java.sql.Connection]]
    * @return Affected Row Count
    */
  def insert(o: Product)(implicit connection: Connection): Int = {
    SQL(
      """
         INSERT INTO products (code, name, description, color, size, picture_url, price, category_id) VALUES
        ({code},{name},{description},{color},{size},{pictureUrl},{price},{categoryId})
      """)
      .on('code -> o.code,
        'name -> o.name,
        'description -> o.description,
        'color -> o.color,
        'size -> o.size.toUpperCase(),
        'pictureUrl -> o.pictureUrl,
        'price -> o.price,
        'categoryId -> o.categoryId)
      .executeUpdate()
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
    SQL(
      """
        SELECT code, name, description, color, size, picture_url, price, category_id FROM products WHERE name like {name}
      """)
      .on('name -> s"%$name%")
      .as(parser.*)
  }

  /**
    * Find all Products by Sizes.
    *
    * @param connection implicit [[java.sql.Connection]]
    * @return List of Products
    */
  def findBySize(size: String)(implicit connection: Connection): List[Product] = {
    assume(size.nonEmpty, "Sizes parameter cannot be empty")
    SQL(
      s"""
        SELECT code, name, description, color, size, picture_url, price, category_id FROM products WHERE size = {sizes}
      """)
      .on('sizes -> s"${size.toUpperCase}")
      .as(parser.*)
  }

  /**
    * Find all Products by Colors.
    *
    * @throws IllegalArgumentException if colors parameter is empty
    * @param connection implicit [[java.sql.Connection]]
    * @return List of Products
    */
  def findByColors(colors: List[String] = List.empty)(implicit connection: Connection): List[Product] = {
    assume(colors.nonEmpty, "Colors parameter cannot be empty")
    val sqlString: String =
      """
        SELECT code, name, description, color, size, picture_url, price, category_id FROM products WHERE
      """

    val sqlParams = colors.zipWithIndex
      .map {
        case (c, i) => s"color LIKE {color$i}"
      }
      .mkString(" or ")

    val namedParameter = colors.zipWithIndex
      .map {
        case (c, i) => NamedParameter(s"color$i", s"%$c%")
      }

    SQL(s"$sqlString $sqlParams")
      .on(namedParameter: _*)
      .as(parser.*)
  }

  /**
    * Find all Products by Price Range.
    *
    * @throws IllegalArgumentException if from greater than to
    * @param connection implicit [[java.sql.Connection]]
    * @return List of Products
    */
  def findByPriceRange(from: Int, to: Int)(implicit connection: Connection): List[Product] = {
    assume(from <= to, "from cannot greater than to")
    SQL(
      s"""
        SELECT code, name, description, color, size, picture_url, price, category_id FROM products WHERE price >= {from} AND price <= {to}
      """)
      .on('from -> from, 'to -> to)
      .as(parser.*)
  }


}
