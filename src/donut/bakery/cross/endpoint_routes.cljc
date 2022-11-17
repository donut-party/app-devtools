(ns donut.bakery.cross.endpoint-routes
  (:require [donut.routes :as dr]
            #?@(:clj
                [;; endpoint namespaces go here
                 [donut.bakery.backend.endpoint.example-entity-endpoint :as example-entity] 
[donut.bakery.backend.endpoint.endpoint_routes-endpoint :as endpoint_routes]])))

(def routes
  (dr/merge-route-opts
   [;; example
    ["/api/v1/example-entities"
     {:name     :example-entities
      :ent-type :example-entity
      :id-key   :example_entity/id}
     #?(:clj example-entity/collection-handlers)]
    ["/api/v1/entities/{example_entity/id}"
     {:name     :example-entity
      :ent-type :example-entity
      :id-key   :example_entity/id}
     #?(:clj example-entity/member-handlers)] 
["/api/v1/endpoint_routes"
    {:name     :endpoint_routes
     :ent-type :endpoint_route
     :id-key   :endpoint_route/id}
    #?(:clj endpoint_routes/collection-handlers)] 
["/api/v1/endpoint_routes/{endpoint_route/id}"
    {:name     :endpoint_route
     :ent-type :endpoint_route
     :id-key   :endpoint_route/id}
    #?(:clj endpoint_routes/member-handlers)]]))
