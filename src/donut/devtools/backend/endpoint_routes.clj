(ns donut.devtools.backend.endpoint-routes
  (:require
   [donut.devtools.backend.endpoint.dbxray-endpoint :as dbxray]
   [donut.devtools.backend.endpoint.endpoint-routes-endpoint :as endpoint-routes]
   [donut.devtools.backend.endpoint.system-endpoint :as system]
   [ring.util.response :as resp]))

(def routes
  [["/api/donut/devtools/endpoint-routes"
    (merge
     {:name             :donut.devtools/endpoint-routes
      :ent-type         :endpoint-routes
      :id-key           :endpoint-routes/id
      :donut.system/doc "returns endpoint route definitions"}
     endpoint-routes/collection-handlers)]

   ["/api/donut/devtools/dbxray"
    (merge
     {:name             :donut.devtools/dbxray
      :ent-type         :dbxray
      :id-key           :dbxray/id
      :donut.system/doc "returns schema definitions"}
     dbxray/collection-handlers)]

   ["/api/donut/devtools/system"
    (merge
     {:name             :donut.devtools/system
      :ent-type         :system
      :id-key           :system/id
      :donut.system/doc "returns system definition"}
     system/collection-handlers)]

   ["/donut/devtools*"
    {:description "renders devtools's index.html"
     :get         {:handler (fn [_]
                              (prn "devtools")
                              (-> (resp/resource-response "donut/devtools/index.html"
                                                          {:root "public"})
                                  (resp/content-type "text/html")
                                  (resp/status 200)))}}]])
