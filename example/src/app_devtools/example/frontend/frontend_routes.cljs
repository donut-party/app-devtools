(ns app-devtools.example.frontend.frontend-routes
  (:require
   [donut.app-devtools.frontend.frontend-routes :as app-devtools-routes]
   [donut.frontend.events :as dfe]
   [donut.frontend.routes :as dfr]
   [donut.frontend.sync.flow :as dsf]
   [app-devtools.example.frontend.components.home :as h]))

(dfr/defroutes routes
  [["/"
    {:name       :home
     :components {:main [h/component]}}]
   app-devtools-routes/routes])
