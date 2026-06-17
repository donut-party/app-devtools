(ns bakery.example.frontend.frontend-routes
  (:require
   [donut.bakery.frontend.frontend-routes :as bakery-routes]
   [donut.frontend.events :as dfe]
   [donut.frontend.routes :as dfr]
   [donut.frontend.sync.flow :as dsf]
   [bakery.example.frontend.components.home :as h]))

(dfr/defroutes routes
  [["/"
    {:name       :home
     :components {:main [h/component]}}]
   bakery-routes/routes])
