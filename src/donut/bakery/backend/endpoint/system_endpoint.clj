(ns donut.bakery.backend.endpoint.system-endpoint
  (:require
   [donut.bakery.backend.endpoint.common :as endpoint-common]))

(def collection-handlers
  {:get
   {:handler
    (fn collection-get [{{:keys [system-store]} :dependencies}]
      {:status 200
       :body   [{:system/id     :system
                 :system/system (endpoint-common/format-system @system-store)}]})}})
