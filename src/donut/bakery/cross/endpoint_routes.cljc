(ns donut.bakery.cross.endpoint-routes
  (:require [donut.routes :as dr]
            #?@(:clj
                [;; endpoint namespaces go here
                 [donut.bakery.backend.endpoint.endpoint-routes-endpoint :as endpoint-routes]])))

(def routes
  (dr/merge-route-opts
   [["/donut/bakery/endpoint-routes"
     {:name     :donut.bakery/endpoint-routes
      :ent-type :endpoint-routes
      :id-key   :endpoint-routes/id}
     #?(:clj endpoint-routes/collection-handlers)]]))
