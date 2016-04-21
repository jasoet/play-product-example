package domains

import play.api.libs.json._
import play.api.libs.functional.syntax._

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

object Category {
  implicit val reads: Reads[Category] = (
    (JsPath \ "id").read[Int] and
      (JsPath \ "name").read[String] and
      (JsPath \ "parentId").readNullable[Int]
    ) (Category.apply _)

  implicit val writes: Writes[Category] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "name").write[String] and
      (JsPath \ "parentId").writeNullable[Int]
    ) (unlift(Category.unapply))
}