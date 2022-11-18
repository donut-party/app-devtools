(ns donut.bakery.frontend.subs
  (:require
   [donut.frontend.core.utils :as dcu]
   [donut.frontend.nav.utils :as dnu]
   [re-frame.core :as rf]))

(rf/reg-sub :endpoint-routes
  (fn [db]
    (-> (dcu/entities db :endpoint-routes :endpoint-routes/id)
        first
        :endpoint-routes/routes)))
