(ns donut.bakery.frontend.components.database
  (:require
   [donut.bakery.frontend.ui :as ui]
   [re-frame.core :as rf]))

(defn table
  [table-name {:keys [columns column-order]}]
  [:div
   [:div {:class "font-mono font-bold"}
    table-name]
   [:table {:class "mt-1"}
    [:tbody
     [:<> (map (fn [column-name]
                 [:tr {:key column-name}
                  [:td {:class "p-3 font-mono italic text-sm border border-collapse border-slate-300 align-top"}
                   (name column-name)]
                  [:td {:class "border border-collapse border-slate-300"}
                   [ui/pppre (get-in columns [column-name :raw])]]])
               column-order)]]]])

(defn component
  []
  [:div
   [ui/h1 "Database"]
   (let [{:keys [table-order tables]}  @(rf/subscribe [:dbxray])]
     (into [:div {:class "p-4 bg-white mt-2 divide-y divide-slate-100"}]
           (map (fn [table-name]
                  [table table-name (table-name tables)])
                table-order)))])
