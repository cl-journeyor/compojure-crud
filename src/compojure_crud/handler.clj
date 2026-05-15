(ns compojure-crud.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.anti-forgery :as antif]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [cheshire.core :as json]
            [compojure-crud.config :as cfg]
            [compojure-crud.responses :as ress]
            [compojure-crud.validators :as v]
            [jnorlib-db.core :as db]))

(def fetch-page
  (GET "/"
    [page]
    (let [page-num (v/get-int page 1)
          page-size 10
          mng (db/manager (cfg/new-ds))
          sql (str
               "select * from employees"
               " order by id"
               " limit " page-size
               " offset " (* (- page-num 1) page-size))]
      (try
        (json/encode {:token antif/*anti-forgery-token*
                      :employees (db/exec-query! mng sql #{:id
                                                           :name
                                                           :role
                                                           :salary
                                                           :added_date})})
        (catch Exception e (ress/internal-server-error! e))
        (finally (.close mng))))))

(defroutes app-routes
  fetch-page
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
