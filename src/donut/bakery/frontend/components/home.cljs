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
   [:div
    [ui/h1 "Backend Routes"]

    [:div {:class "p-4 bg-white mt-2"}
     [ui/table
      (into [:tbody]
            (map endpoint-row @(rf/subscribe [:endpoint-routes])))]]]])
