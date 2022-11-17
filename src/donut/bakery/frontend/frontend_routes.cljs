(ns donut.bakery.frontend.frontend-routes
  (:require
   [donut.frontend.sync.flow :as dsf]
   [donut.bakery.frontend.components.home :as h]))

(def routes
  [["/"
    {:name       :home
     :components {:main [h/component]}
     :lifecycle  {:enter [[::dsf/get :donut.bakery/endpoint-routes]]}}]])
