(ns donut.devtools.backend.endpoint.endpoint-routes-endpoint
  (:require
   [clojure.walk :as walk]
   [donut.devtools.backend.endpoint.common :as endpoint-common]
   [donut.system :as ds]
   [reitit.core :as r]))


(defn format-route-data
  "Handle objects that don't lend themselves to transit transmission, like functions"
  [x]
  (if ((some-fn vector? map? seq? number? string? keyword?) x)
    x
    (str x)))

(def collection-handlers
  {:get
   {:handler
    (fn collection-get [{{:keys [system-store]} :dependencies}]
      {:status 200
       :body   [{:endpoint-routes/id     :all
                 :endpoint-routes/routes (walk/postwalk endpoint-common/format-system
                                                        (-> @system-store
                                                            (ds/instance [:routing :router])
                                                            r/routes))}]})}})
