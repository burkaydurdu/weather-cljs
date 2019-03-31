(ns weather.city.views
  (:require [reagent.core :as reagent]
            [weather.city.subs]
            [re-frame.core :refer [dispatch subscribe]]))


(defn search-bar []
  [:div.control.has-icons-left.has-icons-right
   [:input.input.is-large {:placeholder  "Search..."
                           :on-key-press (fn [e]
                                           (when (= 13 (.-charCode e))
                                             (dispatch [:get-city-weather (-> e .-target .-value)])))}]
   [:span.icon.is-medium.is-left
    [:i.fas.fa-search]]
   [:span.icon.is-medium.is-right
    [:i.fas.fa-check]]])

(defn monitor-table [weather]
  [:table.table.is-bordered
   {:style {:background-color "rgba(255,255,255,.5)"}}
   [:thead
    [:tr
     [:th
      [:abbr {:title "Humidity"} "Hum"]]
     [:th
      [:abbr {:title "Temp Max"} "Max"]]
     [:th
      [:abbr {:title "Temp Min"} "Min"]]
     [:th
      [:abbr {:title "Clouds"} "Clo"]]]]
   [:tbody
    [:tr
     [:td (-> weather :main :humidity)]
     [:td (str (Math/round (-> weather :main :temp_max)) " °C")]
     [:td (str (Math/round (-> weather :main :temp_min)) " °C")]
     [:td (-> weather :clouds :all)]]]])

(defn city []
  (reagent/create-class
    {:component-did-mount #(dispatch [:get-city-weather "istanbul"])
     :reagent-render      (fn []
                            (let [weather @(subscribe [:city-weather])]
                              [:div.columns.is-centered
                               [:div.column.is-half
                                [search-bar]
                                [:div.card {:style {:margin-top       "10px"
                                                    :background-color "rgba(255,255,255,.5)"}}
                                 [:div.card-content
                                  [:div.media
                                   [:div.media-left
                                    [:figure.image.is-48x48
                                     [:img {:src (str "http://openweathermap.org/img/w/"
                                                      (-> weather :weather first :icon)
                                                      ".png")}]]
                                    [:p.title.is-3 (str (-> weather :main :temp) " °C")]]
                                   [:div.media-content
                                    [:p.title.is-3 (:name weather)]
                                    [:p.subtitle.is-6 (-> weather :weather first :main)]]]
                                  [:div.content
                                   [:p
                                    (-> weather :weather first :description)]
                                   [monitor-table weather]]]]]]))}))
