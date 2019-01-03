(ns dataviz.report.core
  (:require [dataviz.config :refer [env]]
            [dataviz.report.pdf :as pdf]
            [dataviz.browser.etaoin.core :as browser]
            [clojure.tools.logging :as log]))

;; (defn json-to-pdf
;;   "Generate pdf from the input data in json format"
;;   [opts])

(defn html-to-pdf
  "Generate pdf from the html"
  [opts]
  (let [in (get opts :html-report-file)
        report-name (get opts :pdf-report-name)]
    (log/info "Generating Pdf from Html Report")
    (pdf/html-to-pdf in report-name)))

(defn js-to-pdf
  "Generate pdf by executing the js code in headless chrome"
  [opts]
  (log/info "Generating Pdf by loading Html JS file in Headless chrome")
  (browser/load-html-file opts))
