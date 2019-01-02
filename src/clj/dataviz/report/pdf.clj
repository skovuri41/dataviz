(ns dataviz.report.pdf
  (:require [clj-htmltopdf.core :as htp]
            [clojure.java.io :as io]
            [selmer.parser :refer [render-file render]]))

(defn html-to-pdf
  "Converts html to pdf"
  [in out]
  (htp/->pdf
   (io/input-stream in)
   out))

(comment
  (html-to-pdf "template.html" "output4.pdf")
  (spit "/Users/shyam/temp/r1.html" (render-file "template.html" {:svg (slurp "/Users/shyam/temp/test2.svg")}))
  (html-to-pdf "/Users/shyam/temp/r1.html" "tt.pdf")
  (html-to-pdf "test.html" "output3.pdf"))
