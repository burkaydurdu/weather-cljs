(ns weather.db
  (:require [re-frame.core :refer [reg-cofx]]
            [weather.util :refer [get-item]]))


(def default-db {:name "Weather"})

(reg-cofx
  :search-history
  (fn [cofx _]
    (assoc cofx :search-history (get-item "history"))))