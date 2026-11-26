(ns app-devtools.example.frontend.core
  (:require
   [donut.bakery.inject :as inject]
   [donut.frontend.config :as dconf]
   [donut.frontend.core.flow :as dcf]
   [donut.frontend.core.utils :as dcu]
   [donut.frontend.nav.flow :as dnf]
   [app-devtools.example.frontend.app :as app]
   [app-devtools.example.frontend.frontend-routes :as frontend-routes]
   [app-devtools.example.frontend.subs] ;; load subs
   [donut.system :as ds]
   [meta-merge.core :as meta-merge]
   [re-frame.core :as rf]
   [reagent.dom :as rdom]))

(def endpoint-routes (inject/backend-routes))

(defmethod ds/named-system :frontend
  [_]
  (meta-merge/meta-merge
   dconf/default-config
   {::ds/defs
    {:donut.frontend
     {:sync-router     #::ds{:config {:routes endpoint-routes}}
      :frontend-router #::ds{:config {:routes frontend-routes/routes}}}}}))

(defmethod ds/named-system :frontend-dev
  [_]
  (ds/system :frontend
    ;; add dev routes
    {[:donut.frontend :frontend-router ::ds/config :routes]
     (into frontend-routes/routes [])}))

(defn ^:dev/after-load start []
  (rf/dispatch-sync [::dcf/start-system (ds/system :frontend-dev)])
  (rf/dispatch-sync [::dnf/dispatch-current])
  (rdom/render [app/app] (dcu/el-by-id "app")))

(defn init
  []
  (start))

(defn ^:dev/before-load stop [_]
  (rf/dispatch-sync [::dcf/stop-system]))
