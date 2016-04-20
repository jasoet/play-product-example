package domains

/**
  * Documentation Here
  *
  * @author Deny Prasetyo.
  */

case class Category(id: Int,
                    name: String,
                    parentId: Int,
                    var children: List[Category])
