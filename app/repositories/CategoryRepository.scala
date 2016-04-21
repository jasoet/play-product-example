package repositories

import java.sql.Connection
import javax.inject.Singleton

import anorm._
import anorm.SqlParser._
import domains.{Category, CategoryFlat}

/**
  * Repository Class for [[domains.Category]]
  * Only define single database operation and cannot hold transaction.
  * Transaction must be placed on services.
  *
  * Don't Use this Class directly from Controller.
  * Controller must use Services that already handle Transaction.
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
    * @return Some(Long) if Success, None if Failure
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
    *
    * @param connection implicit [[java.sql.Connection]]
    * @return List of Category
    */
  def findByParentId(parentId: Int)(implicit connection: Connection): List[Category] = {
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

  /**
    * List all Categories As [[CategoryFlat]].
    * Product List Category from root (category with parent = null) to grand child (3 depth)
    * Orphaned Category will not shown.
    *
    * @param connection implicit [[java.sql.Connection]]
    * @return List of Category
    */
  def listAsFlat()(implicit connection: Connection): List[CategoryFlat] = {
    val parser: RowParser[CategoryFlat] =
      Macro.parser[CategoryFlat](
        "root_id",
        "root_name",
        "child_id",
        "child_name",
        "grand_child_id",
        "grand_child_name"
      )

    SQL(
      """
        SELECT
          root.id  AS root_id,
          root.name  AS root_name,
          child.id AS child_id,
          child.name AS child_name,
          grand_child.id AS grand_child_id,
          grand_child.name AS grand_child_name
        FROM categories AS root
          LEFT OUTER
          JOIN categories AS child
            ON child.parent_id = root.id
          LEFT OUTER
          JOIN categories AS grand_child
            ON grand_child.parent_id = child.id
        WHERE root.parent_id IS NULL
        ORDER
        BY root_name, child_name, grand_child_name
      """)
      .as(parser.*)
  }


}
