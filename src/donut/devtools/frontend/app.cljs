(ns donut.devtools.frontend.app
  (:require
   [donut.devtools.frontend.handlers] ;; load handlers
   [donut.devtools.frontend.subs] ;; load subs
   [donut.frontend.nav.components :as dnc]
   [donut.frontend.nav.flow :as dnf]
   [re-frame.core :as rf]))

(defn nav-link
  [route-name text opts]
  [dnc/simple-route-link
   (merge {:route-name route-name
           :class      "px-3 py-2 bg-slate-800 hover:bg-slate-900 mb-1 block"}
          opts)
   text])

(defn app
  []
  [:div
   [:main {:class "flex-1"}

    ;; side nav
    [:div {:class "hidden md:fixed md:inset-y-0 md:flex md:w-64 md:flex-col"}
     [:div {:class "flex min-h-0 flex-1 flex-col bg-slate-600 text-white tracking-wide"}
      [:div {:class "px-7 py-4 font-bold bg-rose-600 border-b-4 border-slate-800 text-lg"}
       "Donut Devtools "
       [:img {:class "inline"
              :src "/donut/devtools/static/images/noun-donut-4079056-white.png"
              :width "32"}]]
      [:div {:class "p-4"}
       [nav-link :donut.devtools/endpoint-routes "endpoint routes"]
       [nav-link :donut.devtools/database "database"]
       [nav-link :donut.devtools/system "system"]]]]

    ;; main area
    [:div {:class "flex flex-1 flex-col md:pl-64"}
     [:div {:class "py-4 px-8"}
      @(rf/subscribe [::dnf/routed-component :main])]]]])
