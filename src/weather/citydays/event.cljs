(ns weather.citydays.event
  (:require [re-frame.core :refer [reg-event-fx]]
            [weather.util :refer [create-request-map api-key]]))


(reg-event-fx
  :get-city-days-weather
  (fn [{:keys [db]} [_ city]]
    (let [uri    "/data/2.5/forecast"
          params {:params {:q      city
                           :APIKEY api-key
                           :units "metric"}}]
      {:http-xhrio (merge (create-request-map :get 
                                              uri 
                                              [:load-city-days-weather-result-ok city]
                                              [:dummy-http-on-fail city]) params)})))


(reg-event-fx
  :load-city-days-weather-result-ok
  (fn [{:keys [db]} [_ city city-days-weather]]
    (let [data {:name city :status true}]
      {:db              (update (assoc db :city-days-weather city-days-weather) :search-history conj data)
       :change-history! data})))

