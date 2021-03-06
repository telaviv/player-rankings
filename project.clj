(defproject player-rankings "0.1.0-SNAPSHOT"

  :description "FIXME: write description"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [selmer "0.8.2"]
                 [com.taoensso/timbre "4.1.1"]
                 [com.taoensso/tower "3.0.2"]
                 [markdown-clj "0.9.66"]
                 [environ "1.0.0"]
                 [im.chit/cronj "1.4.3"]
                 [compojure "1.3.3"]
                 [ring/ring-defaults "0.1.4"]
                 [ring/ring-session-timeout "0.1.0"]
                 [ring/ring-json "0.4.0"]
                 [ring-middleware-format "0.5.0"]
                 [bouncer "0.3.2"]
                 [prone "0.8.1"]
                 [org.clojure/tools.nrepl "0.2.12"]
                 [ring-server "0.4.0"]
                 [clj-http "3.0.0-SNAPSHOT"]
                 [org.clojure/data.json "0.2.6"]
                 [clojurewerkz/neocons "3.1.0-beta3"]
                 [clj-time "0.9.0"]
                 [com.taoensso/timbre "3.4.0"]
                 [spyscope "0.1.5"]
                 [prismatic/schema "1.0.1"]
                 [com.cemerick/url "0.1.1"]
                 [enlive "1.1.6"]
                 ]

  :min-lein-version "2.0.0"
  :uberjar-name "player-rankings.jar"
  :jvm-opts ["-server"]
  :source-paths ["src/clj"]

;;enable to start the nREPL server when the application launches
;:env {:repl-port 7001}

  :main player-rankings.core

  :plugins [[lein-ring "0.9.1"]
            [lein-environ "1.0.0"]
            [lein-ancient "0.6.5"]
            [cider/cider-nrepl "0.11.0-SNAPSHOT"]
            ]


  :ring {:handler player-rankings.handler/app
         :init    player-rankings.handler/init
         :destroy player-rankings.handler/destroy
         :uberwar-name "player-rankings.war"}




  :profiles
  {:uberjar {:omit-source true
             :env {:production true}

             :aot :all}
   :dev {:dependencies [[ring-mock "0.1.5"]
                        [ring/ring-devel "1.3.2"]
                        [pjstadig/humane-test-output "0.7.0"]
                        [org.clojure/tools.nrepl "0.2.12"]
                        ]
         :source-paths ["env/dev/clj"]
         :java-source-paths ["src/java"]



         :repl-options {:init-ns player-rankings.core}
         :injections [(require 'pjstadig.humane-test-output)
                      (pjstadig.humane-test-output/activate!)
                      (require 'spyscope.core)]
         :env {:dev true}}})
