(ns donut.app-devtools.backend.endpoint.dbxray-endpoint
  (:require
   [donut.dbxray :as dbx]
   [donut.system :as ds]
   [next.jdbc :as jdbc]))

(def collection-handlers
  {:get
   {:handler
    (fn collection-get [{{:keys [system-store]} :dependencies}]
      {:status 200
       :body   [{:dbxray/id   :all
                 :dbxray/xray (-> @system-store
                                  (ds/instance [:db :datasource])
                                  jdbc/get-connection
                                  (dbx/xray {:include-raw? true}))}]})}})
