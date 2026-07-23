(ns donut.app-devtools.backend.endpoint-routes
  (:require
   [donut.app-devtools.backend.endpoint.dbxray-endpoint :as dbxray]
   [donut.app-devtools.backend.endpoint.endpoint-routes-endpoint :as endpoint-routes]
   [donut.app-devtools.backend.endpoint.system-endpoint :as system]
   [ring.util.response :as resp]))

(def routes
  [["/api/donut/app-devtools/endpoint-routes"
    (merge
     {:name             :donut.app-devtools/endpoint-routes
      :ent-type         :endpoint-routes
      :id-key           :endpoint-routes/id
      :donut.system/doc "returns endpoint route definitions"}
     endpoint-routes/collection-handlers)]

   ["/api/donut/app-devtools/dbxray"
    (merge
     {:name             :donut.app-devtools/dbxray
      :ent-type         :dbxray
      :id-key           :dbxray/id
      :donut.system/doc "returns schema definitions"}
     dbxray/collection-handlers)]

   ["/api/donut/app-devtools/system"
    (merge
     {:name             :donut.app-devtools/system
      :ent-type         :system
      :id-key           :system/id
      :donut.system/doc "returns system definition"}
     system/collection-handlers)]

   ["/donut/app-devtools*"
    {:description "renders app-devtools's index.html"
     :get         {:handler (fn [_]
                              (prn "app-devtools")
                              (-> (resp/resource-response "donut/app-devtools/index.html"
                                                          {:root "public"})
                                  (resp/content-type "text/html")
                                  (resp/status 200)))}}]])
