(ns donut.bakery.backend.endpoint-routes
  (:require
   [donut.bakery.backend.endpoint.dbxray-endpoint :as dbxray]
   [donut.bakery.backend.endpoint.endpoint-routes-endpoint :as endpoint-routes]
   [ring.util.response :as resp]))

(def routes
  [["/api/donut/bakery/endpoint-routes"
    {:name     :donut.bakery/endpoint-routes
     :ent-type :endpoint-routes
     :id-key   :endpoint-routes/id}
    endpoint-routes/collection-handlers]

   ["/api/donut/bakery/dbxray"
    {:name     :donut.bakery/dbxray
     :ent-type :dbxray
     :id-key   :dbxray/id}
    dbxray/collection-handlers]

   ["/donut/bakery*"
    {:description "renders bakery's index.html"}
    {:get {:handler (fn [_]
                      (-> (resp/resource-response "donut/bakery/index.html"
                                                  {:root "public"})
                          (resp/content-type "text/html")
                          (resp/status 200)))}}]])
