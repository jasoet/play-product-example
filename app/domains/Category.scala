package domains

/**
  * Case Class to Map categories Table
  * Mapping [File -> Column]
  * id -> id
  * name -> name
  * parentId -> parent_id
  *
  * @author Deny Prasetyo.
  */

case class Category(id: Int,
                    name: String,
                    parentId: Option[Int])
