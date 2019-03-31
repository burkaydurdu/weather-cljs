(ns weather.citydays.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :city-days-weather
  (fn [db _]
    (:city-days-weather db)))
