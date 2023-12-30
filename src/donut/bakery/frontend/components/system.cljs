(ns donut.bakery.frontend.components.system
  (:require
   [donut.bakery.frontend.ui :as ui]
   [re-frame.core :as rf]))

(defn system-component-defs
  [component-defs]
  [:table {:class "border-collapse border-y min-w-full"}
   (into [:tbody]
         (mapv (fn [[component-name component-def]]
                 [:tr {:class "divide-x divide-y"}
                  [:td {:class "p-4 align-text-top text-sm"}
                   [:pre {:class "text-sm"} (str component-name)]]
                  [:td
                   [ui/pppre component-def]]])
               component-defs))])

(defn system-component-group
  [[group-name component-defs]]
  [:div {:class "bg-white mt-4 rounded-lg border-b-4"}
   [:div {:class "font-bold p-4 rounded-t-lg"}
    [:pre (str group-name)]]
   [system-component-defs component-defs]])

(defn component
  []
  [:div
   [ui/h1 "System"]
   (into
    [:div]
    (mapv system-component-group
          @(rf/subscribe [:system])))])
