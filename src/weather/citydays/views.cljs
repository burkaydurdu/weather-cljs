(ns weather.citydays.views
  (:require [reagent.core :as reagent]
            [weather.citydays.subs]
            [re-frame.core :refer [dispatch subscribe]]
            [clojure.string :as str]
            [weather.util :refer [filter-group format-date]]))

(defn monitor-table [list]
  [:table.table.is-bordered
   {:style {:background-color "rgba(255,255,255,.5)"}}
   [:thead
    [:tr
     [:th
      [:abbr {:title "Hour"} "H"]]
     [:th
      [:abbr {:title "Humidity"} "Hum"]]
     [:th
      [:abbr {:title "Temp Max"} "Max"]]
     [:th
      [:abbr {:title "Temp Min"} "Min"]]
     [:th
      [:abbr {:title "Clouds"} "Clo"]]]]
   [:tbody
    (for [item list]
      ^{:key (:dt_txt item)}
      [:tr
       [:td (last (format-date(:dt_txt item)))]
       [:td (-> item :main :humidity)]
       [:td (str (Math/round (-> item :main :temp_max)) " °C")]
       [:td (str (Math/round (-> item :main :temp_min)) " °C")]
       [:td (-> item :clouds :all)]])]])


(defn card [weather city]
  [:div.card {:style {:margin-top       "10px"
                      :background-color "rgba(255,255,255,.5)"
                      :border-radius    "8px"
                      :box-shadow       "10px 5px 5px black"}}
   [:div.card-content
    [:div.media
     [:div.media-left
      [:figure.image.is-48x48
       [:img 
        {:src (str "http://openweathermap.org/img/w/" (-> weather first :weather first :icon) ".png")}]]
      [:p.title.is-3 (str (Math/round (-> weather first :main :temp)) " °C")]]
     
     [:div.media-content
      [:p.title.is-3 (:name city)]
      [:p.subtitle.is-6 (-> weather first :weather first :main)]
      [:p.subtitle.is-6 (-> weather first :dt_txt format-date first)]]]
    
    [:div.content
     [monitor-table weather]]]])


(defn citydays []
  (reagent/create-class
    {:component-did-mount #(dispatch [:get-city-days-weather "istanbul"])
     :reagent-render      (fn []
                            (let [weather @(subscribe [:city-days-weather])]
                              [:div.columns.is-centered.is-multiline
                               [:div.column.is-half
                                [:div.control.has-icons-left.has-icons-right
                                 [:input.input.is-large {:placeholder  "Search..."
                                                         :on-key-press (fn [e]
                                                                         (when (= 13 (.-charCode e))
                                                                           (dispatch [:get-city-days-weather 
                                                                                      (-> e .-target .-value)])))}]
                                 [:span.icon.is-medium.is-left
                                  [:i.fas.fa-search]]
                                 [:span.icon.is-medium.is-right
                                  [:i.fas.fa-check]]]]

                               [:div.columns.is-multiline
                                (for [item (filter-group (:list weather) :dt_txt)]
                                  ^{:key (first (format-date(:dt_txt (first item))))}
                                  [:div.column.is-4.is-flex
                                   (card item (-> weather :city))])]]))}))
