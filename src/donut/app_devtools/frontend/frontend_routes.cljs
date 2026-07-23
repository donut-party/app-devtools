(ns donut.app-devtools.frontend.frontend-routes
  (:require
   [donut.app-devtools.frontend.components.database :as db]
   [donut.app-devtools.frontend.components.endpoint-routes :as er]
   [donut.app-devtools.frontend.components.home :as h]
   [donut.app-devtools.frontend.components.system :as system]
   [donut.frontend.events :as dfe]
   [donut.frontend.sync.flow :as dsf]))

(def routes
  [["/donut/app-devtools"
    {:name       :donut.app-devtools/home
     :components {:main [h/component]}
     ::dfe/on    {:enter [[::dsf/get {::dsf/req {:route-name :donut.app-devtools/endpoint-routes}}]]}}]

   ["/donut/app-devtools/endpoint-routes"
    {:name       :donut.app-devtools/endpoint-routes
     :components {:main [er/component]}
     ::dfe/on    {:enter [[::dsf/get {::dsf/req {:route-name :donut.app-devtools/endpoint-routes}}]]}}]

   ["/donut/app-devtools/database"
    {:name       :donut.app-devtools/database
     :components {:main [db/component]}
     ::dfe/on    {:enter [[::dsf/get {::dsf/req {:route-name :donut.app-devtools/dbxray}}]]}}]

   ["/donut/app-devtools/system"
    {:name       :donut.app-devtools/system
     :components {:main [system/component]}
     ::dfe/on    {:enter [[::dsf/get {::dsf/req {:route-name :donut.app-devtools/system}}]]}}]])
