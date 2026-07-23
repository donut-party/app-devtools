(ns donut.devtools.frontend.frontend-routes
  (:require
   [donut.devtools.frontend.components.database :as db]
   [donut.devtools.frontend.components.endpoint-routes :as er]
   [donut.devtools.frontend.components.home :as h]
   [donut.devtools.frontend.components.system :as system]
   [donut.frontend.events :as dfe]
   [donut.frontend.sync.flow :as dsf]))

(def routes
  [["/donut/devtools"
    {:name       :donut.devtools/home
     :components {:main [h/component]}
     ::dfe/on    {:enter [[::dsf/get {::dsf/req {:route-name :donut.devtools/endpoint-routes}}]]}}]

   ["/donut/devtools/endpoint-routes"
    {:name       :donut.devtools/endpoint-routes
     :components {:main [er/component]}
     ::dfe/on    {:enter [[::dsf/get {::dsf/req {:route-name :donut.devtools/endpoint-routes}}]]}}]

   ["/donut/devtools/database"
    {:name       :donut.devtools/database
     :components {:main [db/component]}
     ::dfe/on    {:enter [[::dsf/get {::dsf/req {:route-name :donut.devtools/dbxray}}]]}}]

   ["/donut/devtools/system"
    {:name       :donut.devtools/system
     :components {:main [system/component]}
     ::dfe/on    {:enter [[::dsf/get {::dsf/req {:route-name :donut.devtools/system}}]]}}]])
