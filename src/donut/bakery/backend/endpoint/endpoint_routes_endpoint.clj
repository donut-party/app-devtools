(ns donut.bakery.backend.endpoint.endpoint-routes-endpoint
  (:require
   [clojure.walk :as walk]
   [donut.system :as ds]))

(defn representable?
  [x]
  (or (vector? x)
      (map? x)
      (seq? x)
      (number? x)
      (string? x)))

(def collection-handlers
  {:get
   {:handler
    (fn collection-get [{{:keys [system-store]} :dependencies}]
      {:status 200
       :body   [{:endpoint-routes/id     :all
                 :endpoint-routes/routes (mapv (fn [[path opts]]
                                                 [path (walk/postwalk (fn [x]
                                                                        (if (representable? x)
                                                                          x
                                                                          (str x)))
                                                                      opts)])
                                               (ds/instance @system-store [:routing :routes]))}]})}})
