(ns compojure-crud.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.anti-forgery :as antif]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.util.response :as resp]
            [cheshire.core :as json]
            [misc.core :as misc]
            [compojure-crud.config :as cfg]
            [compojure-crud.responses :as ress]
            [compojure-crud.validators :as v]
            [jnorlib-db.core :as db])
  (:import [java.math BigDecimal]
           [java.sql Date]
           [java.time LocalDate]
           [java.util Objects]))

(def get-token
  (GET "/api/token"
    []
    (json/encode {:X-CSRF-Token antif/*anti-forgery-token*})))

(def fetch-page
  (GET "/api"
    [page]
    (let [page-num (v/get-int page 1)
          page-size 10
          mng (db/manager cfg/ds)
          sql (str
               "select * from employees"
               " order by id"
               " limit " page-size
               " offset " (* (- page-num 1) page-size))]
      (try
        (json/encode {:employees (db/exec-query! mng sql #{:id
                                                           :name
                                                           :role
                                                           :salary
                                                           :added_date})})
        (catch Exception e (ress/internal-server-error! e))
        (finally (.close mng))))))

(def insert-employee
  (POST "/api"
    request
    (if-let [req (misc/try-convert-vals
                  (:params request)
                  {:name #(Objects/requireNonNull %)
                   :role #(Objects/requireNonNull %)
                   :salary #(BigDecimal. %)})]
      (let [mng (db/manager cfg/ds)
            sql (str
                 "insert into employees"
                 " values (default, ?, ?, ?, ?)")]
        (try
          (db/exec! mng sql [(:name req)
                             (:role req)
                             (:salary req)
                             (Date/valueOf (LocalDate/now))])
          (ress/ok)
          (catch Exception e (ress/internal-server-error! e))))
      (ress/bad-request))))

(def update-employee
  (PUT "/api"
    request
    (if-let [req (misc/try-convert-vals
                  (:params request)
                  {:id #(Long/parseLong %)
                   :name #(Objects/requireNonNull %)
                   :role #(Objects/requireNonNull %)
                   :salary #(BigDecimal. %)})]
      (let [mng (db/manager cfg/ds)
            sql (str
                 "update employees set"
                 " name = ?,"
                 " role = ?,"
                 " salary = ?"
                 " where id = ?")]
        (try
          (db/exec! mng sql [(:name req)
                             (:role req)
                             (:salary req)
                             (:id req)])
          (ress/ok)
          (catch Exception e (ress/internal-server-error! e))))
      (ress/bad-request))))

(def delete-employee
  (DELETE "/api"
    request
    (if-let [req (misc/try-convert-vals
                  (:params request)
                  {:id #(Long/parseLong %)})]
      (let [mng (db/manager cfg/ds)
            sql (str
                 "delete from employees"
                 " where id = ?")]
        (try
          (db/exec! mng sql [(:id req)])
          (ress/ok)
          (catch Exception e (ress/internal-server-error! e))))
      (ress/bad-request))))

(defroutes app-routes
  get-token
  fetch-page
  insert-employee
  update-employee
  delete-employee
  (GET "/" [] (resp/redirect "/index.html"))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (wrap-defaults site-defaults)
      #_(ring.middleware.cors/wrap-cors :access-control-allow-origin
                                      #"http://localhost:8080"

                                      :access-control-allow-methods
                                      [:get :put :post :delete])))
