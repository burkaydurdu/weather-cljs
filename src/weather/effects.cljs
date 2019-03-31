(ns weather.effects
  (:require
    [re-frame.core :refer [reg-fx]]
    [weather.util :as util]))


(reg-fx
  :change-history!
  (fn [search-history]
    (let [history (util/get-item "history")]
      (util/set-item! "history" (conj history search-history)))))