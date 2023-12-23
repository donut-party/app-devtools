(ns donut.bakery.frontend.core
  (:require
   [donut.frontend.config :as dconf]
   [donut.frontend.core.flow :as dcf]
   [donut.frontend.core.utils :as dcu]
   [donut.frontend.nav.flow :as dnf]
   [donut.system :as ds]
   [donut.generated.endpoint-routes :as endpoint-routes]
   [donut.bakery.frontend.app :as app]
   [donut.bakery.frontend.frontend-routes :as frontend-routes]
   [donut.bakery.frontend.subs] ;; load subs
   [meta-merge.core :as meta-merge]
   [re-frame.core :as rf]
   [reagent.dom :as rdom]))

(defmethod ds/named-system :donut.bakery/frontend
  [_]
  (meta-merge/meta-merge
   dconf/default-config
   {::ds/defs
    {:donut.frontend
     {:sync-router     #::ds{:config {:routes endpoint-routes/routes}}
      :frontend-router #::ds{:config {:routes frontend-routes/routes}}}}}))

(defmethod ds/named-system :donut.bakery/frontend-dev
  [_]
  (ds/system :donut.bakery/frontend))

(defn ^:dev/after-load start []
  (rf/dispatch-sync [::dcf/start-system (ds/system :donut.bakery/frontend-dev)])
  (rf/dispatch-sync [::dnf/dispatch-current])
  (rdom/render [app/app] (dcu/el-by-id "app")))

(defn init
  []
  (start))

(defn ^:dev/before-load stop [_]
  (rf/dispatch-sync [::dcf/stop-system]))
