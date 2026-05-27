(ns user
  (:require [cheshire.core :as json])
  (:require [jnorlib-db.core :as db])
  (:import [com.zaxxer.hikari HikariDataSource]))

(def ds (doto (HikariDataSource.)
          (.setJdbcUrl "jdbc:postgresql://localhost:5432/foo")
          (.setUsername (System/getenv "USER"))
          (.setPassword (System/getenv "PSQL_PASS"))))


(with-open [mng (db/manager ds)]
  (db/exec-query! mng "select * from baz" #{(keyword "quu x")}))

(json/encode {"name" "Alexis"})

(.concat "Hello" :key)