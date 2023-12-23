(ns donut.bakery.backend.system-plugin
  (:require
   [donut.bakery.backend.endpoint-routes :as er]
   [donut.system :as ds]))

(defn inject-plugin
  [system]
  (update-in system [::ds/defs :middleware :routes] into er/routes))
