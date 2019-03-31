(ns weather.common.subs
  (:require [re-frame.core :refer [reg-sub]]))


(reg-sub
  :search-history
  (fn [db _]
    (:search-history db)))
