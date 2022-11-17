(ns donut.bakery.backend.endpoint.endpoint-routes-endpoint
  (:require
   [clojure.walk :as walk]))

(def collection-handlers
  {:get
   {:handler
    (fn collection-get [{:keys [routes]}]
      {:status 200
       :body   [{:endpoint-routes/id     :all
                 :endpoint-routes/routes (mapv (fn [[path opts]]
                                                 [path (walk/postwalk (fn [x]
                                                                        (if (fn? x)
                                                                          "fn"
                                                                          x))
                                                                      opts)])
                                               routes)}]})}})
