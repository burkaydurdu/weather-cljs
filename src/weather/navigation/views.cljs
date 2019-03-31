(ns weather.navigation.views
  (:require
    [re-frame.core :refer [dispatch subscribe]]
    [weather.navigation.subs]))

(defn nav-bar 
  []
  [:nav.navbar 
    {:role "navigation" :aria-label "main navigation"}
    [:div.navbar-menu.is-active
     [:div.navbar-start
      [:a.navbar-item 
       {:href "/#/"}
       "Home"]
      [:a.navbar-item 
       {:href "/#/city"}
       "City"]
      [:a.navbar-item
       {:href "/#/citydays"}
       "City 0-5"]]]])

(defn navigation-panel
  [active-panel]
  (let [[panel panel-name] @active-panel]
    [:<>
      [nav-bar]
      [:div.container {:style {:padding "2rem"}}
        (when panel
          [panel])]]))

(defn main-panel []
  [navigation-panel (subscribe [:active-panel])])
