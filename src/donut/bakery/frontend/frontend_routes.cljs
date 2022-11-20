(ns donut.bakery.frontend.frontend-routes
  (:require
   [donut.frontend.sync.flow :as dsf]
   [donut.bakery.frontend.components.home :as h]))

(def routes
  [["/donut/bakery"
    {:name       :home
     :components {:main [h/component]}
     :lifecycle  {:enter [[::dsf/get :donut.bakery/endpoint-routes]]}}]

   ["/donut/bakery/endpoint-routes"
    {:name       :endpoint-routes
     :components {:main [h/component]}
     :lifecycle  {:enter [[::dsf/get :donut.bakery/endpoint-routes]]}}]

   ["/donut/bakery/database"
    {:name       :database
     :components {:main [h/component]}
     :lifecycle  {:enter [[::dsf/get :donut.bakery/endpoint-routes]]}}]])
