package controllers

import javax.inject.{Inject, Singleton}

import domains.Product
import domains.Product._
import play.api.Logger
import play.api.libs.json.{JsError, Json}
import play.api.mvc.{Controller, _}
import services.ProductService

/**
  * Controller Class to Serve Product Rest API
  *
  * @author Deny Prasetyo.
  */

@Singleton
class ProductController @Inject()(service: ProductService) extends Controller {

  def list(size: String, colors: String, from: Int, to: Int) = Action {
    service.findByFiler(size, colors.split(",").toList, from, to)
      .map { list => Ok(Json.toJson(list)) }
      .recover {
        case ex =>
          Logger.error(ex.getMessage, ex)
          InternalServerError(ex.getMessage)
      }
      .get
  }

  def get(code: String) = Action {
    service.findOne(code)
      .map {
        _
          .map { c => Ok(Json.toJson(c)) }
          .getOrElse(NotFound)
      }
      .recover {
        case e =>
          Logger.error(e.getMessage, e)
          InternalServerError(e.getMessage)
      }
      .get
  }

  def create() = Action { request =>
    request.body.asJson
      .map {
        _.validate[Product]
          .map { product =>
            service.insert(product)
              .map { _ =>
                Ok("Product Successfully Created!")
              }
              .recover {
                case e =>
                  Logger.error(s"Create Error: ${e.getMessage}", e)
                  BadRequest(e.getMessage)
              }
              .get
          }
          .recoverTotal {
            e =>
              Logger.error(s"Validate Json Error: ${e.errors.mkString(",")}")
              BadRequest(JsError.toJson(e))
          }
      }
      .getOrElse {
        BadRequest("Expecting Json Data")
      }
  }

  def modify(code: String) = Action { request =>
    request.body.asJson
      .map {
        _.validate[Product]
          .map { product =>
            service.update(code, product)
              .map { _ =>
                Ok("Product Successfully Updated!")
              }
              .recover {
                case e =>
                  Logger.error(s"Update Error: ${e.getMessage}", e)
                  BadRequest(e.getMessage)
              }
              .get
          }
          .recoverTotal {
            e =>
              Logger.error(s"Validate Json Error: ${e.errors.mkString(",")}")
              BadRequest(JsError.toJson(e))
          }
      }
      .getOrElse {
        BadRequest("Expecting Json data")
      }
  }

  def delete(code: String) = Action {
    service.delete(code)
      .map { affectedRow =>
        Ok(s"Delete $affectedRow rows Successfully!!")
      }
      .recover {
        case e =>
          Logger.error(s"Delete Product Error: ${e.getMessage}", e)
          InternalServerError(e.getMessage)
      }
      .get
  }
}
