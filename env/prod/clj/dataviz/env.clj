(ns dataviz.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[dataviz started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[dataviz has shut down successfully]=-"))
   :middleware identity})
