(ns compojure-crud.responses
  (:require [cheshire.core :as json]))

(defn bad-request
  []
  (json/encode "Bad request body"))

(defn internal-server-error!
  [ex]
  (.printStackTrace ex)
  (json/encode "Unexpected error"))

(defn ok
  []
  (json/encode "OK"))
