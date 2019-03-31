(ns weather.util
  (:require [ajax.core :as ajax]
            [clojure.string :as str]))


(def api-url "https://api.openweathermap.org")
(def api-key "ac8ae4fe7ac1b8ac593e50fcb4248761") ;; APIKEY=ac8ae4fe7ac1b8ac593e50fcb4248761
(defn set-item!
  [key val]
  (.setItem (.-localStorage js/window) key (.stringify js/JSON (clj->js val))))


(defn get-item
  [key]
  (js->clj (.parse js/JSON (.getItem (.-localStorage js/window) key)) :keywordize-keys true))


(defn remove-item!
  [key]
  (.removeItem (.-localStorage js/window) key))


(defn change-title!
  [title]
  (set! (.-title js/document) title))

(defn create-request-map
([type uri on-success]
  (create-request-map type uri on-success nil))
([type uri on-success on-fail]
  (cond-> {:method          type
           :uri             (str api-url uri)
           :format          (ajax/json-request-format)
           :response-format (ajax/json-response-format {:keywords? true})
           :on-success      (if (vector? on-success) on-success [on-success])
           :on-failure      (if (vector? on-fail) on-fail [on-fail])}
          (nil? on-fail) (assoc :on-failure [:dummy-http-on-fail]))))


;(defn filter-group [data key]
;  (loop
;    [x data
;     y []]
;    (if (empty? x)
;      y
;      (recur
;        (vec (filter (fn [z] (not= (key (first x)) (key z))) x))
;        (conj y (vec (filter (fn [z] (= (key (first x)) (key z))) x)))))))

(defn format-date [date]
  (let [data (str/split date #" ")]
    [(str/join "/" (reverse (str/split (first data) #"-")))
     (apply str (drop-last 3 (last data)))]))

(defn filter-group [data key]
  (loop
    [x data
     y []]
    (if (empty? x)
      y
      (recur
        (vec (filter (fn [z] (not= (first (format-date(key (first x)))) (first (format-date(key z))))) x))
        (conj y (vec (filter (fn [z] (= (first (format-date(key (first x)))) (first (format-date(key z))))) x)))))))

