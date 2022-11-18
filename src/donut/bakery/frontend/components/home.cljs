(ns donut.bakery.frontend.components.home
  (:require
   [donut.bakery.frontend.ui :as ui]
   [re-frame.core :as rf]))

(defn endpoint-row
  [[path options]]
  [:tr
   [ui/td [ui/pppre path]]
   [ui/td [ui/pppre options]]])

(defn component
  []
  [:div
   [ui/h1 "Donut Bakery 🍩"]
   [:div
    [ui/h2 "Backend Routes"]
    [ui/table
     (into [:tbody]
           (map endpoint-row @(rf/subscribe [:endpoint-routes])))]]])
