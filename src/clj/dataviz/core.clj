(ns dataviz.core
  (:require [dataviz.config :refer [env]]
            [dataviz.report.core :as report]
            [clojure.tools.cli :refer [parse-opts]]
            [clojure.tools.logging :as log]
            [mount.core :as mount])
  (:gen-class))

(def cli-options
  [["-p" "--port PORT" "Port number"
    :parse-fn #(Integer/parseInt %)]])

(defn stop-app []
  (doseq [component (:stopped (mount/stop))]
    (log/info component "stopped"))
  (shutdown-agents))

(defn start-app [args]
  (doseq [component (-> args
                        (parse-opts cli-options)
                        mount/start-with-args
                        :started)]
    (log/info component "started"))
  (.addShutdownHook (Runtime/getRuntime) (Thread. stop-app)))

(defn -main [& args]
  (mount/start #'dataviz.config/env)
  (log/info "Starting Dataviz app")
  ;; (start-app args)

  ;; (report/html-to-pdf (select-keys env [:chrome :html-report-file :pdf-report-name]))

  (let [chrome-options (get env :chrome)
        download-dir (select-keys chrome-options [:download-dir])
        driver-opts (select-keys chrome-options [:driver-opts])
        driver-wait-time (select-keys chrome-options [:driver-wait-time])
        html-js-file (select-keys env [:html-js-file])]
    (report/js-to-pdf (merge download-dir driver-opts html-js-file driver-wait-time))))
