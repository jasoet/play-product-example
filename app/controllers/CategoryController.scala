package controllers

import javax.inject.{Inject, Singleton}

import play.api.mvc.Controller
import services.{CategoryService, ProductService}

/**
  * Controller Class to Serve Category Rest API
  *
  * @author Deny Prasetyo.
  */

@Singleton
class CategoryController @Inject()(service: CategoryService) extends Controller {

  def list() = play.mvc.Results.TODO

  def get(code: String) = play.mvc.Results.TODO

  def create() = play.mvc.Results.TODO

  def modify(code: String) = play.mvc.Results.TODO

  def delete(code: String) = play.mvc.Results.TODO
}
