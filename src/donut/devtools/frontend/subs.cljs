(ns donut.devtools.frontend.subs
  (:require
   [clojure.string :as str]
   [donut.frontend.form.flow :as dff]
   [donut.frontend.path :as p]
   [re-frame.core :as rf]))

(rf/reg-sub :donut.devtools/endpoint-routes
  (fn [db]
    (p/get-path db :entity [:endpoint-routes :all :endpoint-routes/routes])))

(rf/reg-sub :donut.devtools/dbxray
  (fn [db]
    (p/get-path db :entity [:dbxray :all :dbxray/xray])))

(rf/reg-sub :donut.devtools/filtered-dbxray
  :<- [:donut.devtools/dbxray]
  :<- [::dff/attr-buffer {::dff/form-key         [:filter :table-name]
                          :donut.input/attr-path :table-name}]
  (fn [[dbxray table-name-filter]]
    (if (str/blank? table-name-filter)
      dbxray
      (update dbxray :table-order (fn [table-names]
                                    (filter #(not= -1 (.indexOf (name %) table-name-filter))
                                            table-names))))))


(rf/reg-sub :donut.devtools/system
  (fn [db]
    (p/get-path db :entity [:system :system :system/system])))
