(ns compojure-crud.validators)

;;; JUSTIFICATION: `parse-long` is not used because it throws if the GET param
;;; is nil so this fn is used instead.
(defn get-int
  [^String s ^long default]
  (try
    (Long/parseLong s)
    (catch Exception _
      default)))
