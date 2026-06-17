(ns donut.bakery.backend.endpoint-routes
  (:require
   [donut.bakery.backend.endpoint.dbxray-endpoint :as dbxray]
   [donut.bakery.backend.endpoint.endpoint-routes-endpoint :as endpoint-routes]
   [donut.bakery.backend.endpoint.system-endpoint :as system]
   [ring.util.response :as resp]))

(def routes
  [["/api/donut/bakery/endpoint-routes"
    (merge
     {:name             :donut.bakery/endpoint-routes
      :ent-type         :endpoint-routes
      :id-key           :endpoint-routes/id
      :donut.system/doc "returns endpoint route definitions"}
     endpoint-routes/collection-handlers)]

   ["/api/donut/bakery/dbxray"
    (merge
     {:name             :donut.bakery/dbxray
      :ent-type         :dbxray
      :id-key           :dbxray/id
      :donut.system/doc "returns schema definitions"}
     dbxray/collection-handlers)]

   ["/api/donut/bakery/system"
    (merge
     {:name             :donut.bakery/system
      :ent-type         :system
      :id-key           :system/id
      :donut.system/doc "returns system definition"}
     system/collection-handlers)]

   ["/donut/bakery*"
    {:description "renders bakery's index.html"
     :get         {:handler (fn [_]
                              (prn "bakery")
                              (-> (resp/resource-response "donut/bakery/index.html"
                                                          {:root "public"})
                                  (resp/content-type "text/html")
                                  (resp/status 200)))}}]])
