(ns weather.city.event
  (:require [re-frame.core :refer [reg-event-fx]]
            [weather.effects]
            [weather.util :refer [create-request-map api-key]]))


(reg-event-fx
  :get-city-weather
  (fn [{:keys [_]} [_ city]]
    (let [uri    "/data/2.5/weather"
          params {:params {:q      city
                           :APIKEY api-key
                           :units  "metric"}}]
      {:http-xhrio (merge (create-request-map :get
                                              uri
                                              [:load-city-weather-result-ok city]
                                              [:dummy-http-on-fail city]) params)})))


(reg-event-fx
  :load-city-weather-result-ok
  (fn [{:keys [db]} [_ city city-weather]]
    (let [data {:name city :status true :type "city"}]
      {:db              (update (assoc db :city-weather city-weather) :search-history conj data)
       :change-history! data})))

