(ns dataviz.report.core
  (:require [dataviz.report.pdf :as pdf]
            [dataviz.browser.etaoin.core :as browser]
            [clojure.tools.logging :as log]))

;; (defn json-to-pdf
;;   "Generate pdf from the input data in json format"
;;   [opts])

(defn html->pdf
  "Generate pdf from the html"
  [opts]
  (let [in (get opts :html-report-file)
        report-name (get opts :pdf-report-name)]
    (log/info "Generating Pdf from Html Report")
    (pdf/html-to-pdf in report-name)))

(defn load-in-browser
  "Load html and js code in headless chrome"
  [opts]
  (log/info "Load html and js code in headless chrome")
  (browser/load-html-file opts))
