(ns weather.city.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :city-weather
  (fn [db _]
    (:city-weather db)))
