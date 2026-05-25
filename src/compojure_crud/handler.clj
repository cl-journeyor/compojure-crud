(ns compojure-crud.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.anti-forgery :as antif]
            [ring.middleware.cors :as cors]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.util.response :as resp]
            [cheshire.core :as json]
            [compojure-crud.config :as cfg]
            [compojure-crud.responses :as ress]
            [compojure-crud.validators :as v]
            [jnorlib-db.core :as db]))

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
    (do
      (println (:params request))
      (ress/ok)))) ; FIXME: Fix either at back or front.

(defroutes app-routes
  get-token
  fetch-page
  insert-employee
  (GET "/" [] (resp/redirect "/index.html"))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (wrap-defaults site-defaults)

      ;; Modify origin as desired.
      #_(cors/wrap-cors :access-control-allow-origin #"http://localhost:8080"
                      :access-control-allow-methods [:get :put :post :delete])))
