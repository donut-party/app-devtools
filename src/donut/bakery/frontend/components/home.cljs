(ns donut.bakery.frontend.components.home
  (:require
   [donut.bakery.frontend.ui :as ui]
   [re-frame.core :as rf]))

(defn endpoint-row
  [route]
  [:tr [:td (str route)]])

(defn component
  []
  [:div
   [ui/h1 "Donut Bakery 🍩"]
   [:div
    [ui/h2 "Backend Routes"]
    [:table
     (into [:tbody]
           (map endpoint-row @(rf/subscribe [:endpoint-routes])))]]])
