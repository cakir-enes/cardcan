(defproject plzwork "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.597"
                  :exclusions [com.google.javascript/closure-compiler-unshaded
                               org.clojure/google-closure-library]]
                 [thheller/shadow-cljs "2.8.73"]
                 [reagent "0.8.1"]
                 [re-frame "0.10.9"]
                 [compojure "1.6.1"]
                 [yogthos/config "1.1.6"]
                 [ring "1.7.1"]]

  :plugins [
            [lein-shell "0.5.0"]]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj" "src/cljs"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]


  :shell {:commands {"open" {:windows ["cmd" "/c" "start"]
                             :macosx  "open"
                             :linux   "xdg-open"}}}

  :aliases {"dev"          ["with-profile" "dev" "do"
                            ["clean"]
                            ["run" "-m" "shadow.cljs.devtools.cli" "watch" "app"]]
            "prod"         ["with-profile" "prod" "do"
                            ["clean"]
                            ["run" "-m" "shadow.cljs.devtools.cli" "release" "app"]]
            "build-report" ["with-profile" "prod" "do"
                            ["clean"]
                            ["run" "-m" "shadow.cljs.devtools.cli" "run" "shadow.cljs.build-report" "app" "target/build-report.html"]
                            ["shell" "open" "target/build-report.html"]]
            "karma"        ["with-profile" "prod" "do"
                            ["clean"]
                            ["run" "-m" "shadow.cljs.devtools.cli" "compile" "karma-test"]
                          ["shell" "karma" "start" "--single-run" "--reporters" "junit,dots"]]}

  :profiles
  {:dev
   {:dependencies [[binaryage/devtools "0.9.11"]
                   [day8.re-frame/re-frame-10x "0.4.5"]
                   [day8.re-frame/tracing "0.5.3"]
                   [io.replikativ/konserve "0.5.1"]]}

   :prod {:dependencies [[day8.re-frame/tracing-stubs "0.5.3"]]}

   :uberjar {:source-paths ["env/prod/clj"]
             :dependencies [[day8.re-frame/tracing-stubs "0.5.3"]]
             :omit-source  true
             :main         plzwork.server
             :aot          [plzwork.server]
             :uberjar-name "plzwork.jar"
             :prep-tasks   ["compile" ["prod"]]}})
