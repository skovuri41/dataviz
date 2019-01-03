(ns dataviz.core
  (:require [dataviz.config :refer [env]]
            [dataviz.report.core :as report]
            [clojure.tools.cli :refer [parse-opts]]
            [clojure.tools.logging :as log]
            [cli-matic.core :refer [run-cmd]]
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

(defn load-in-browser
  "Load the html/js file in the browser"
  [opt]
  (let [chrome-options (get env :chrome)
        download-dir (select-keys chrome-options [:download-dir])
        driver-opts (select-keys chrome-options [:driver-opts])
        driver-wait-time (select-keys chrome-options [:driver-wait-time])
        html-js-file (select-keys env [:html-js-file])]
    (report/load-in-browser (merge download-dir driver-opts html-js-file driver-wait-time))))

(defn html->pdf
  "Convert the input Html to Pdf"
  [opt]
  (log/info "html->pdf")
  (report/html->pdf (select-keys env [:chrome :html-report-file :pdf-report-name])))

(def CONFIGURATION
  {:app {:command "dataviz"
         :description "A command-line util to load html in chrome and generate pdf etc"
         :version "0.0.1"}
   :commands [{:command "load-in-browser"
               :description ["Load Html/Js file in the browser"]
               ;; add dummy options for bug in cli-matic
               :opts        [{:option "a1" :short "a" :env "AA" :as "First addendum" :type :int :default 0}]
               :runs load-in-browser}
              {:command "html-to-pdf"
               :description ["Convert Html to Pdf"]
               ;; add dummy options for bug in cli-matic
               :opts        [{:option "b1" :short "b" :env "A" :as "test" :type :int :default 0}]
               :runs html->pdf}
              ]})

(defn -main [& args]
  (mount/start #'dataviz.config/env)
  (log/info "Starting Dataviz app")
  (start-app args)
  (run-cmd args CONFIGURATION))
