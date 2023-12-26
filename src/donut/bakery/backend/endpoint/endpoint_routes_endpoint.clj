(ns donut.bakery.backend.endpoint.endpoint-routes-endpoint
  (:require
   [clojure.walk :as walk]
   [donut.system :as ds]
   [reitit.core :as r]))

(defn representable?
  [x]
  (or (vector? x)
      (map? x)
      (seq? x)
      (number? x)
      (string? x)
      (keyword? x)))

(def collection-handlers
  {:get
   {:handler
    (fn collection-get [{{:keys [system-store]} :dependencies}]
      {:status 200
       :body   [{:endpoint-routes/id     :all
                 :endpoint-routes/routes (walk/postwalk (fn [x]
                                                          (if (representable? x)
                                                            x
                                                            (str x)))
                                                        (r/routes (ds/instance @system-store [:routing :router])))}]})}})
