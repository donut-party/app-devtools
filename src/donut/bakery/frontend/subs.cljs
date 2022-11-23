(ns donut.bakery.frontend.subs
  (:require
   [donut.frontend.core.utils :as dcu]
   [re-frame.core :as rf]))

(rf/reg-sub :endpoint-routes
  (fn [db]
    (-> (dcu/entities db :endpoint-routes :endpoint-routes/id)
        first
        :endpoint-routes/routes)))

(rf/reg-sub :dbxray
  (fn [db]
    (-> (dcu/entities db :dbxray :dbxray/id)
        first
        :dbxray/xray)))
