package controllers

import javax.inject.{Inject, Singleton}

import domains.Category
import domains.Category._
import play.api.Logger
import play.api.libs.json._
import play.api.mvc.{Controller, _}
import services.CategoryService

/**
  * Controller Class to Serve Category Rest API
  *
  * @author Deny Prasetyo.
  */

@Singleton
class CategoryController @Inject()(service: CategoryService) extends Controller {

  def list(name: String) = Action { request =>
    val listCategory =
      name.trim.isEmpty match {
        case true => service.findAll()
        case false => service.findByName(name)
      }

    listCategory
      .map { list => Ok(Json.toJson(list)) }
      .recover {
        case ex =>
          Logger.error(ex.getMessage, ex)
          InternalServerError(ex.getMessage)
      }
      .get
  }

  def get(id: Int) = Action {
    service.findOne(id)
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
        _.validate[Category]
          .map { category =>
            service.insert(category)
              .map { _ =>
                Ok("Category Successfully Created!")
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

  def modify(id: Int) = Action { request =>
    request.body.asJson
      .map {
        _.validate[Category]
          .map { category =>
            service.update(id, category)
              .map { _ =>
                Ok("Category Successfully Updated!")
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

  def delete(id: Int) = Action {
    service.delete(id)
      .map { affectedRow =>
        Ok(s"Delete $affectedRow rows Successfully!!")
      }
      .recover {
        case e =>
          Logger.error(s"Delete Category Error: ${e.getMessage}", e)
          InternalServerError(e.getMessage)
      }
      .get
  }
}
