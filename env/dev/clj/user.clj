(ns user
  (:require [dataviz.config :refer [env]]
            [clojure.spec.alpha :as s]
            [expound.alpha :as expound]
            [mount.core :as mount]
            [dataviz.core :refer [start-app]]
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
