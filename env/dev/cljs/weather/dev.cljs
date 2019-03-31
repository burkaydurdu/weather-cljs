(ns ^:figwheel-no-load weather.dev
  (:require
    [weather.core :as core]
    [devtools.core :as devtools]))


(enable-console-print!)

(devtools/install!)

(core/init!)
