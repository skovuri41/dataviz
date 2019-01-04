(defproject dataviz "0.1.0-SNAPSHOT"

  :description "FIXME: write description"
  :url "http://example.com/FIXME"

  :dependencies [[cheshire "5.8.1"]
                 [clojure.java-time "0.3.2"]
                 [cprop "0.1.13"]
                 [funcool/struct "1.3.0"]
                 [markdown-clj "1.0.5"]
                 [mount "0.1.15"]
                 [nrepl "0.5.3"]
                 [me.raynes/fs "1.4.6"]
                 [funcool/cuerdas "2.0.6"]
                 [etaoin "0.2.9"]
                 [ch.qos.logback/logback-classic "1.2.3"]
                 [org.clojure/clojure "1.10.0"]
                 [org.clojure/core.cache "0.7.1"]
                 [org.clojure/tools.cli "0.4.1"]
                 [org.clojure/tools.logging "0.4.1"]
                 [selmer "1.12.5"]
                 [cli-matic "0.3.3"]
                 [com.openhtmltopdf/openhtmltopdf-core "0.0.1-RC17"]
                 [com.openhtmltopdf/openhtmltopdf-pdfbox "0.0.1-RC17"]
                 [com.openhtmltopdf/openhtmltopdf-rtl-support "0.0.1-RC17"]
                 [com.openhtmltopdf/openhtmltopdf-svg-support "0.0.1-RC17"]
                 [metasoarous/oz "1.4.1"]
                 ;; [org.jsoup/jsoup "1.11.3"]
                 ;; [commons-io/commons-io "2.6"]
                 [clj-htmltopdf "0.1-alpha6"]]

  :min-lein-version "2.0.0"
  
  :source-paths ["src/clj"]
  :test-paths ["test/clj"]
  :resource-paths ["resources"]
  :target-path "target/%s/"
  :main ^:skip-aot dataviz.core

  :plugins []

  :profiles
  {:uberjar {:omit-source true
             :aot :all
             :uberjar-name "dataviz.jar"
             :source-paths ["env/prod/clj"]
             :resource-paths ["env/prod/resources"]}

   :dev           [:project/dev :profiles/dev]
   :test          [:project/dev :project/test :profiles/test]

   :project/dev  {:jvm-opts ["-Dconf=dev-config.edn"]
                  :dependencies [[expound "0.7.2"]
                                 [pjstadig/humane-test-output "0.9.0"]
                                 [prone "1.6.1"]
                                 ]
                  :plugins      [[com.jakemccrary/lein-test-refresh "0.23.0"]
                                 [lein-binplus "0.6.4"]]
                  :source-paths ["env/dev/clj"]
                  :resource-paths ["env/dev/resources"]
                  :repl-options {:init-ns user}
                  :injections [(require 'pjstadig.humane-test-output)
                               (pjstadig.humane-test-output/activate!)]}
   :project/test {:jvm-opts ["-Dconf=test-config.edn"]
                  :resource-paths ["env/test/resources"]}
   :profiles/dev {}
   :profiles/test {}})
