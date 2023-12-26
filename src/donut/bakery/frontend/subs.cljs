(ns donut.bakery.frontend.subs
  (:require
   [donut.frontend.core.utils :as dcu]
   [donut.frontend.form.flow :as dff]
   [re-frame.core :as rf]
   [clojure.string :as str]))

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

(rf/reg-sub :filtered-dbxray
  :<- [:dbxray]
  :<- [::dff/attr-buffer {:donut.form/key         [:filter :table-name]
                          :donut.input/attr-path  :table-name
                          :donut.form/feedback-fn nil}]
  (fn [[dbxray table-name-filter]]
    (if (str/blank? table-name-filter)
      dbxray
      (update dbxray :table-order (fn [table-names]
                                    (filter #(not= -1 (.indexOf (name %) table-name-filter))
                                            table-names))))))


(rf/reg-sub :system
  (fn [db]
    (-> (dcu/entities db :system :system/id)
        first
        :system/system)))
