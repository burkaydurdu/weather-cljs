(ns weather.common.event
  (:require [re-frame.core :refer [reg-event-fx inject-cofx reg-event-db]]
            [weather.db :as db]))

(reg-event-fx
  :initialise-db
  [(inject-cofx :search-history)]
  (fn [{:keys [_ search-history]} _]
    {:db (assoc db/default-db :search-history search-history)}))


(reg-event-fx
  :dummy-http-on-fail
  (fn [{:keys [db]} [_ city]]
    (let [data {:name city :status false}]
      {:db              (update db :search-history conj data)
       :change-history! data})))
