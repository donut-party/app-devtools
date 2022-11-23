(ns donut.bakery.frontend.components.database
  (:require
   [donut.bakery.frontend.ui :as ui]
   [re-frame.core :as rf]))

(defn table
  [table-name table-xray]
  [:div
   [:div {:class "font-mono font-bold"}
    table-name]
   [ui/pppre table-xray]])

(defn component
  []
  [:div
   [ui/h1 "Database"]
   (let [{:keys [table-order tables]}  @(rf/subscribe [:dbxray])]
     (into [:div {:class "p-4 bg-white mt-2 divide-y divide-slate-100"}]
           (map (fn [table-name]
                  [table table-name (table-name tables)])
                table-order)))])
