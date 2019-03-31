(defproject weather "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/clojurescript "1.10.520"]
                 [re-frame "0.10.5"]
                 [cljs-ajax "0.8.0"]
                 [secretary "1.2.3"]
                 [day8.re-frame/http-fx "0.1.6"]
                 [org.clojure/core.match "0.3.0-alpha5"]
                 [reagent "0.8.1"]]

  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-figwheel "0.5.18"]]

  :clean-targets ^{:protect false}
[:target-path
 [:cljsbuild :builds :app :compiler :output-dir]
 [:cljsbuild :builds :app :compiler :output-to]]

  :resource-paths ["public"]

  :figwheel {:http-server-root "."
             :nrepl-port       7002
             :nrepl-middleware [cider.piggieback/wrap-cljs-repl]
             :css-dirs         ["public/css"]}

  :cljsbuild {:builds {:app
                       {:source-paths ["src" "env/dev/cljs"]
                        :compiler
                                      {:main            "weather.dev"
                                       :output-to       "public/js/app.js"
                                       :output-dir      "public/js/out"
                                       :asset-path      "js/out"
                                       :source-map      true
                                       :optimizations   :none
                                       :closure-defines {"re_frame.trace.trace_enabled_QMARK_" true}
                                       :preloads        [day8.re-frame-10x.preload]
                                       :pretty-print    true}
                        :figwheel
                                      {:on-jsload "weather.core/mount-root"
                                       :open-urls ["http://localhost:3449/index.html"]}}
                       :release
                       {:source-paths ["src" "env/prod/cljs"]
                        :compiler
                                      {:output-to     "public/js/app.js"
                                       :output-dir    "public/js/release"
                                       :optimizations :advanced
                                       :infer-externs true
                                       :pretty-print  false}}}}

  :aliases {"package" ["do" "clean" ["cljsbuild" "once" "release"]]}

  :profiles {:dev {:source-paths ["src" "env/dev/clj"]
                   :dependencies [[binaryage/devtools "0.9.10"]
                                  [figwheel-sidecar "0.5.18"]
                                  [nrepl "0.6.0"]
                                  [day8.re-frame/re-frame-10x "0.3.7"]
                                  [day8.re-frame/tracing "0.5.1"]
                                  [cider/piggieback "0.4.0"]]}})
