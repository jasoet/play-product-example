package domains

import play.api.libs.json._
import play.api.libs.functional.syntax._

/**
  * Case Class to Map products Table
  *
  * @author Deny Prasetyo.
  */

case class Product(code: String,
                   name: String,
                   description: String,
                   color: String,
                   size: String,
                   pictureUrl: String,
                   price: Int,
                   categoryId: Int)

object Product {
  implicit val reads: Reads[Product] = (
    (JsPath \ "code").read[String] and
      (JsPath \ "name").read[String] and
      (JsPath \ "description").read[String] and
      (JsPath \ "color").read[String] and
      (JsPath \ "size").read[String] and
      (JsPath \ "pictureUrl").read[String] and
      (JsPath \ "price").read[Int] and
      (JsPath \ "categoryId").read[Int]
    ) (Product.apply _)

  implicit val writes: Writes[Product] = (
    (JsPath \ "code").write[String] and
      (JsPath \ "name").write[String] and
      (JsPath \ "description").write[String] and
      (JsPath \ "color").write[String] and
      (JsPath \ "size").write[String] and
      (JsPath \ "pictureUrl").write[String] and
      (JsPath \ "price").write[Int] and
      (JsPath \ "categoryId").write[Int]
    ) (unlift(Product.unapply))
}

