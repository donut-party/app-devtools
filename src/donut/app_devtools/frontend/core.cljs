(ns donut.app-devtools.frontend.core
  (:require
   [donut.compiler.monolith.inject :as inject]
   [donut.frontend.config :as dconf]
   [donut.frontend.core.flow :as dcf]
   [donut.frontend.core.utils :as dcu]
   [donut.frontend.nav.flow :as dnf]
   [donut.system :as ds]
   [donut.app-devtools.frontend.app :as app]
   [donut.app-devtools.frontend.frontend-routes :as frontend-routes]
   [donut.app-devtools.frontend.subs] ;; load subs
   [meta-merge.core :as meta-merge]
   [re-frame.core :as rf]
   [reagent.dom :as rdom]))

(def endpoint-routes (inject/backend-routes))

(defmethod ds/named-system :donut.app-devtools/frontend
  [_]
  (meta-merge/meta-merge
   dconf/default-config
   {::ds/defs
    {:donut.frontend
     {:sync-router     #::ds{:config {:routes endpoint-routes}}
      :frontend-router #::ds{:config {:routes frontend-routes/routes}}}}}))

(defmethod ds/named-system :donut.app-devtools/frontend-dev
  [_]
  (ds/system :donut.app-devtools/frontend))

(defn ^:dev/after-load start []
  (rf/dispatch-sync [::dcf/start-system (ds/system :donut.app-devtools/frontend-dev)])
  (rf/dispatch-sync [::dnf/dispatch-current])
  (rdom/render [app/app] (dcu/el-by-id "app")))

(defn init
  []
  (start))

(defn ^:dev/before-load stop [_]
  (rf/dispatch-sync [::dcf/stop-system]))
