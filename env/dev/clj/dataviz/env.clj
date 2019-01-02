(ns dataviz.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [dataviz.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[dataviz started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[dataviz has shut down successfully]=-"))
   :middleware wrap-dev})
