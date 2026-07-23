(ns donut.app-devtools.backend.endpoint.system-endpoint
  (:require
   [donut.app-devtools.backend.endpoint.common :as endpoint-common]))

(def collection-handlers
  {:get
   {:handler
    (fn collection-get [{{:keys [system-store]} :dependencies}]
      {:status 200
       :body   [{:system/id     :system
                 :system/system (endpoint-common/format-system @system-store)}]})}})
