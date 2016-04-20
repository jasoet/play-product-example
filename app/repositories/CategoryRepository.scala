package repositories

import java.sql.Connection
import javax.inject.Singleton
import anorm._
import anorm.SqlParser._
import domains.Category

/**
  * Repository Class for [[domains.Category]]
  * Only define single database operation and cannot hold transaction.
  * Transaction must be placed on services.
  *
  * Every operation need implicit [[java.sql.Connection]].
  *
  * @author Deny Prasetyo.
  */

@Singleton
class CategoryRepository {

  /**
    * Insert a new Category.
    * There are no validations logic here such as to limit category tree depth.
    *
    * @param o          Category Object
    * @param connection implicit [[java.sql.Connection]]
    * @return True if Success, False otherwise
    */
  def insert(o: Category)(implicit connection: Connection): Option[Long] = {
    SQL(
      """
         INSERT INTO categories (id, name, parent_id) VALUES
        (nextval('categories_id_seq'), {name}, {parentId})
      """)
      .on('name -> o.name, 'parentId -> o.parentId)
      .executeInsert()
  }

  /**
    * Update existing Category
    * There are no Validations here such as parent id check to prevent orphaned category
    *
    * @param id         Existing Category Id
    * @param o          Category Object
    * @param connection implicit [[java.sql.Connection]]
    * @return Row Affected Count
    */
  def update(id: Int, o: Category)(implicit connection: Connection): Int = {
    SQL(
      """
        UPDATE categories SET name={name},parent_id={parentId} WHERE id={updatedId}
      """)
      .on('name -> o.name, 'parentId -> o.parentId, 'updatedId -> id)
      .executeUpdate()
  }

  /**
    * Delete Existing Category
    * There are no Validations here such as check if category has children.
    * You need to remove the children manually and should be handled by Services.
    *
    * @param id         Existing Category Id
    * @param connection implicit [[java.sql.Connection]]
    * @return Row Affected Count
    */
  def delete(id: Int)(implicit connection: Connection): Int = {
    SQL(
      """
        DELETE FROM categories WHERE id={deletedId}
      """)
      .on('deletedId -> id)
      .executeUpdate()
  }

  /**
    * Find all Categories.
    * Field children not populated here, must be populated by Services
    *
    * @param connection implicit [[java.sql.Connection]]
    * @return List of Category
    */
  def findAll()(implicit connection: Connection): List[Category] = {
    val parser: RowParser[Category] =
      get[Int]("id") ~ get[String]("name") ~ get[Option[Int]]("parent_id") map {
        case id ~ name ~ parentId => Category(id, name, parentId)
      }

    SQL(
      """
        SELECT id,name,parent_id FROM categories
      """)
      .as(parser.*)
  }

  /**
    * Find a Category by Id.
    * Field children not populated here, must be populated by Services
    *
    * @param connection implicit [[java.sql.Connection]]
    * @return List of Category
    */
  def findOne(id: Int)(implicit connection: Connection): Option[Category] = {
    val parser: RowParser[Category] =
      get[Int]("id") ~ get[String]("name") ~ get[Option[Int]]("parent_id") map {
        case idx ~ name ~ parentId => Category(idx, name, parentId)
      }

    SQL(
      """
        SELECT id,name,parent_id FROM categories WHERE id={id}
      """)
      .on('id -> id)
      .as(parser.singleOpt)
  }

  /**
    * Find Category's Children by parentId.
    * Field children not populated here, must be populated by Services
    *
    * @param connection implicit [[java.sql.Connection]]
    * @return List of Category
    */
  def findChildren(parentId: Int)(implicit connection: Connection): List[Category] = {
    val parser: RowParser[Category] =
      get[Int]("id") ~ get[String]("name") ~ get[Option[Int]]("parent_id") map {
        case id ~ name ~ parentIdx => Category(id, name, parentIdx)
      }

    SQL(
      """
        SELECT id,name,parent_id FROM categories WHERE parent_id={parentId}
      """)
      .on('parentId -> parentId)
      .as(parser.*)
  }

  /**
    * Find all Categories by Name.
    * Use `like '%{name}%'` filter
    * Field children not populated here, must be populated by Services
    *
    * @param connection implicit [[java.sql.Connection]]
    * @return List of Category
    */
  def findByName(name: String)(implicit connection: Connection): List[Category] = {
    val parser: RowParser[Category] =
      get[Int]("id") ~ get[String]("name") ~ get[Option[Int]]("parent_id") map {
        case id ~ namex ~ parentId => Category(id, namex, parentId)
      }

    SQL(
      """
        SELECT id,name,parent_id FROM categories WHERE name like {name}
      """)
      .on('name -> s"%$name%")
      .as(parser.*)
  }

}
