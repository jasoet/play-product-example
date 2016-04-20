package domains

/**
  * Documentation Here
  *
  * @author Deny Prasetyo.
  */

case class Category(id: Int,
                    name: String,
                    parentId: Option[Int],
                    var children: List[Category])
