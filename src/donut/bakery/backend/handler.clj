(ns donut.bakery.backend.handler
  (:require
   [reitit.ring :as rr]))

(defn wrap-dependencies
  [handler dependencies]
  (fn [req]
    (handler (merge req dependencies))))

(defn handler
  [{:keys [router middleware db routes]}]
  (-> router
      (rr/ring-handler)
      (wrap-dependencies {:db     db
                          :routes routes})
      middleware))
