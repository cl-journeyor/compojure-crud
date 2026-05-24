(ns compojure-crud.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.anti-forgery :as antif]
            [ring.middleware.cors :as cors]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [cheshire.core :as json]
            [compojure-crud.config :as cfg]
            [compojure-crud.responses :as ress]
            [compojure-crud.validators :as v]
            [jnorlib-db.core :as db]))

(def get-token
  (GET "/token"
    []
    (json/encode {:token antif/*anti-forgery-token*})))

(def fetch-page
  (GET "/"
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

(defroutes app-routes
  fetch-page
  get-token
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (wrap-defaults site-defaults)

      ;; Modify origin as desired.
      (cors/wrap-cors :access-control-allow-origin #"http://localhost:8080"
                      :access-control-allow-methods [:get :put :post :delete])))
