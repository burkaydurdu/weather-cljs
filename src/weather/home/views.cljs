(ns weather.home.views
  (:require [weather.home.subs]
            [cljs.core.match :refer-macros [match]]
            [cljs.core :as clj]
            [reagent.core :as reagent]
            [clojure.string :as string]
            [re-frame.core :refer [subscribe]]
            [weather.common.subs]))

(def not-empty? (complement empty?))

(defn history-filter [history-list filt]
  (match [(:success? filt) (:search filt)]
                      [nil ""] history-list
                      [(:or true false) ""] (filter #(= (:status %) (:success? filt)) history-list)
                      [nil (_ :guard #(not-empty? %))] (filter #(string/includes? (:name %) (:search filt)) history-list)
                      :else (filter #(and 
                                       (= (:status %) (:success? filt))
                                       (string/includes? (:name %) (:search filt))) history-list)))


(defn my-comp
  []
  (let [filt         (reagent/atom {:success? nil :search ""})
        history-list (subscribe [:search-history])
        is-active    (reagent/atom 1)]
    (fn []
      [:div.column.is-half
       [:nav.panel.is-mobile
        [:p.panel-heading "History"]
        
        [:div.panel-block
         [:p.control.has-icons-left
          [:input.input.is-small 
           {:placeholder "Search"
            :on-change   #(swap! filt assoc :search (-> % .-target .-value))}]
          [:span.icon.is-small.is-left
           [:i.fas.fa-search 
            {:aria-hidden "true"}]]]]
        
        [:p.panel-tabs
         [:a 
          {:on-click #(do 
                        (swap! filt assoc :success? nil :search "")
                        (reset! is-active 1))
           :class (when (= @is-active 1) 
                    "is-active")} 
          "All"]
         [:a 
          {:on-click #(do 
                        (swap! filt assoc :success? true)
                        (reset! is-active 2))
           :class (when (= @is-active 2) 
                    "is-active")} 
          "Success"]
         [:a 
          {:on-click #(do 
                        (swap! filt assoc :success? false)
                        (reset! is-active 3))
           :class (when (= @is-active 3)
                    "is-active")}
          "Error"]]

        (for [item (history-filter @history-list @filt)]
          ^{:key (str (random-uuid))}
          [:a.panel-block.is-active
           [:span.panel-icon
            [:i.fas.fa-book {:aria-hidden "true"}]] (:name item)])]])))

(defn home []
  (reagent/create-class
    {:component-did-mount #()
     :reagent-render      (fn []
                            [:div.columns.is-centered
                              [my-comp]])}))
