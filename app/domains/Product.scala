package domains

/**
  * Documentation Here
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
