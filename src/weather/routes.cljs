(ns weather.routes
  (:require-macros [secretary.core :refer [defroute]])
  (:import goog.history.Html5History)
  (:require [secretary.core :as secretary]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            [re-frame.core :refer [dispatch]]
            [weather.common.event]
            [weather.home.event]
            [weather.city.event]
            [weather.citydays.event]
            [weather.navigation.events]
            [weather.home.views :refer [home]]
            [weather.city.views :refer [city]]
            [weather.citydays.views :refer [citydays]]))

(defn hook-browser-navigation! []
  (doto (Html5History.)
    (events/listen
      EventType/NAVIGATE
      (fn [event]
        (secretary/dispatch! (.-token event))))
    (.setEnabled true)))


(defn app-routes []
  (secretary/set-config! :prefix "#")


  (defroute "/" []
            (dispatch [:set-active-panel [home :home]]))


  (defroute "/city" []
            (dispatch [:set-active-panel [city :city]]))

  (defroute "/citydays" []
            (dispatch [:set-active-panel [citydays :citydays]]))

  (hook-browser-navigation!))