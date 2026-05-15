(ns compojure-crud.validators)

(defn get-int
  [^String s ^long default]
  (try
    (Long/parseLong s)
    (catch Exception _
      default)))
