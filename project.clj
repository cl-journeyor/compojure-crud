(defproject compojure-crud "0.1.0-SNAPSHOT"
  :aot :all
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.12.3"]
                 [compojure "1.6.1"]
                 [ring/ring-defaults "0.3.2"]
                 [ring-cors/ring-cors "0.1.13"]
                 [cheshire/cheshire "6.2.0"]
                 [com.zaxxer/HikariCP "7.0.2"]
                 [org.postgresql/postgresql "42.7.11"]]
  :java-source-paths ["src/jnorlib_db"]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler compojure-crud.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]}})
