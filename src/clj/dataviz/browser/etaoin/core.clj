(ns dataviz.browser.etaoin.core
  (:require [dataviz.config :refer [env]]
            [etaoin.keys :as k]
            [etaoin.api :refer :all]
            [clojure.tools.logging :as log]
            [clojure.string :as str]
            [clojure.java.io :as io]
            [selmer.parser :refer [render-file render]]))

(defn pdf? [file]
  (-> file
      .getAbsolutePath
      (str/ends-with? ".pdf")))

(defn svg? [file]
  (-> file
      .getAbsolutePath
      (str/ends-with? ".svg")))

(defn html? [file]
  (-> file
      .getAbsolutePath
      (str/ends-with? ".html")))

(defn load-html-file
  "Load Html File in Chrome"
  [opts]
  (let [download-dir (get opts :download-dir)
        driver-opts (get opts :driver-opts)
        html-js-file (get opts :html-js-file)
        driver-wait-time (get opts :driver-wait-time)]
    (with-chrome driver-opts driver
      (with-wait driver-wait-time
        (execute {:driver driver
                  :method :post
                  :path [:session (:session @driver) "chromium/send_command"]
                  :data {:cmd "Page.setDownloadBehavior"
                         :params {:behavior "allow"
                                  :downloadPath download-dir}}})
        (go driver html-js-file)
        (headless? driver)))
    (let [files (file-seq (io/file download-dir))
          found-pdf (some pdf? files)
          found-html (some html? files)
          found-svg (some svg? files)]
      (cond found-svg
            (log/info format "*.svg file found in %s directory." download-dir)
            found-html
            (log/info format "*.html file found in %s directory." download-dir)
            found-pdf
            (log/info format "*.pdf file found in %s directory." download-dir)))))

(comment
  (def driver-opts)
  ;; let's perform a quick Wiki session
  (go driver "https://en.wikipedia.org/")
  (wait-visible driver [{:id :simpleSearch} {:tag :input :name :search}])

  ;; search for something
  (fill driver {:tag :input :name :search} "Clojure programming language")
  (fill driver {:tag :input :name :search} k/enter)
  (wait-visible driver {:class :mw-search-results})

  ;; I'm sure the first link is what I was looking for
  (click driver [{:class :mw-search-results} {:class :mw-search-result-heading} {:tag :a}])
  (wait-visible driver {:id :firstHeading})

  ;; let's ensure
  (get-url driver) ;; "https://en.wikipedia.org/wiki/Clojure"

  (get-title driver) ;; "Clojure - Wikipedia"

  (has-text? driver "Clojure") ;; true

  ;; navigate on history
  (back driver)
  (forward driver)
  (refresh driver)
  (get-title driver)

  ;; stops Firefox and HTTP server
  (quit driver)

  (def driver (chrome {:path-driver "e://app//eclipse//temp//chromedriver.exe" :headless true}))
  (def driver (chrome {:path-driver "e://app//eclipse//temp//chromedriver.exe"
                       :capabilities {:chromeOptions
                                      {:args ["--disable-gpu --ignore-certificate-errors --verbose"]}}
                       }))

  (def driver (chrome {:path-driver "e://app//eclipse//temp//chromedriver.exe"
                       :download-dir "e:\\app\\temp"
                       :capabilities {:chromeOptions
                                      {:args ["--disable-gpu --ignore-certificate-errors"]}}
                       }))

  (def driver (chrome {:path-driver "e://app//eclipse//temp//chromedriver.exe"
                       :download-dir "e:\\app\\temp"
                       :capabilities {:chromeOptions
                                      {:args ["--headless --disable-gpu --ignore-certificate-errors"]}}}))

  (def driver-opts {:path-driver "e://app//eclipse//temp//chromedriver.exe"
                    :download-dir "e:\\app\\temp"
                    :size [1920 780]
                    :capabilities {:chromeOptions
                                   {:args ["--disable-gpu --ignore-certificate-errors"]}}})

  (def driver (chrome {:path-driver "e://app//eclipse//temp//chromedriver.exe"
                       :headless true}))

  (go driver "http://localhost:3000")
  (click driver [{:tag :li :index 2} {:tag :a}])
  (click driver [{:tag :li :index 1} {:tag :a}])
  (wait driver 7)
  (click-visible driver [{:tag :li :index 1} {:tag :a}])
  (has-text? driver "List Report")
  (has-text? driver "Table Report")
  (quit driver)


  (with-chrome driver-opts driver
    (with-wait 3
      (go driver "http://localhost:3000")
      (click driver [{:tag :li :index 2} {:tag :a}])
      (click driver [{:tag :li :index 1} {:tag :a}])
      (has-text? driver "List Report")))

  (with-chrome nil driver
    (go driver "http://localhost:3000"))

  (def download-directory "e:\\app\\temp")

  (def el (query driver [{:tag :li :index 2} {:tag :a}]))


  (go driver "http://localhost:3000/list")
  (go driver "http://localhost:3000/table")
  (quit driver)

  (def driver-opts {:path-driver "e://app//eclipse//temp//chromedriver.exe"
                    :download-dir "e:\\app\\temp"
                    :capabilities {:chromeOptions
                                   {:args ["--disable-gpu --ignore-certificate-errors"]}}
                    })

  (let [files (file-seq (io/file download-directory))
        found (some pdf? files)]
    (is found (format "No *.pdf file found in %s directory." download-directory)))

  (let [download-directory "e:\\app\\temp"
        driver-opts {:path-driver "e://app//eclipse//temp//chromedriver.exe"
                     :download-dir "e:\\app\\temp"
                     :capabilities {:chromeOptions
                                    {:args ["--disable-gpu --ignore-certificate-errors"]}}
                     }]
    (with-chrome-headless driver-opts driver
      (with-wait 3
        (execute {:driver driver
                  :method :post
                  :path [:session (:session @driver) "chromium/send_command"]
                  :data {:cmd "Page.setDownloadBehavior"
                         :params {:behavior "allow"
                                  :downloadPath download-directory}}})
        (go driver "http://localhost:3000/")
        (screenshot driver "e:\\app\\temp\\screen.png")
        (go driver "http://localhost:3000/list")
        (headless? driver))))

  (let [download-directory "e:\\app\\temp"
        driver-opts {:path-driver "e://app//eclipse//temp//chromedriver.exe"
                     :download-dir "e:\\app\\temp"
                     :capabilities {:chromeOptions
                                    {:args ["--disable-gpu --ignore-certificate-errors"]}}
                     }]
    (with-chrome driver-opts driver
      (with-wait 3
        (execute {:driver driver
                  :method :post
                  :path [:session (:session @driver) "chromium/send_command"]
                  :data {:cmd "Page.setDownloadBehavior"
                         :params {:behavior "allow"
                                  :downloadPath download-directory}}})
        (go driver (str "file://e:\\app\\eclipse\\projects\\chrome-reports-1\\" "index.html"))
        (screenshot driver "e:\\app\\temp\\screen.png")
        (click driver {:id "download"})
        ;; (go driver "http://localhost:3000/list")
        (headless? driver))))

  (let [download-directory "e:\\app\\temp"
        driver-opts {:path-driver "e://app//eclipse//temp//chromedriver.exe"
                     :download-dir "e:\\app\\temp"
                     :capabilities {:chromeOptions
                                    {:args ["--disable-gpu --ignore-certificate-errors"]}}
                     }]
    (with-chrome driver-opts driver
      (with-wait 3
        (go driver "http://dusd1devrhap042/public/welcome")
        (click driver {:tag :button :fn/text "Sign In"})
        (headless? driver))))



  (go driver "http://dusd1devrhap042/public/welcome")
  (click driver {:tag :button :fn/text "Sign In"})
  (def input-text-elements (query-all driver {:tag :input :type :text}))
  (def input-password-elements (query-all driver {:tag :input :type :password}))
  (fill-el driver (first input-text-elements) "arajwani@cls-bank.com")
  (fill-el driver (first input-password-elements) "Test$1234")
  (click driver {:tag :input :value "Sign In"})

  (click-el driver (nth (query-all driver {:tag :button}) 0))
  (click-el driver (nth (query-all driver {:tag :button}) 1))
  (back driver)


  (click driver {:tag :button :index 1})
  (nth (query-all driver {:tag :button}) 0)
  (click driver {:tag :div :class "app-selection-menu-button"})

  (query driver {:tag :button})


  (fill-el driver ((next elements) "Test$1234"))


  (click driver :download)
  (click driver {:tag :button :fn/text "Download PDF"})
  (query driver {:tag :button :fn/text "Download PDF"})


  (execute {:driver driver
            :method :post
            :path [:session (:session @driver) "chromium/send_command"]
            :data {:cmd  "Page.setDownloadBehavior"
                   :params {:behavior     "allow"
                            :downloadPath download-directory}}
            })

  (screenshot driver "page.png")
  (js-execute driver "alert(1)")
  (screenshot driver (java.io.File. "test-native.png"))

  (let [download-dir "/Users/shyam/temp"
        driver-opts {:path-driver "/Users/shyam/projects/chrome-driver/chromedriver"
                     ;; :download-dir "/Users/shyam/temp"
                     :headless true
                     :log-level :debug
                     :capabilities {:chromeOptions
                                    {:args ["--disable-gpu --ignore-certificate-errors"]}}
                     }]
    (with-chrome driver-opts driver
      (with-wait 1
        (execute {:driver driver
                  :method :post
                  :path [:session (:session @driver) "chromium/send_command"]
                  :data {:cmd "Page.setDownloadBehavior"
                         :params {:behavior "allow"
                                  :downloadPath download-dir}}})
        (go driver "https://en.wikipedia.org/")
        (wait-visible driver [{:id :simpleSearch} {:tag :input :name :search}])
        ;; search for something
        (fill driver {:tag :input :name :search} "Clojure programming language")
        (fill driver {:tag :input :name :search} k/enter)
        (wait-visible driver {:class :mw-search-results})
        ;; I'm sure the first link is what I was looking for
        (click driver [{:class :mw-search-results} {:class :mw-search-result-heading} {:tag :a}])
        (wait-visible driver {:id :firstHeading})
        ;; let's ensure
        (get-url driver)   ;; "https://en.wikipedia.org/wiki/Clojure"
        (get-title driver) ;; "Clojure - Wikipedia"
        (println (has-text? driver "Clojure")) ;; true
        (println (headless? driver)))))

  (let [download-dir (get-in env [:chrome :download-dir])
        driver-opts (get-in env [:chrome :driver-opts])]
    (with-chrome driver-opts driver
      (with-wait 1
        (execute {:driver driver
                  :method :post
                  :path [:session (:session @driver) "chromium/send_command"]
                  :data {:cmd "Page.setDownloadBehavior"
                         :params {:behavior "allow"
                                  :downloadPath download-dir}}})
        (go driver "https://en.wikipedia.org/")
        (wait-visible driver [{:id :simpleSearch} {:tag :input :name :search}])
        ;; search for something
        (fill driver {:tag :input :name :search} "Clojure programming language")
        (fill driver {:tag :input :name :search} k/enter)
        (wait-visible driver {:class :mw-search-results})
        ;; I'm sure the first link is what I was looking for
        (click driver [{:class :mw-search-results} {:class :mw-search-result-heading} {:tag :a}])
        (wait-visible driver {:id :firstHeading})
        ;; let's ensure
        (get-url driver)   ;; "https://en.wikipedia.org/wiki/Clojure"
        (get-title driver) ;; "Clojure - Wikipedia"
        (println (has-text? driver "Clojure")) ;; true
        (println (headless? driver)))))



  (let [chrome-options (get env :chrome)
        download-dir (select-keys chrome-options [:download-dir])
        driver-opts (select-keys chrome-options [:driver-opts])
        html-js-file (select-keys env [:html-js-file])]
    (load-html-file (merge download-dir driver-opts html-js-file)))


  )
