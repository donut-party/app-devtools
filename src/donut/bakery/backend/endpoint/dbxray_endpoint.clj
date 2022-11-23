(ns donut.bakery.backend.endpoint.dbxray-endpoint
  (:require
   [donut.dbxray :as dbx]
   [next.jdbc :as jdbc]))

(def collection-handlers
  {:get
   {:handler
    (fn collection-get [{:keys [db]}]
      {:status 200
       :body   [{:dbxray/id   :all
                 :dbxray/xray (dbx/xray (jdbc/get-connection db))}]})}})
