(ns user
  (:require [dataviz.config :refer [env]]
            [clojure.spec.alpha :as s]
            [expound.alpha :as expound]
            [mount.core :as mount]
            [dataviz.core :refer [start-app]]
            [dataviz.db.core]
            [conman.core :as conman]
            [luminus-migrations.core :as migrations]))

(alter-var-root #'s/*explain-out* (constantly expound/printer))

(defn start []
  (mount/start #'dataviz.config/env))

;; (defn start []
;;   (mount/start-without #'dataviz.core/repl-server))

(defn stop []
  (mount/stop #'dataviz.config/env))

;; (defn stop []
;;   (mount/stop-except #'dataviz.core/repl-server))

(defn restart []
  (stop)
  (start))

(defn restart-db []
  (mount/stop #'dataviz.db.core/*db*)
  (mount/start #'dataviz.db.core/*db*)
  (binding [*ns* 'dataviz.db.core]
    (conman/bind-connection dataviz.db.core/*db* "sql/queries.sql")))

(defn reset-db []
  (migrations/migrate ["reset"] (select-keys env [:database-url])))

(defn migrate []
  (migrations/migrate ["migrate"] (select-keys env [:database-url])))

(defn rollback []
  (migrations/migrate ["rollback"] (select-keys env [:database-url])))

(defn create-migration [name]
  (migrations/create name (select-keys env [:database-url])))


