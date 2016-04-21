package domains

/**
  * Class for represent Category hierarchy as flat.
  * Currently only store 3 depth layer.
  *
  * @author Deny Prasetyo.
  */

case class CategoryFlat(rootId: Int,
                        rootName: String,
                        childId: Int,
                        childName: String,
                        grandChildId: Int,
                        grandChildName: String)
