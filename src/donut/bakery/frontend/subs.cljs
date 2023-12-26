(ns donut.bakery.frontend.subs
  (:require
   [clojure.string :as str]
   [donut.frontend.form.flow :as dff]
   [donut.frontend.path :as p]
   [donut.system :as ds]
   [re-frame.core :as rf]))

(rf/reg-sub :endpoint-routes
  (fn [db]
    (p/get-path db :entity [:endpoint-routes :all :endpoint-routes/routes])))

(rf/reg-sub :dbxray
  (fn [db]
    (p/get-path db :entity [:dbxray :all :dbxray/xray])))

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
    (p/get-path db :entity [:system :system :system/system ::ds/defs])))
