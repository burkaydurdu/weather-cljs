(ns weather.core
    (:require
      [reagent.core :as r]
      [re-frame.core :as re-frame]
      [weather.routes :refer [app-routes]]
      [weather.navigation.views :as views]
      [day8.re-frame.http-fx]))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (r/render [views/main-panel] (.getElementById js/document "app")))

(defn init! []
  (app-routes)
  (re-frame/dispatch-sync [:initialise-db])
  (mount-root))

