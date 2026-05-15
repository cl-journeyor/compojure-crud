(ns compojure-crud.responses
  (:require [cheshire.core :as json]))

(defn internal-server-error!
  [ex]
  (.printStackTrace ex)
  (json/encode {:status 500
                :body "Unexpected error"}))

(defn ok
  []
  (json/encode {:status 200
                :body "OK"}))
