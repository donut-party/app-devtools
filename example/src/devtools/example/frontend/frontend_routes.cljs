(ns devtools.example.frontend.frontend-routes
  (:require
   [donut.devtools.frontend.frontend-routes :as devtools-routes]
   [donut.frontend.events :as dfe]
   [donut.frontend.routes :as dfr]
   [donut.frontend.sync.flow :as dsf]
   [devtools.example.frontend.components.home :as h]))

(dfr/defroutes routes
  [["/"
    {:name       :home
     :components {:main [h/component]}}]
   devtools-routes/routes])
