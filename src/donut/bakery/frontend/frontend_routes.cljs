(ns donut.bakery.frontend.frontend-routes
  (:require
   [donut.bakery.frontend.components.database :as db]
   [donut.bakery.frontend.components.endpoint-routes :as er]
   [donut.bakery.frontend.components.home :as h]
   [donut.bakery.frontend.components.system :as system]
   [donut.frontend.events :as dfe]
   [donut.frontend.sync.flow :as dsf]))

(def routes
  [["/donut/bakery"
    {:name       :home
     :components {:main [h/component]}
     ::dfe/on    {:enter [[::dsf/get {::dsf/req {:route-name :donut.bakery/endpoint-routes}}]]}}]

   ["/donut/bakery/endpoint-routes"
    {:name       :endpoint-routes
     :components {:main [er/component]}
     ::dfe/on    {:enter [[::dsf/get {::dsf/req {:route-name :donut.bakery/endpoint-routes}}]]}}]

   ["/donut/bakery/database"
    {:name       :database
     :components {:main [db/component]}
     ::dfe/on    {:enter [[::dsf/get {::dsf/req {:route-name :donut.bakery/dbxray}}]]}}]

   ["/donut/bakery/system"
    {:name       :system
     :components {:main [system/component]}
     ::dfe/on    {:enter [[::dsf/get {::dsf/req {:route-name :donut.bakery/system}}]]}}]])
