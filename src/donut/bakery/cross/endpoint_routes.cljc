(ns donut.bakery.cross.endpoint-routes
  (:require [donut.routes :as dr]
            #?@(:clj
                [;; endpoint namespaces go here
                 [donut.bakery.backend.endpoint.endpoint-routes-endpoint :as endpoint-routes]
                 [ring.util.response :as resp]])))

(def routes
  (dr/merge-route-opts
   [["/api/donut/bakery/endpoint-routes"
     {:name     :donut.bakery/endpoint-routes
      :ent-type :endpoint-routes
      :id-key   :endpoint-routes/id}
     #?(:clj endpoint-routes/collection-handlers)]

    ["/donut/bakery*"
     {:description "renders bakery's index.html"}
     #?(:clj
        {:get {:handler (fn [_]
                          (-> (resp/resource-response "donut/bakery/index.html"
                                                      {:root "public"})
                              (resp/content-type "text/html")
                              (resp/status 200)))}})]]))
