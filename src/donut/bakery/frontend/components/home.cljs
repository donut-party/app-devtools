(ns donut.bakery.frontend.components.home
  (:require
   [donut.bakery.frontend.ui :as ui]
   [re-frame.core :as rf]))

(defn component
  []
  [:div
   [:div
    [ui/h1 "Endpoint Routes"]
    (into
     [:div {:class "p-4 bg-white mt-2 divide-y divide-slate-100"}]
     (map (fn [[path options]]
            [:div {:class "py-4 first:pt-0 last:pb-0"}
             [:div {:class "font-mono font-bold text-sm rounded p-1"} path]
             [ui/pppre options]])
          @(rf/subscribe [:donut.bakery/endpoint-routes])))]])
