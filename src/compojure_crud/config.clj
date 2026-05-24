(ns compojure-crud.config
  (:import [com.zaxxer.hikari HikariDataSource]))

(def ds (doto (HikariDataSource.)
          (.setJdbcUrl "jdbc:postgresql://localhost:5432/company")
          (.setUsername (System/getenv "USER"))
          (.setPassword (System/getenv "PSQL_PASS"))))
