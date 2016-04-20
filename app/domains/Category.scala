package domains

/**
  * Case Class to Map categories Table
  * Mapping [File -> Column]
  * id -> id
  * name -> name
  * parentId -> parent_id
  *
  * Field children is transient, not mapped to Column
  *
  * @author Deny Prasetyo.
  */

case class Category(id: Int,
                    name: String,
                    parentId: Option[Int],
                    var children: List[Category] = List.empty)
